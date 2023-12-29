package g58089.mobg5.stible.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import g58089.mobg5.stible.data.CurrentSessionRepository
import g58089.mobg5.stible.data.GameHistoryRepository
import g58089.mobg5.stible.data.GameInteraction
import g58089.mobg5.stible.data.dto.GameRecap
import g58089.mobg5.stible.data.dto.GameRules
import g58089.mobg5.stible.data.dto.GuessResponse
import g58089.mobg5.stible.data.dto.Stop
import g58089.mobg5.stible.data.network.RequestState
import g58089.mobg5.stible.data.util.ErrorType
import g58089.mobg5.stible.data.util.GameState
import g58089.mobg5.stible.data.util.Language
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * The [ViewModel] handling all business logic in the [GameScreen].
 *
 * So the base gameplay.
 */
class GameScreenViewModel(
    private val gameInteraction: GameInteraction,
    private val currentSessionRepo: CurrentSessionRepository,
    private val gameHistoryRepo: GameHistoryRepository
) : ViewModel() {

    /**
     * The guessed stop name provided by the user.
     *
     * This should be kept updated with each user input.
     */
    var userGuess by mutableStateOf("")
        private set

    /**
     * The initial data necessary to run the game.
     *
     * Should be queried ONCE at the start of the game.
     */
    var gameRules by mutableStateOf(GameRules())
        private set

    /**
     * The current state of any call to the backend.
     */
    var requestState: RequestState by mutableStateOf(RequestState.Default)
        private set

    /**
     * The user's chosen [Language].
     */
    var userLang by mutableStateOf(Language.FRENCH)
        private set

    /**
     * The number of times the user guessed TODAY.
     */
    private var guessCount by mutableIntStateOf(0)

    /**
     * The current state of play.
     */
    var gameState by mutableStateOf(GameState.BLOCKED)
        private set

    /**
     * The stop the user tries to guess, revealed at the final guess (won or lost).
     */
    var mysteryStop: String? by mutableStateOf(null)

    /**
     * A short hand for [gameState] == PLAYING.
     *
     * Easier for the view.
     * TODO: do i keep this
     */
    val canGuess
        get() = gameState == GameState.PLAYING

    /**
     * All [GuessResponse] received during the current game session.
     */
    val madeGuesses = mutableStateListOf<GuessResponse>()
    /*
    NOTE: I could observe the database's current session, as it's a flow. That said,
    if i replace adding the newly received guess to a mutableList with a database insert,
    it significantly slows down my app. What I could do is instead save it to this list for display
    purposes, unblock the input and send it to the database. So that's what I'm doing.
     */

    /**
     * `true` if we managed to load initial data.
     */
    var isGameReady by mutableStateOf(false)
        private set


    /**
     * The biggest proximity percentage achieved during this session.
     */
    val highestProximity: Double
        get() = madeGuesses.maxOf { it.proximityPecentage }

    init {
        viewModelScope.launch {
            fetchGameRules()
            if (requestState is RequestState.Success) {
                isGameReady = true
            }
            currentSessionRepo.clearForNewSession() // TODO: conditional

            if (isGameReady) {
                gameState = GameState.PLAYING
            }
        }
    }


    /**
     * Grabs the latest [GameRules] from the back-end.
     */
    private suspend fun fetchGameRules() {
        requestState = RequestState.Loading

        try {
            gameRules = gameInteraction.getGameRules(userLang)
            requestState = RequestState.Success
        } catch (e: IOException) {
            requestState = RequestState.Error(ErrorType.NO_INTERNET)
        } catch (e: HttpException) {
            requestState =
                if (e.code() == 400)
                // language wasn't "fr" or "nl" --> should never happen
                    RequestState.Error(ErrorType.BAD_LANGUAGE)
                else
                // idk an error 500 or something you can never be sure
                    RequestState.Error(ErrorType.UNKNOWN)
        }
    }


    /**
     * Updates the currently input guess in the view model with what the user wrote.
     */
    fun changeGuess(newGuess: String) {
        // TODO: check if game over (probably)
        userGuess = newGuess
    }

    /**
     * Makes a guess and saves the returned [GuessResponse], if any.
     */
    fun guess() {
        // pruning guesses made at an impossible time
        if (!canGuess) {
            requestState = RequestState.Error(ErrorType.GAME_OVER)
            return
        }

        // pruning empty guesses
        if (userGuess.isBlank()) {
            requestState = RequestState.Error(ErrorType.BAD_STOP)
            return
        }

        // to prevent going from LOST -> BLOCKED -> PLAYING
        val oldGameState = gameState

        requestState = RequestState.Loading
        gameState = GameState.BLOCKED // block user input
        viewModelScope.launch {

            val response = sendGuessRequest()
            response?.let {
                // display to the database
                madeGuesses.add(it)
                guessCount++ // only increment if we actually succeed at guessing

                // figure out whether the game ended and unblock input if needed
                gameState = handleStateAfterGuess(it, oldGameState)
                userGuess = ""

                // save the newly made guess to the session
                currentSessionRepo.insertGuessResponse(it)

                if (gameState != GameState.PLAYING) {
                    handleGameOver(it.mysteryStop)
                }

            }
        }
    }

    /**
     * Generates a [GameRecap] for this session and stores it.
     */
    private suspend fun handleGameOver(mystery: Stop?) {
        mysteryStop = mystery?.stopName
        // TODO : the stop name is untranslated (GARE DU MIDI instead of Gare du Midi/Zuidstation)
        // that said, it's the same for the web game. would need an additional HTTP request.

        val stopToSave = mystery ?: Stop()
        val recap = GameRecap(gameRules.puzzleNumber, guessCount, highestProximity, stopToSave)
        gameHistoryRepo.insertRecap(recap)
    }

    /**
     * Figures out what is the state of the game after a guess.
     *
     * @param guess the newly received [GuessResponse]
     * @param oldGameState the [GameState] we were in before. Restored if this guess wasn't significant
     *
     * @return the [GameState] according to what happened during this guess
     */
    private fun handleStateAfterGuess(guess: GuessResponse, oldGameState: GameState): GameState {
        return if (guess.directionEmoji == "✅") { // i hate this so much
            GameState.WON
        } else if (guessCount >= gameRules.maxGuessCount) {
            GameState.LOST
        } else {
            oldGameState
        }
    }


    /**
     * Sends the guessed stop name to the back-end and retrieves its response.
     *
     * @return the [GuessResponse] received by the backend or `null` if an error occurred.
     * Details about the error can be found by examining [requestState].
     *
     * TODO: returning null or throwing a custom exception ?
     */
    private suspend fun sendGuessRequest(): GuessResponse? {
        try {
            val response =
                gameInteraction.guess(userGuess, gameRules.puzzleNumber, guessCount, userLang)
            if (response.code() == 205) {
                // I need to catch 205, because it signifies that the client has outdated info
                requestState = RequestState.Error(ErrorType.NEW_LEVEL_AVAILABLE)
                // let user stay blocked
                return null
            }

            if (response.isSuccessful) {
                requestState = RequestState.Success
                return response.body()
            }

            // Can't use HttpException because i have a Response object
            requestState = if (response.code() == 400) {
                /* the stop probably didn't exist.
               well the fun stuff is that a 400 is returned by the server if
               an exception occurs while it's handling it.
               Which is usually when the request body is malformed, so it *is*
               a user error, but still some function could fail and it wouldn't
               be the user's fault. Also past me used 400 for both "this stop doesn't exist"
               and "idk something happened" so now i'm... well i'll just put bad stop.
             */
                RequestState.Error(ErrorType.BAD_STOP)
            } else {
                RequestState.Error(ErrorType.UNKNOWN)
            }
        } catch (e: IOException) {
            requestState = RequestState.Error(ErrorType.NO_INTERNET)
        }

        return null
    }
}