package coder.apps.space.library.helper

import android.app.Activity
import coder.apps.space.library.BuildConfig
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager

fun Activity.launchInAppReviewFlow(complete: () -> Unit?, error: () -> Unit?) {
    val reviewManager = if (BuildConfig.DEBUG) {
        FakeReviewManager(this)
    } else {
        ReviewManagerFactory.create(this)
    }
    val reviewFlowRequest = reviewManager.requestReviewFlow()

    reviewFlowRequest.addOnCompleteListener { requestInfo ->
        if (requestInfo.isSuccessful) {
            val reviewInfo = requestInfo.result
            val flow = reviewManager.launchReviewFlow(this, reviewInfo)
            flow.addOnCompleteListener {
                complete.invoke()
            }
        } else {
            error.invoke()
        }
    }
}

fun Activity.checkIfUserHasReviewedApp(reviewed: (isRated: Boolean) -> Unit?) {
    val reviewManager = ReviewManagerFactory.create(this)
    val request = reviewManager.requestReviewFlow()
    if (request.isSuccessful) {
        reviewed.invoke(false)
    } else {
        // The user has already reviewed the app.
        reviewed.invoke(true)
    }
}



