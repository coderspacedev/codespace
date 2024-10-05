package coder.apps.space.library.helper

import android.os.*
import java.lang.ref.*

open class LeakGuardHandlerWrapper<T> @JvmOverloads constructor(
    ownerInstance: T,
    looper: Looper? = Looper.myLooper()
) : Handler(looper!!) {

    private val mOwnerInstanceRef = WeakReference(ownerInstance)

    val ownerInstance: T?
        get() = mOwnerInstanceRef.get()
}
