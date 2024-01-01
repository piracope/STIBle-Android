package g58089.mobg5.stible.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.LocaleListCompat
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

    val isInNederlands: Boolean
        get() = AppCompatDelegate.getApplicationLocales().toLanguageTags().contains("nl")

    /* FIXME: returns false if system set to NL but never interacted with in-app lang picker
    If the user never interacted with changeToNederlands(), the returned list of app locales
    will be empty -> returns false, DESPITE THE FACT that the UI is ACTUALLY in NL.
    To get the language, I should use LocaleManagerCompat, but this requires the Context,
    which according to StackOverflow shouldn't be passed to the ViewModel.

    HOW TO FIX THIS : move the language handling OUTSIDE of this, in a new class, like
    LanguageManager or even inside OfflineUserPreferencesRepository. This would be created
    like everything else inside AppDataContainer which HAS the context.
     */
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
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
        }
    }

    fun changeToNederlands(isSufferingEnabled: Boolean) {
        val locale = if (isSufferingEnabled) "nl" else "fr"
        // FIXME: translate every stop in current_session
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale))
    }
}