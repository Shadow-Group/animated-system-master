package com.osama.project34.ui.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by bullhead on 9/10/17.
 */

public class FancyTextView extends AppCompatTextView {
    public FancyTextView(Context context) {
        super(context);
        setTypeFace();
    }

    public FancyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeFace();
    }

    public FancyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeFace();
    }

    private void setTypeFace() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Mad.ttf"));
    }
}
