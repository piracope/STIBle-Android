package g58089.mobg5.stible.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import g58089.mobg5.stible.data.CurrentSessionRepository
import g58089.mobg5.stible.data.GameHistoryRepository
import g58089.mobg5.stible.data.GameInteraction
import g58089.mobg5.stible.data.LocaleRepository
import g58089.mobg5.stible.data.UserPreferencesRepository
import g58089.mobg5.stible.data.network.RequestState
import g58089.mobg5.stible.data.util.Language
import g58089.mobg5.stible.data.util.STIBleException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * [ViewModel] handling logic for the settings screen.
 */
class SettingsScreenViewModel(
    private val currentSessionRepo: CurrentSessionRepository,
    private val gameHistoryRepo: GameHistoryRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val localeRepository: LocaleRepository,
    private val gameInteraction: GameInteraction,
) : ViewModel() {

    /**
     * Whether the user has turned on map/easy mode.
     */
    var isMapModeEnabled by mutableStateOf(false)
        private set

    var canChangeMapMode by mutableStateOf(true)
        private set

    /**
     * Checks whether the user chose to play in Dutch.
     */
    val isInNederlands: Boolean
        get() = localeRepository.isInNederlands()

    /**
     * State of any HTTP request.
     */
    var requestState: RequestState = RequestState.Default
        private set

    init {
        viewModelScope.launch {
            userPreferencesRepository.userData.collect {
                isMapModeEnabled = it.isMapModeEnabled
                canChangeMapMode = it.mapModeLockPuzzleNumber != it.lastSeenPuzzleNumber
            }
        }
    }

    /**
     * Updates [isMapModeEnabled]
     */
    fun changeMapMode(isOn: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setIsMapModeEnabled(isOn)
            // FIXME: the player can change Map Mode during a game -> cheating
        }
    }

    /**
     * Wipes everything.
     *
     * Clears the current session and history. Reverts all user settings to their default state.
     * Restores the default Locale.
     */
    fun removeAllData() {
        viewModelScope.launch {
            userPreferencesRepository.clearPreferences()
            currentSessionRepo.clearForNewSession()
            gameHistoryRepo.clearGameHistory()
            localeRepository.resetToDefault()
        }
    }

    /**
     * Switches the in-app Locale to Dutch if [isSufferingEnabled] is true.
     *
     * This method will also ask the backend for a translation of all stops in the current session,
     * so that every word displayed is translated. These translations are done in a separate thread
     * fpr each GuessResponse in the database to avoid having to wait for 0..6 consecutive HTTP
     * requests to finalize before switching the locale.
     *
     * NOTE : this method calls a function that causes a configuration change.
     */
    fun changeToNederlands(isSufferingEnabled: Boolean) {
        val newLang = if (isSufferingEnabled) Language.DUTCH else Language.FRENCH
        val oldLang = localeRepository.language

        viewModelScope.launch {
            val allGuesses = currentSessionRepo.getAllGuessResponses().first()
            for (guess in allGuesses) {
                viewModelScope.launch {
                    try {
                        val newName =
                            gameInteraction.translateStop(guess.stopName, oldLang, newLang)
                        currentSessionRepo.setStopName(guess, newName)
                    } catch (e: STIBleException) {
                        requestState = RequestState.Error(e.errorType)
                    }
                }
            }
        }
        localeRepository.setLocale(newLang)
    }
}