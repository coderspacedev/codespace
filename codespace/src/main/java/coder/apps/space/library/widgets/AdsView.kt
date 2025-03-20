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
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AdsView, defStyleAttr, 0)
        val layoutResId = typedArray.getResourceId(R.styleable.AdsView_shimmer_preview_layout, 0)
        if (layoutResId != 0) {
            LayoutInflater.from(context).inflate(layoutResId, this, true)
        }
        typedArray.recycle()
    }
}