package com.erbao.joystar.views;

/**
 * Created by asus on 2018/3/1.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AutoHeightImageView extends ImageView {

    private Drawable mDrawable = null;
    private static int mWidth = 0;

    public AutoHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoHeightImageView(Context context) {
        super(context);
    }

    public AutoHeightImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mDrawable = getDrawable();
        if (mWidth != 0) {
            setAutoHeight();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mWidth == 0) {
            mWidth = getMeasuredWidth();
            if (mDrawable != null) {
                setAutoHeight();
            }
        }
    }

    private void setAutoHeight() {
        float scale = mDrawable.getMinimumHeight() / (float) mDrawable.getMinimumWidth();
        float height = mWidth * scale;
//        setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) height));
        if (getParent() instanceof RelativeLayout) {
            setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) height));
        }
        if (getParent() instanceof LinearLayout) {
            setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) height));
        }
    }
}
