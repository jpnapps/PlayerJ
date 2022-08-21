package com.jpndev.player.utils.extensions

import android.content.res.Resources
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jpndev.player.R
import com.jpndev.player.utils.CenteredImageSpan
import com.jpndev.player.utils.constants.Common
import java.util.Locale

/**
 * Used to get the Spannable String from the CenteredImageSpan from the String input
 * @param placeholderIcon - Icon that needs to be set within the text
 * @param placeholderName - StringRes for the name of the placeholder icon
 * @param isCenteredImageSpan - true/false depending upon whether CenteredImageSpan is invoked or not
 * @return SpannableString corresponding to the respective values
 * @param onClick onClick event called when Image is clicked
 */
fun String.getSpannableString(
    @DrawableRes placeholderIcon: Int?,
    @StringRes placeholderName: Int,
    isCenteredImageSpan: Boolean? = false,
    onClick: (() -> Unit)?= null
): SpannableString? = when (isCenteredImageSpan) {
    true -> CenteredImageSpan.getSpannableString(
        placeholderIcon,
        placeholderName,
        this
    )
    else ->null
    /*else -> CenteredImageSpanTwo.getSpannableString(
        placeholderIcon,
        placeholderName,
        this,
        onClick
    )*/
}

/**
 * Used to get the Spannable String from the CenteredImageSpan from the SpannableString input
 * @param placeholderIcon - Icon that needs to be set within the text
 * @param placeholderName - StringRes for the name of the placeholder icon
 * @param isCenteredImageSpan - true/false depending upon whether CenteredImageSpan is invoked or not
 * @return SpannableString corresponding to the respective values
 */
fun SpannableString.getSpannableString(
    @DrawableRes placeholderIcon: Int?,
    @StringRes placeholderName: Int,
    isCenteredImageSpan: Boolean? = false
): SpannableString? = when (isCenteredImageSpan) {
    true -> CenteredImageSpan.getSpannableString(
        placeholderIcon,
        placeholderName,
        this
    )
    else ->null
   /* else -> CenteredImageSpanTwo.getSpannableString(
        placeholderIcon,
        placeholderName,
        this
    )*/
}

/**
 * Get the string from resources and also replaces [Common.APP_NAME_PLACEHOLDER] with [R.string.def_app_name] (Application name).
 * @param stringRes string resource identifier
 * @return app name replaces string
 */
fun Resources.getAppNameReplacedString(@StringRes stringRes: Int) =
    getString(stringRes).replace(Common.APP_NAME_PLACEHOLDER, getString(R.string.app_name))

/**
 * Replaces [Common.APP_NAME_PLACEHOLDER] with [R.string.def_app_name] (Application name).
 * @return app name replaces string
 */
/*fun String.getAppNameReplacedString() =
    this.replace(Common.APP_NAME_PLACEHOLDER, getString(R.string.app_name))*/

/**
 * Method to Remove special characters
 * @param value String to format
 * @return formatted String
 */
fun String.removeSpecialChars() =
    replace(Common.SPECIAL_CHARACTERS.toRegex(), "")

/**
 * Method to Capitalize string
 * @return String : capitalized string
 */
fun String.capitalizeString(): String? {
    if (this.isNotEmpty()) {
        val array = this.toCharArray()
        var capitalizeNext = true
        val phrase = StringBuilder()
        for (char in array) {
            if (capitalizeNext && Character.isLetter(char)) {
                phrase.append(Character.toUpperCase(char))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(char)) {
                capitalizeNext = true
            }
            phrase.append(char)
        }
        return phrase.toString()
    } else {
        return this
    }
}

/**
 * Makes a substring of a string bold.
 * @param textToBold Text you want to make bold
 * @return String with bold substring
 */
fun String.boldSection(textToBold: String): SpannableStringBuilder? {
    val builder = SpannableStringBuilder()
    if (textToBold.length > 0 && textToBold.trim { it <= ' ' } != "") { //for counting start/end indexes
        val testText = toLowerCase(Locale.US)
        val testTextToBold = textToBold.toLowerCase(Locale.US)
        val startingIndex = testText.indexOf(testTextToBold)
        val endingIndex = startingIndex + testTextToBold.length
        //for counting start/end indexes
        if (startingIndex < 0 || endingIndex < 0) {
            return builder.append(this)
        } else if (startingIndex >= 0 && endingIndex >= 0) {
            builder.append(this)
            builder.setSpan(StyleSpan(Typeface.BOLD), startingIndex, endingIndex, 0)
        }
    } else {
        return builder.append(this)
    }
    return builder
}

/**
 * Makes a string of a string bold.
 * @return String with bold spanString
 */
fun String.bold(): SpannableString? {
    val spanString = SpannableString(this)
    spanString.setSpan(StyleSpan(Typeface.BOLD), 0, spanString.length, 0)
    return spanString
}

/**
 * Resize portion of string in a string
 *
 * @param textToResize String value of text to be resized
 * @param textSize Int value of text size
 * @return String with resize spanString
 */
fun String.resize(textToResize: String, textSize : Int): SpannableStringBuilder? {
    val builder = SpannableStringBuilder()
    if (textToResize.isNotEmpty() && textToResize.trim { it <= ' ' } != "") { //for counting start/end indexes
        val testText = toLowerCase(Locale.US)
        val testTextToBold = textToResize.toLowerCase(Locale.US)
        val startingIndex = testText.indexOf(testTextToBold)
        val endingIndex = startingIndex + testTextToBold.length
        //for counting start/end indexes
        if (startingIndex < 0 || endingIndex < 0) {
            return builder.append(this)
        } else if (startingIndex >= 0 && endingIndex >= 0) {
            builder.append(this)
            builder.setSpan(AbsoluteSizeSpan(textSize), startingIndex, endingIndex, SPAN_INCLUSIVE_INCLUSIVE)
        }
    } else {
        return builder.append(this)
    }
    return builder
}

/**
 * Method to remove new line characters from a string
 * @return formatted String
 */
fun String.escapeNewLineChars() =
    replace(Common.NEW_LINE_CHARACTER, " ")

/**
 * Supply the Currency Symbol, e.g. US$, would return $ and CA$ would return $
 * @return the character escaped currency symbol
 */
fun String.isolateCurrencySymbol(): String {
    for (char in this) {
        if (char !in CharRange('A', 'Z')) {
            return char.toString()
        }
    }
    return this
}