package com.erbao.joystar.views;

/**
 * Created by asus on 2018/1/22.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

public class MyHorizontalScrollView extends HorizontalScrollView {

    //滚动条中的水平先行布局
    private LinearLayout mWrpper;
    //水平线性布局的左侧菜单menu
    private ViewGroup mMenu;
    //水平先行布局的右侧线性布局
    private ViewGroup mContent;
    //水平先行布局的右侧线性布局
    private ViewGroup mMenu2;
    //屏幕的宽
    private int mScreenWidth;
    //menu的宽离屏幕右侧的距离
    private int mMenuRightPadding = 80;
    //menu的宽度
    private int mMenuWidth;
    private int mMenuWidth2;
    private boolean once;


    /**
     * 未使用自定义属性时调用
     */
    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*
         * 获取屏幕的宽度
         * 通过context拿到windowManager，在通过windowManager拿到Metrics,用DisplayMetrics接收
         * */
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        //把dp转换成px
        mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mMenuRightPadding,
                context.getResources().getDisplayMetrics());
    }

    /*
     * 设置子view的宽和高
     * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int childCount = getChildCount();//判断是否存在子控件

        if (!once) {
            mWrpper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWrpper.getChildAt(0);
            mContent = (ViewGroup) mWrpper.getChildAt(1);
            mMenu2 = (ViewGroup) mWrpper.getChildAt(2);
            //menu的宽度等于屏幕的宽度减去menu离屏幕右侧的边距
            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            mMenuWidth2 = mMenu2.getLayoutParams().width = mScreenWidth + mMenuRightPadding+mMenuRightPadding/2;
            //右边的先行布局的宽度直接等于屏幕的宽度
            mContent.getLayoutParams().width = mScreenWidth-mMenuRightPadding;
            once = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /*
     * 通过设置偏移量将menu隐藏
     * */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        super.onLayout(changed, l, t, r, b);
        /*
         * 通过scrollTo（x,y）方法设置屏幕的偏移量，x为正
         * 内容向左移动
         * */
        if (changed) {
            this.scrollTo(mMenuWidth, 0);
        }


    }

    /*
     * 因为HorizontalScrollView自己控制move和down的事件
     * 所以我们还要判断一下up.如果当前的x偏移量大于menu宽度的一半
     * 隐藏menu，否则显示menu
     * */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                System.out.println("=========scrollX=========="+scrollX);
                System.out.println("=========mMenuWidth=========="+mMenuWidth);
                System.out.println("=========mMenuWidth=========="+mMenuWidth/2);
                System.out.println("=========mMenuWidth=========="+mMenuWidth2);
                System.out.println("=========mMenuWidth=========="+mScreenWidth);
//                if (scrollX >= mMenuWidth / 2&&scrollX<mScreenWidth) {
//                    this.smoothScrollTo(mMenuWidth, 0);
//                } else {
//                    if (scrollX>(mScreenWidth+mMenuWidth/2)){
//                        System.out.println("=========1==========");
//                      this.smoothScrollTo(mMenuWidth2, 0);
//                    }else {
//                        System.out.println("=========2==========");
//                        this.smoothScrollTo(0, 0);
//                    }
//                }
                if (scrollX >= mMenuWidth / 2) {

                    if (scrollX<mScreenWidth){
                        this.smoothScrollTo(mMenuWidth, 0);
                    }else {
                        this.smoothScrollTo(mMenuWidth2, 0);

                    }
                } else {
                        this.smoothScrollTo(0, 0);
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        System.out.println("======================:"+l+"----:"+t+"---:"+oldl+"---:"+oldt);
        System.out.println("========mMenuWidth==============:"+mMenuWidth);
        //--------------效果1----------------------
        if (l>=mMenuWidth){
            ViewHelper.setTranslationX(mContent, l-mMenuWidth);
        }

//        ViewHelper.setTranslationX(mMenu, l);
//        ViewHelper.setTranslationX(mMenu2, 0);

        //--------------效果5----------------------
//        ViewHelper.setTranslationX(mMenu,0);
        //--------------效果2----------------------
//        ViewHelper.setTranslationX(mMenu, 2*l);
        //--------------效果3----------------------
//        //scale 1~0的变化率
//        float scale = l*1.0f/mMenuWidth;
//        //Content的缩放比例1~0.7
//        float rightScale=scale*0.3f+0.7f;
//        //设置缩放中心
//        ViewHelper.setPivotX(mContent,0);
//        ViewHelper.setPivotY(mContent,mContent.getHeight()/2);
//        //进行缩放
//        ViewHelper.setScaleX(mContent, rightScale);
//        ViewHelper.setScaleY(mContent, rightScale);
        //--------------效果4----------------------
//        //scale 1~0的变化率
//        float scale = l*1.0f/mMenuWidth;
//        //Content的缩放比例1~0.7
//        float rightScale=scale*0.3f+0.7f;
//        //Menu的透明度变化0~1
//        float leftAlpha=1.0f-scale;
//        //Menu的缩放变化0.3-1.0
//        float leftScale=1.0f-scale*0.7f;
//        //mContent设置缩放中心
//        ViewHelper.setPivotX(mContent,0);
//        ViewHelper.setPivotY(mContent,mContent.getHeight()/2);
//        //mContent进行缩放
//        ViewHelper.setScaleX(mContent, rightScale);
//        ViewHelper.setScaleY(mContent, rightScale);
//
//		/*
//		 * 对mMenu进行缩放和设置透明度
//		 * */
//        ViewHelper.setScaleX(mMenu, leftScale);
//        ViewHelper.setScaleY(mMenu, leftScale);
//        ViewHelper.setAlpha(mMenu, leftAlpha);

    }
}
