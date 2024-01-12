package g58089.mobg5.stible.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import g58089.mobg5.stible.data.CurrentSessionRepository
import g58089.mobg5.stible.data.GameHistoryRepository
import g58089.mobg5.stible.data.GameInteraction
import g58089.mobg5.stible.data.GameRulesRepository
import g58089.mobg5.stible.data.LocaleRepository
import g58089.mobg5.stible.data.UserPreferencesRepository
import g58089.mobg5.stible.data.dto.GameRecap
import g58089.mobg5.stible.data.dto.GameRules
import g58089.mobg5.stible.data.dto.GuessResponse
import g58089.mobg5.stible.data.dto.Stop
import g58089.mobg5.stible.data.dto.UserPreferences
import g58089.mobg5.stible.data.network.RequestState
import g58089.mobg5.stible.data.util.ErrorType
import g58089.mobg5.stible.data.util.GameState
import g58089.mobg5.stible.data.util.STIBleException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

private const val TAG = "GameScreenViewModel"

/**
 * The [ViewModel] handling all business logic in the [GameScreen].
 *
 * So the base gameplay.
 */
class GameScreenViewModel(
    private val gameInteraction: GameInteraction,
    private val currentSessionRepo: CurrentSessionRepository,
    private val gameHistoryRepo: GameHistoryRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val localeRepository: LocaleRepository,
    private val gameRulesRepository: GameRulesRepository
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
     * The number of times the user guessed TODAY.
     */
    private val guessCount
        get() = _madeGuesses.size

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
     */
    val canGuess
        get() = gameState == GameState.PLAYING


    private val _madeGuesses = mutableStateListOf<GuessResponse>()

    /**
     * All [GuessResponse] received during the current game session.
     */
    val madeGuesses: List<GuessResponse>
        get() = _madeGuesses
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
        get() = _madeGuesses.maxOfOrNull { it.proximityPecentage } ?: 0.0

    /**
     * The [UserPreferences] containing various key-value pairs.
     */
    private var userPreferences by mutableStateOf(UserPreferences())

    /**
     * Checks if the player has enabled Map/Easy mode.
     */
    val isMapModeEnabled: Boolean
        get() = userPreferences.isMapModeEnabled

    init {
        setupFlowCollectors()
        initializeGame()
    }

    /**
     * Initializes the GameScreen
     */
    fun initializeGame() {
        viewModelScope.launch {
            isGameReady = false
            fetchGameRules()
            if (requestState !is RequestState.Success) {
                // if we can't even get the game rules -> it's over
                return@launch
            }

            // a little thingie to make this available for other ViewModels
            userPreferencesRepository.setMaxGuessCount(gameRules.maxGuessCount)

            // check if today is a new day
            if (userPreferences.lastSeenPuzzleNumber < gameRules.puzzleNumber) {
                currentSessionRepo.clearForNewSession()
                userPreferencesRepository.setLastSeenPuzzleNumber(gameRules.puzzleNumber)
            }

            // old session was already recovered from our flow collectors

            if (_madeGuesses.isNotEmpty()) {
                gameState = getStateAfterGuess(_madeGuesses.last(), GameState.PLAYING)
                if (gameState != GameState.PLAYING) {
                    mysteryStop = _madeGuesses.last().mysteryStop?.name
                }
            } else {
                gameState = GameState.PLAYING
            }

            // when everything is done, tell the view that it can stop displaying a spinny
            // thing or whatever the view decided to show (SEPARATION OF CONCERNS !!!!!!!!!)
            isGameReady = true
        }
    }

    /**
     * Sets up the Flows to automatically put their new data inside my own little silly fields.
     */
    private fun setupFlowCollectors() {
        viewModelScope.launch {
            combine(
                userPreferencesRepository.userData,
                currentSessionRepo.getAllGuessResponses()
            ) { pref, session ->
                userPreferences = pref
                _madeGuesses.clear()
                _madeGuesses.addAll(session)
            }.collect()
        }
    }

    /**
     * Grabs the latest [GameRules] from the back-end.
     */
    private suspend fun fetchGameRules() {
        requestState = RequestState.Loading

        try {
            gameRules = gameRulesRepository.getGameRules(localeRepository.language)
            requestState = RequestState.Success
        } catch (e: STIBleException) {
            requestState = RequestState.Error(e.errorType)
        }
    }


    /**
     * Updates the currently input guess in the view model with what the user wrote.
     */
    fun changeGuess(newGuess: String) {
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
                // display on the screen
                _madeGuesses.add(it)

                // figure out whether the game ended and unblock input if needed
                gameState = getStateAfterGuess(it, oldGameState)
                userGuess = ""

                // save the newly made guess to the session
                currentSessionRepo.insertGuessResponse(it)

                if (gameState != GameState.PLAYING) {
                    handleGameOver(it.mysteryStop)
                }
            }

            if (response == null) {
                gameState = oldGameState
            }
        }
    }

    /**
     * Locks the map mode for the day.
     *
     * Should be called when the map is opened, as the player REALLY started to take advantage of
     * the easy/map mode.
     */
    fun lockMapMode() {
        // map mode shouldn't be locked if the user opens the map AFTER having finished the game.
        if (userPreferences.isMapModeEnabled && (gameState != GameState.WON && gameState != GameState.LOST)) {
            viewModelScope.launch {
                userPreferencesRepository.setMapModeLockPuzzleNumber(gameRules.puzzleNumber)
            }
        }
    }

    /**
     * Generates a [GameRecap] for this session and stores it.
     */
    private suspend fun handleGameOver(mystery: Stop?) {
        mysteryStop = mystery?.name
        if (mystery == null) {
            Log.e(TAG, "Game is over, but no mystery stop was provided by the backend.")
        }

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
    private fun getStateAfterGuess(guess: GuessResponse, oldGameState: GameState): GameState {
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
     */
    private suspend fun sendGuessRequest(): GuessResponse? {
        return try {
            val response =
                gameInteraction.guess(
                    userGuess,
                    gameRules.puzzleNumber,
                    guessCount,
                    localeRepository.language
                )

            requestState = RequestState.Success
            response
        } catch (e: STIBleException) {
            requestState = RequestState.Error(e.errorType)
            null
        }
    }
}