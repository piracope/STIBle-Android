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
import g58089.mobg5.stible.data.util.ErrorType
import g58089.mobg5.stible.data.util.Language
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsScreenViewModel(
    private val currentSessionRepo: CurrentSessionRepository,
    private val gameHistoryRepo: GameHistoryRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val localeRepository: LocaleRepository,
    private val gameInteraction: GameInteraction,
) : ViewModel() {
    var isMapModeEnabled by mutableStateOf(false)
        private set

    val isInNederlands: Boolean
        get() = localeRepository.isInNederlands()

    var requestState: RequestState = RequestState.Default
        private set

    init {
        viewModelScope.launch {
            userPreferencesRepository.userData.collect {
                isMapModeEnabled = it.isMapModeEnabled
            }
        }
    }

    fun changeMapMode(isOn: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setIsMapModeEnabled(isOn)
        }
    }

    fun removeAllData() {
        viewModelScope.launch {
            userPreferencesRepository.clearPreferences()
            currentSessionRepo.clearForNewSession()
            gameHistoryRepo.clearGameHistory()
            localeRepository.resetToDefault()
        }
    }

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
                    } catch (e: Exception) {
                        requestState = RequestState.Error(ErrorType.TRANSLATION_FAILURE)
                    }
                }
            }
        }
        localeRepository.setLocale(newLang)
    }
}