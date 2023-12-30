package g58089.mobg5.stible.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import g58089.mobg5.stible.STIBleApplication
import g58089.mobg5.stible.ui.screens.GameScreenViewModel
import g58089.mobg5.stible.ui.screens.SettingsScreenViewModel

/**
 * Provides a factory to create instances of ViewModels for the entire STIBle app.
 */
object STIBleViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            GameScreenViewModel(
                stibleApplication().container.gameInteraction,
                stibleApplication().container.currentSessionRepository,
                stibleApplication().container.gameHistoryRepository,
                stibleApplication().container.userPreferencesRepository
            )
        }
        initializer {
            SettingsScreenViewModel(
                stibleApplication().container.currentSessionRepository,
                stibleApplication().container.gameHistoryRepository,
                stibleApplication().container.userPreferencesRepository
            )
        }
    }
}

/**
 * Returns the currently running [STIBleApplication].
 */
fun CreationExtras.stibleApplication(): STIBleApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as STIBleApplication)