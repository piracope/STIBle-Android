package g58089.mobg5.stible.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import g58089.mobg5.stible.model.GameRules
import g58089.mobg5.stible.model.util.ErrorType
import g58089.mobg5.stible.model.util.Language
import g58089.mobg5.stible.network.RequestState
import g58089.mobg5.stible.network.STIBleApi
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

    init {
        requestState = RequestState.Loading

        viewModelScope.launch {
            try {
                // get initial data
                gameRules = STIBleApi.retrofitService.start(userLang.code)
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

            Log.d("ViewModel", "Sample puzzle number : " + gameRules.puzzleNumber)
        }
    }

}