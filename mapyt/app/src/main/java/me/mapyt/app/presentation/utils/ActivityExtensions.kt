package me.mapyt.app.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

fun Activity.setTouchEnabled(isEnabled: Boolean) {
    if (isEnabled) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    } else {
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    view?.let {
        view.clearFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

inline fun <reified T : Activity> Context.startActivity(body: Intent.() -> Unit) {
    startActivity(Intent(this, T::class.java).apply(body))
}

interface AppActivityBase {}

object ActivityBaseUtils {
    fun setFragment(
        activity: AppCompatActivity,
        fragment: Fragment,
        @IdRes viewId: Int,
        addToBackStack: Boolean,
    ) {
        activity.supportFragmentManager.commit {
            replace(viewId, fragment)
            setReorderingAllowed(true)
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            if (addToBackStack)
                addToBackStack(fragment.javaClass.simpleName)
        }
    }
}

/*
* PoC para simular POP de Swift,
* permite crear extensiones para clases de la API de android (AppCompatActivity)
* pero limitandolas a las que heredan de una interface en particular (AppActivityBase)
* TODO: Validar si hay limitantes con generics y constraints
* TODO: Validar si podria impactar de alguna forma en rendimiento
* */
fun <T> T.setFragment(
    fragment: Fragment,
    @IdRes viewId: Int,
    addToBackStack: Boolean = false,
) where T : AppCompatActivity, T : AppActivityBase =
    ActivityBaseUtils.setFragment(this, fragment, viewId, addToBackStack)

fun <T> T.setupToolbar(
    @IdRes toolbarId: Int,
    @StringRes titleId: Int,
    @StringRes subtitleId: Int? = null,
) where T : AppCompatActivity, T : AppActivityBase =
    setupToolbar(findViewById(toolbarId),
        getString(titleId),
        if (subtitleId != null) getString(subtitleId) else null)

fun <T> T.setupToolbar(
    @IdRes toolbarId: Int,
    title: String,
    subtitle: String? = null,
) where T : AppCompatActivity, T : AppActivityBase =
    setupToolbar(findViewById(toolbarId), title, subtitle)

fun <T> T.setupToolbar(
    toolbar: Toolbar,
    title_: String,
    subtitle_: String? = null,
) where T : AppCompatActivity, T : AppActivityBase {
    setSupportActionBar(toolbar)
    supportActionBar?.run {
        title = title_
        subtitle_?.let { subtitle = it }
    }
}

fun <T> T.enableBackNavigation(enabled: Boolean) where T : AppCompatActivity, T : AppActivityBase {
    supportActionBar?.setDisplayHomeAsUpEnabled(enabled)
    supportActionBar?.setDisplayShowHomeEnabled(enabled)
}

fun <T> T.applyBackButtonDefaultBehavior(onSupportNavigateUp: Boolean): Boolean
        where T : AppCompatActivity, T : AppActivityBase {
    onBackPressed()
    return onSupportNavigateUp
}