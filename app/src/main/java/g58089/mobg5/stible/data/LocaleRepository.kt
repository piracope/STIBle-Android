package g58089.mobg5.stible.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.LocaleManagerCompat
import androidx.core.os.LocaleListCompat
import java.util.IllformedLocaleException

/**
 * Repository that checks whether the game is in Dutch and changes the app's locale.
 */
class LocaleRepository(private val context: Context) {

    /**
     * The two-letter code of the app's current displayed language.
     *
     * It's "fr" by default, or "nl" if the user plays in dutch.
     */
    val language: String
        get() = if (isInNederlands()) "nl" else "fr"

    /**
     * Checks whether the app should be displayed in Dutch.
     */
    fun isInNederlands(): Boolean {
        val appLocales = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        if (appLocales.isBlank()) {
            return LocaleManagerCompat.getSystemLocales(context).toLanguageTags().contains("nl")
        }

        return appLocales.contains("nl")
    }

    /**
     * Sets the app locales to the given String.
     *
     * If the given string isn't valid, the locales will be reset to default.
     */
    fun setLocale(locale: String) {
        try {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale))
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