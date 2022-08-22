package com.jpndev.player.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.lang.ref.WeakReference

/**
 * Used for drawing image in vertically center of the text
 * @param drawable [Drawable] which will be added to spannable
 * @author Malwinder Singh
 */
class CenteredImageSpan(drawable: Drawable) : ImageSpan(drawable, ALIGN_BASELINE) {
    //Weak Reference to Drawable
    private var mDrawableRef: WeakReference<Drawable?>? = null
    // Extra variables used to redefine the Font Metrics when an ImageSpan is added
    private var initialDescent: Int? = 0
    // Gap to be made with Drawable and baselines
    private var extraSpace = 0

    override fun getSize(
        paint: Paint, text: CharSequence,
        start: Int, end: Int,
        fontMatric: Paint.FontMetricsInt?
    ): Int {
        val rect = cachedDrawable?.bounds
        // Centers the text with the ImageSpan
        val extraSpace = fontMatric?.ascent?.let { fontMatric.descent.minus(it) }?.let { rect?.bottom?.minus(it) }
        extraSpace?.let {
            if (it >= 0) {
                // Stores the initial descent and computes the margin available
                initialDescent = fontMatric.descent
                this.extraSpace = it
            }
        }
        fontMatric?.apply {
            //Will be non null since initialDescent is already initialized.
            descent = initialDescent?.let { (this@CenteredImageSpan.extraSpace / 2).plus(it) }!!
            bottom = descent
            descent.let { descent ->
                rect?.bottom?.unaryMinus()?.plus(descent)?.let {
                    ascent = it
                }
            }
        }

        fontMatric?.top = fontMatric?.ascent
        return rect?.right!!
    }

    /**
     * see detail message in android.text.TextLine
     *
     * @param canvas the canvas, can be null if not rendering
     * @param text the text to be draw
     * @param start the text start position
     * @param end the text end position
     * @param x the edge of the replacement closest to the leading margin
     * @param top the top of the line
     * @param y the baseline
     * @param bottom the bottom of the line
     * @param paint the work paint
     */
    override fun draw(
        canvas: Canvas, text: CharSequence,
        start: Int, end: Int, x: Float,
        top: Int, y: Int, bottom: Int, paint: Paint
    ) {
        val drawable = cachedDrawable
        canvas.save()
        var transY = drawable?.bounds?.bottom?.let { bottom.minus(it) }
        // this is the key
        transY = transY?.minus(paint.fontMetricsInt.descent / 2 - 8)
        transY?.toFloat()?.let { canvas.translate(x, it) }
        drawable?.draw(canvas)
        canvas.restore()
    }

    /**
     * Redefined locally because it is a private member from
     * DynamicDrawableSpan
     */
    private val cachedDrawable: Drawable?
        get() {
           return mDrawableRef?.get()?: run{
               mDrawableRef = WeakReference(drawable)
               drawable
           }
        }

    companion object {

        /**
         * Returns spannable String with Image added and centered aligned.
         * @param drawableResId Resource ID of Drawable to show in text.
         * @param textInMessageResId Resource ID of Text to be replaced with Drawable
         * @param message Complete text message that includes [textInMessageResId].
         */
        fun getSpannableString(
            @DrawableRes
            drawableResId: Int?, @StringRes textInMessageResId: Int,
            message: String?
        ): SpannableString? {
            return getSpannableString(drawableResId, textInMessageResId, SpannableString(message))
        }

        /**
         * Returns spannable String with Image added and centered aligned.
         * @param drawableResId Resource ID of Drawable to show in text.
         * @param textInMessageResId Resource ID of Text to be replaced with Drawable
         * @param spanStr Complete spannable string that includes [textInMessageResId].
         */
        fun getSpannableString(
            @DrawableRes
            drawableResId: Int?, @StringRes textInMessageResId: Int,
            spanStr: SpannableString?
        ): SpannableString? {
            /* with(ForesightApplication.appContext) {
                val textInMessage = getString(textInMessageResId)
                val indexOfText: Int? =
                    spanStr?.indexOf(textInMessage)
                val imageSpan: ImageSpan? =
                    getDrawableWithBounds(drawableResId)?.let {
                        CenteredImageSpan(
                            it
                        )
                    }

                //Set Image Span on text bounds in the message
                indexOfText?.plus(textInMessage.length)?.let {
                    spanStr.setSpan(
                        imageSpan,
                        indexOfText, it,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                return spanStr
            }
        }*/
            return null
        }
    }
}