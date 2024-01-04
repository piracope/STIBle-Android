package g58089.mobg5.stible.data.util

import android.icu.text.Normalizer2

private val REGEX_UNACCENT = "\\p{Mn}+".toRegex()

/**
 * Returns the [CharSequence] with diacritics removed.
 */
fun CharSequence.unaccent(): String {
    val temp = Normalizer2.getNFDInstance().normalize(this)
    return REGEX_UNACCENT.replace(temp, "")
}

