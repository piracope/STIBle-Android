package g58089.mobg5.stible.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import g58089.mobg5.stible.model.Repository
import g58089.mobg5.stible.model.dto.GameRules
import g58089.mobg5.stible.model.dto.GuessResponse
import g58089.mobg5.stible.model.util.ErrorType
import g58089.mobg5.stible.model.util.Language
import g58089.mobg5.stible.network.RequestState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * The ViewModel handling all business logic in the main screen.
 *
 * So the base gameplay.
 */
class MainScreenViewModel : ViewModel() {

    /**
     * The guess provided by the user.
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
     * The user's chosen language.
     */
    var userLang by mutableStateOf(Language.FRENCH)
        private set

    /**
     * The number of times the user guessed TODAY.
     */
    var guessCount by mutableIntStateOf(0)
        private set

    val canGuess // TODO: use this
        get() = guessCount < gameRules.maxGuessCount

    var madeGuesses = mutableStateListOf<GuessResponse>()
        private set

    init {
        requestState = RequestState.Loading

        viewModelScope.launch {
            try {
                // get initial data
                gameRules = Repository.getGameRules(userLang)
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
    }

    fun guessChange(newGuess: String) {
        // TODO: check if game over (probably)
        userGuess = newGuess
    }

    fun guess() {
        requestState = RequestState.Loading

        if (userGuess.isBlank()) {
            requestState = RequestState.Error(ErrorType.BAD_STOP)
            return
        }

        viewModelScope.launch {
            try {
                val response =
                    Repository.guess(userGuess, gameRules.puzzleNumber, guessCount, userLang)
                if (response.code() == 205) {
                    // I need to catch 205, because it signifies that the client has outdated info
                    requestState = RequestState.Error(ErrorType.NEW_LEVEL_AVAILABLE)
                    return@launch
                }

                if (response.isSuccessful) {
                    // i have no idea what this does but Android Studio wrote it so i guess it's good
                    response.body()?.let {
                        madeGuesses.add(it)
                        guessCount++ // only increment if we actually succeed at guessing
                        userGuess = ""
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
        }
    }

}