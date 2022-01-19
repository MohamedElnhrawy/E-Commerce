package com.gtera.event

import android.os.Bundle
import androidx.annotation.IdRes
import com.gtera.base.BaseActivity

class ActivityLauncher {
    var mode: ActivityMode? = null
    var activity: Class<out BaseActivity<*, *>?>? =
        null
    @IdRes
    var actionId = 0
    var requestCode: Int? = null
    var resultCode: Int? = null
    var extras: Bundle? = null
    fun reInitValue() {
        mode = ActivityMode.INIT
        activity = null
        actionId = 0
        requestCode = null
        resultCode = null
        extras = null
    }

    fun launcherInit(mode: ActivityMode?, resultCode: Int?, extras: Bundle?) {
        this.mode = mode
        this.resultCode = resultCode
        this.extras = extras
    }

    fun launcherInit(
        mode: ActivityMode?,
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        launcherInit(mode, activity, null, extras)
    }

    fun launcherInit(mode: ActivityMode?, @IdRes actionId: Int, extras: Bundle?) {
        launcherInit(mode, actionId, null, extras)
    }

    fun launcherInit(
        mode: ActivityMode?,
        activity: Class<out BaseActivity<*, *>?>?,
        requestCode: Int?,
        extras: Bundle?
    ) {
        this.mode = mode
        this.activity = activity
        this.requestCode = requestCode
        this.extras = extras
    }

    fun launcherInit(
        mode: ActivityMode?, @IdRes actionId: Int,
        requestCode: Int?, extras: Bundle?
    ) {
        this.mode = mode
        this.actionId = actionId
        this.requestCode = requestCode
        this.extras = extras
    }

    enum class ActivityMode {
        INIT, OPEN_ACTIVITY, OPEN_NEW_ACTIVITY, OPEN_FINISH_ACTIVITY, OPEN_RESULT_ACTIVITY, FINISH_ACTIVITY, SET_RESULT, OPEN_FRAGMENT, OPEN_NEW_FRAGMENT, OPEN_RESULT_FRAGMENT
    }
}