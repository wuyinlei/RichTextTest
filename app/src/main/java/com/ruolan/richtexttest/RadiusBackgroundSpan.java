package com.ruolan.richtexttest;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;

/**
 * Created by wuyinlei on 2017/4/7.
 *
 * @function 圆角背景设置  span
 */

public class RadiusBackgroundSpan extends ReplacementSpan {

    private int mSize;
    private int mColor;
    private int mRadius;
    private int mTextColor;
    private int height;

    /**
     * @param color        背景颜色
     * @param textColor    文字颜色
     * @param radius       圆角半径
     * @param baseTextSize baseTextSize
     */
    public RadiusBackgroundSpan(int color, int textColor, int radius, int baseTextSize) {
        mColor = color;
        mRadius = radius;
        mTextColor = textColor;
        height = baseTextSize;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        mSize = (int) (paint.measureText(text, start, end) + 2 * mRadius);
        //mSize就是span的宽度，span有多宽，开发者可以在这里随便定义规则
        //我的规则：这里text传入的是SpannableString，start，end对应setSpan方法相关参数
        //可以根据传入起始截至位置获得截取文字的宽度，最后加上左右两个圆角的半径得到span宽度
        return mSize;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {

        paint.setColor(mColor);//设置背景颜色
        paint.setAntiAlias(true);// 设置画笔的锯齿效果

        //The recommended distance above the baseline for singled spaced text
        //基准线上方
        //The recommended distance below the baseline for singled spaced text
        //基准线下方
        float centHeight = paint.descent() - paint.ascent();

        float diffHeight = height - centHeight;  //这个是后面的字体的大小的高度 -  这个画上的字体的大小的高度  这个地方要设置圆角居中显示

        //left  top  right  bottom    y + paint.ascent()  (float) (y + (paint.descent() * mScale))-7
        RectF oval = new RectF(x, y + paint.ascent() - diffHeight, x + mSize, y + paint.descent());
        //设置文字背景矩形，x为span其实左上角相对整个TextView的x值，y为span左上角相对整个View的y值。paint.ascent()获得文字上边缘，paint.descent()获得文字下边缘
        canvas.drawRoundRect(oval, mRadius, mRadius, paint);//绘制圆角矩形，第二个参数是x半径，第三个参数是y半径
        paint.setColor(mTextColor);//恢复画笔的文字颜色

        canvas.drawText(text, start, end, x + mRadius, y - diffHeight / 2, paint);//绘制文字
    }
}