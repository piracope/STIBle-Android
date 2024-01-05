package g58089.mobg5.stible.data

import g58089.mobg5.stible.data.util.Language

/**
 * Repository that checks whether the game is in Dutch and changes the app's locale.
 */
interface LocaleRepository {
    /**
     * The app's current displayed [Language].
     *
     * It's [Language.FRENCH] by default, or [Language.DUTCH] if the user plays in dutch.
     */
    val language: Language

    /**
     * Checks whether the app should be displayed in Dutch.
     */
    fun isInNederlands(): Boolean

    /**
     * Sets the app locales to the given String.
     *
     * If the given string isn't valid, the locales will be reset to default.
     */
    fun setLocale(lang: Language)

    /**
     * Resets the in-app locale to the default state.
     */
    fun resetToDefault()
}