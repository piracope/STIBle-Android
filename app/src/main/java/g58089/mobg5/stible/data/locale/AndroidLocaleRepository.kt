package g58089.mobg5.stible.data.locale

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.LocaleManagerCompat
import androidx.core.os.LocaleListCompat
import g58089.mobg5.stible.data.LocaleRepository
import g58089.mobg5.stible.data.util.Language
import java.util.IllformedLocaleException

/**
 * Implementation of [LocaleRepository] which uses [AppCompatDelegate] and [LocaleManagerCompat]
 * to handle the locale on an in-app and system basis.
 */
class AndroidLocaleRepository(private val context: Context) : LocaleRepository {
    override val language: Language
        get() = if (isInNederlands()) Language.DUTCH else Language.FRENCH

    override fun isInNederlands(): Boolean {
        var appLocales = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        if (appLocales.isBlank()) {
            appLocales = LocaleManagerCompat.getSystemLocales(context).toLanguageTags()
        }

        return appLocales.contains(Language.DUTCH.code, ignoreCase = true)
    }

    override fun setLocale(lang: Language) {
        try {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang.code))
        } catch (e: IllformedLocaleException) {
            resetToDefault()
        }
    }

    override fun resetToDefault() {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
    }

}