package g58089.mobg5.stible.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import g58089.mobg5.stible.data.CurrentSessionRepository
import g58089.mobg5.stible.data.GameHistoryRepository
import g58089.mobg5.stible.data.LocaleRepository
import g58089.mobg5.stible.data.UserPreferencesRepository
import g58089.mobg5.stible.data.util.Language
import kotlinx.coroutines.launch

class SettingsScreenViewModel(
    private val currentSessionRepo: CurrentSessionRepository,
    private val gameHistoryRepo: GameHistoryRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val localeRepository: LocaleRepository
) : ViewModel() {
    var isMapModeEnabled by mutableStateOf(false)
        private set

    val isInNederlands: Boolean
        get() = localeRepository.isInNederlands()

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
        val lang = if (isSufferingEnabled) Language.DUTCH else Language.FRENCH
        // FIXME: translate every stop in current_session
        localeRepository.setLocale(lang)
    }
}