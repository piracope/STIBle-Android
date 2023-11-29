package g58089.mobg5.stible.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

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



}