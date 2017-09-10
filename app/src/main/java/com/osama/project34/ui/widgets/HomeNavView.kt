package com.osama.floatingplayer.widgets

import android.content.Context
import android.support.design.widget.NavigationView
import android.util.AttributeSet
import android.view.View

/**
 * Created by bullhead on 7/27/17.
 *
 */
class HomeNavView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NavigationView(context, attrs, defStyleAttr) {
    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        val width = resources.displayMetrics.widthPixels / 1.3
        setMeasuredDimension(width.toInt(), heightSpec)
        val count = childCount
        for (i in 0..count - 1) {
            val v = getChildAt(i)
            // this works because you set the dimensions of the ImageView to FILL_PARENT
            v.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth,
                    View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                    measuredHeight, View.MeasureSpec.EXACTLY))
        }
    }
}