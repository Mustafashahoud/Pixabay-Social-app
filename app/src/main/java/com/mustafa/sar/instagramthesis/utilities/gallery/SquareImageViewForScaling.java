package com.mustafa.sar.instagramthesis.utilities.gallery;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * This Class is only to create a new View or Widget in which height = width as written in onMeasure
 * So we choose the name and it must be a child of AppCompatImageView
 * <p>
 * in the item layout "ImageView" you must choose match_parent for the
 */

public class SquareImageViewForScaling extends AppCompatImageView {
    public SquareImageViewForScaling(Context context) {
        super(context);
    }

    public SquareImageViewForScaling(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageViewForScaling(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
