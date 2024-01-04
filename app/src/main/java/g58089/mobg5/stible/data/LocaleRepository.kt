package g58089.mobg5.stible.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.LocaleManagerCompat
import androidx.core.os.LocaleListCompat
import g58089.mobg5.stible.data.util.Language
import java.util.IllformedLocaleException

/**
 * Repository that checks whether the game is in Dutch and changes the app's locale.
 */
class LocaleRepository(private val context: Context) {

    /**
     * The app's current displayed [Language].
     *
     * It's [Language.FRENCH] by default, or [Language.DUTCH] if the user plays in dutch.
     */
    val language: Language
        get() = if (isInNederlands()) Language.DUTCH else Language.FRENCH

    /**
     * Checks whether the app should be displayed in Dutch.
     */
    fun isInNederlands(): Boolean {
        val appLocales = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        if (appLocales.isBlank()) {
            return LocaleManagerCompat.getSystemLocales(context).toLanguageTags()
                .contains(Language.DUTCH.code)
        }

        return appLocales.contains(Language.DUTCH.code)
    }

    /**
     * Sets the app locales to the given String.
     *
     * If the given string isn't valid, the locales will be reset to default.
     */
    fun setLocale(lang: Language) {
        try {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang.code))
        } catch (e: IllformedLocaleException) {
            resetToDefault()
        }
    }

    /**
     * Resets the in-app locale to the default state.
     *
     * The app's language will turn back to whatever the system locale is, or the in-app default
     * if the user's system locale is unsupported.
     */
    fun resetToDefault() {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
    }

}