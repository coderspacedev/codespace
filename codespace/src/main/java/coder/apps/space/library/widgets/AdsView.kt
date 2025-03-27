package coder.apps.space.library.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import coder.apps.space.library.R

/** Made by Ravina Sukhadiya*/
class AdsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var shimmerLayoutResId: Int = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AdsView, defStyleAttr, 0)
        shimmerLayoutResId = typedArray.getResourceId(R.styleable.AdsView_shimmer_preview_layout, 0)
        typedArray.recycle()

        inflateShimmer()
    }

    private fun inflateShimmer() {
        if (shimmerLayoutResId != 0) {
            LayoutInflater.from(context).inflate(shimmerLayoutResId, this, true)
        }
    }

    fun resetShimmer() {
        removeAllViews()
        inflateShimmer()
        requestLayout()
        invalidate()
    }
}