package g58089.mobg5.stible.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import g58089.mobg5.stible.data.CurrentSessionRepository
import g58089.mobg5.stible.data.GameHistoryRepository
import g58089.mobg5.stible.data.UserPreferencesRepository
import kotlinx.coroutines.launch

class SettingsScreenViewModel(
    private val currentSessionRepo: CurrentSessionRepository,
    private val gameHistoryRepo: GameHistoryRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    var isMapModeEnabled by mutableStateOf(false)
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
        }
    }
}