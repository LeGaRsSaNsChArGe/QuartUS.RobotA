package com.example.interfacequartus;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by sebastien on 18-02-28.
 */

public class ImageViewCarrePortrait extends AppCompatImageView {

    public ImageViewCarrePortrait(Context context) {
        super(context);
    }

    public ImageViewCarrePortrait(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewCarrePortrait(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }


}