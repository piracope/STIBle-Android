package g58089.mobg5.stible.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import g58089.mobg5.stible.model.GameInteraction
import g58089.mobg5.stible.model.dto.GameRules
import g58089.mobg5.stible.model.dto.GuessResponse
import g58089.mobg5.stible.model.network.RequestState
import g58089.mobg5.stible.model.util.ErrorType
import g58089.mobg5.stible.model.util.GameState
import g58089.mobg5.stible.model.util.Language
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * The [ViewModel] handling all business logic in the [GameScreen].
 *
 * So the base gameplay.
 */
class GameScreenViewModel(private val gameInteraction: GameInteraction) : ViewModel() {

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
    var madeGuesses = mutableStateListOf<GuessResponse>()
        private set

    /**
     * Calls the backend for initial game data.
     */
    init {
        requestState = RequestState.Loading

        viewModelScope.launch {
            try {
                // get initial data
                gameRules = gameInteraction.getGameRules(userLang)
                requestState = RequestState.Success
                gameState = GameState.PLAYING
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
    }

    /**
     * Updates the currently input guess in the view model with what the user wrote.
     */
    fun guessChange(newGuess: String) {
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

        requestState = RequestState.Loading

        // to prevent going from LOST -> BLOCKED -> PLAYING
        val oldGameState = gameState

        gameState = GameState.BLOCKED // block user input
        viewModelScope.launch {
            try {
                val response =
                    gameInteraction.guess(userGuess, gameRules.puzzleNumber, guessCount, userLang)
                if (response.code() == 205) {
                    // I need to catch 205, because it signifies that the client has outdated info
                    requestState = RequestState.Error(ErrorType.NEW_LEVEL_AVAILABLE)
                    // let user stay blocked
                    return@launch
                }

                if (response.isSuccessful) {
                    // i have no idea what this does but Android Studio wrote it so i guess it's good
                    response.body()?.let {
                        madeGuesses.add(it)
                        guessCount++ // only increment if we actually succeed at guessing

                        // handle game over
                        gameState = if (it.directionEmoji == "✅") { // i hate this so much
                            GameState.WON
                        } else if (guessCount >= gameRules.maxGuessCount) {
                            GameState.LOST
                        } else {
                            oldGameState
                        }

                        if (gameState != GameState.PLAYING) {
                            mysteryStop = it.mysteryStop?.stopName
                            // TODO : the stop name is untranslated (GARE DU MIDI instead of Gare du Midi/Zuidstation)
                        }

                        userGuess = "" // reset input
                    }
                    requestState = RequestState.Success
                    return@launch
                }

                // Can't use HttpException because i have a Response object
                requestState = if (response.code() == 400) {
                    /* the stop probably didn't exist.
                   well the fun stuff is that a 400 is returned by the server if
                   an exception occurs while it's handling it.
                   Which is usually when the request body is malformed, so it *is*
                   a user error, but still some function could fail and it wouldn't
                   be the user's fault. Also past me used 400 for both "this stop doesn't exist"
                   and "idl something happened" so now i'm... well i'll just put bad stop.
                 */
                    RequestState.Error(ErrorType.BAD_STOP)
                } else {
                    RequestState.Error(ErrorType.UNKNOWN)
                }
            } catch (e: IOException) {
                requestState = RequestState.Error(ErrorType.NO_INTERNET)
            }

            // non fatal errors
            gameState = oldGameState
        }
    }
}