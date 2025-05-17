package coder.apps.space.library.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication

abstract class CodeApp : MultiDexApplication(), Application.ActivityLifecycleCallbacks {

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = applicationContext
        create()
        registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                lifecycleStart()
            }
        })
    }


    abstract fun create()
    abstract fun lifecycleStart()

    fun isShowOpenAdsOnStart(classname: String, isShowingAd: Boolean): Boolean {
        if (classname == "com.google.android.gms.ads.AdActivity" || isShowingAd) {
            return false
        }
        for (aClass in classes) {
            if (aClass.name.equals(classname, ignoreCase = true)) {
                return false
            }
        }

        return true
    }

    open fun setAvoidMultipleClass(aClass: MutableList<Class<*>>) {
        classes.addAll(aClass)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    companion object {
        private var instance: CodeApp? = null
        private var appContext: Context? = null
        fun getInstance(): CodeApp =
            instance ?: throw IllegalStateException("Application is not created yet!")

        fun getAppContext(): Context =
            appContext ?: throw IllegalStateException("Application is not created yet!")

        var currentActivity: Activity? = null
        var classes: MutableList<Class<*>> = mutableListOf()
    }
}