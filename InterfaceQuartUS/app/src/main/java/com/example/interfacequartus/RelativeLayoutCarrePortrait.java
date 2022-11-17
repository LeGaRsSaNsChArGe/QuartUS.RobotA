package com.example.interfacequartus;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by sebastien on 18-02-28.
 */

public class RelativeLayoutCarrePortrait extends RelativeLayout {

    public RelativeLayoutCarrePortrait(Context context) {
        super(context);
    }

    public RelativeLayoutCarrePortrait(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeLayoutCarrePortrait(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }


}