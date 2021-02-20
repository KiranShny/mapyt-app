package me.mapyt.app.presentation.base

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

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
            if(addToBackStack)
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
)
        where T : AppCompatActivity, T : AppActivityBase =
    ActivityBaseUtils.setFragment(this, fragment, viewId, addToBackStack)