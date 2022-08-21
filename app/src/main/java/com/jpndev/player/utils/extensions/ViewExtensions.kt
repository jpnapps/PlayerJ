package com.jpndev.player.utils.extensions

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat


/**
 * Custom Kotlin extensions for View operations.
 * @author JPNapps
 */

/**
 * Makes a view visible
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * Sets view's visibility to GONE.
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * Sets view's visibility to INVISIBLE.
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Makes view enabled.
 */
fun View.enable() {
    isEnabled = true
}
/**
 * Makes view disabled.
 */
fun View.disable() {
    isEnabled = false
}

/**
 * Extension method to show a keyboard for View.
 */
fun View.showKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        this@showKeyboard.requestFocus()
        showSoftInput(this@showKeyboard, 0)
    }
}

/**
 * Try to hide the keyboard and returns whether it worked
 */
fun View.hideKeyboard(): Boolean {
    return (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Extension to listen the event for keyboard visibility
 * If keyboard is visible then it will return true and will return false if not visible
 */
fun View.keyboardVisibilityChangeListener(method: ((Boolean) -> Unit)) {
    var isKeyBoardVisible = false
    this.viewTreeObserver.addOnGlobalLayoutListener {
        val rect = Rect()
        this.getWindowVisibleDisplayFrame(rect);
        val screenHeight = this.rootView.height;
        // rect.bottom is the position above soft keypad or device button.
        // if keypad is shown, the r.bottom is smaller than that before.
        val keypadHeight = screenHeight - rect.bottom;
        // 0.15 ratio is perhaps enough to determine keypad height.
        if (keypadHeight > screenHeight * 0.15) {
            // keyboard is opened
            if (!isKeyBoardVisible) {
                isKeyBoardVisible = true
                method.invoke(isKeyBoardVisible)
            }
        } else {
            // keyboard is closed
            if (isKeyBoardVisible) {
                isKeyBoardVisible = false
                method.invoke(isKeyBoardVisible)
            }
        }
    }
}



/**
 * Finds view by id using nullable resource identifier
 * @param id [IdRes] of the view to find
 * @return view of [T] type if [id] is non null. If [id] is null, it returns null.
 */
fun <T : View?> View.findViewById(@IdRes id: Int?): T? {
    return if (id == null) {
        null
    } else {
        findViewById<T>(id)
    }
}

/**
 * Extension to remove view from current parent.
 */
fun View.removeFromParent() {
    val parent = parent as? ViewGroup ?: return
    parent.removeView(this)
}

/**
 * Extension to set the startDrawable of the textview
 * @param drawable for startDrawable of the textview
 */
fun AppCompatTextView.startDrawable(drawable: Int) {
    val drawableStart: Drawable? = ContextCompat.getDrawable(context, drawable)
    this.setCompoundDrawablesWithIntrinsicBounds(drawableStart, null, null, null)
}