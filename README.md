# 自定义圆角背景实现富文本标题展示(纯需求实现)

标签（空格分隔）： 开源项目 自定义VIEW

---

###前言
现在项目中需要实现如下图片的一个需求。<br>
![](http://ww1.sinaimg.cn/large/005AQMyXgy1fhph0ogdyuj30ci034mxd.jpg)
<br>
在此描述一下需求，就是这个显示是一个标题，可能这个地方标题我使用了三行显示(一般一个标题两行足够了)，然后在标题前面加一个描述，用于描述是动态、话题、悄悄话类型(给用户直观的表达就是这个item是什么类型的文章或者新闻),那么要求当有标题是两行的时候,我们需要这个描述和这个文字总体是居中的，当时在想的时候可能很不想做(毕竟这个首次看来就是两个TextView吗,如果是两个TextView,我们怎么在第二个TextView为两行的时候,连带着第一个TextView进行居中显示呢,肯定是下面的一种***错误显示***),可以看出来标题只是把剩余的空间占完然后在居中显示了,毕竟不是一个控件,**第一个TextView在短也是有一定宽度的哈**<br>
![](http://ww1.sinaimg.cn/large/005AQMyXgy1fhph7jhacbj30ci03yt8z.jpg)
###想法
这个时候需求描述清晰了,那就来实现吧,其实想想也简单,只要这个标签和标题是一个控件显示的不就行了,这样就可以实现需求了啊,但是问题就是,他俩的style不一样啊。一个要背景,而且还需要调整字体大小(一般描述都要小于字体大小的).其实这个时候大多会想到谷歌官方提供的SpannableStringBuilder/SpannableString了,毕竟用它可以实现富文本,比如文字变色、超链接颜色显示、点击效果、文字可点击等等。。。。我们也可以找到,就是给一段文字的某个地方添加背景,如果这么容易就好了,仔细看下这个标签的背景。。。。(有一个圆角。。。什么圆角),是不是蒙了,哈哈,其实网上也有封装好的富文本,可以几行代码实现这个效果,但是我没必要就实现这个效果而添加了好多无用的类库吧。那么久没办法了,只能自己实现了。

###代码实现
好吧,首先看下我们的圆角背景实现类吧
RadiusBackgroundSpan.java
```
package com.ruolan.richtexttest;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
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
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        mSize = (int) (paint.measureText(text, start, end) + 2 * mRadius);
        //mSize就是span的宽度，span有多宽，开发者可以在这里随便定义规则
        //我的规则：这里text传入的是SpannableString，start，end对应setSpan方法相关参数
        //可以根据传入起始截至位置获得截取文字的宽度，最后加上左右两个圆角的半径得到span宽度
        return mSize;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

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
```
这里我们继承的是ReplacementSpan,这个是系统为我们提供的一个抽象类,顾名思义就是用于替换我们需要的部分。具体的讲解就不多说了,在文章的最后会有ReplacementSpan解释连接。

其实这个时候我们就可以使用了,关于SpannableStringBuilder/SpannableString的使用这里就不多做解释,自行百度或者谷歌。
定义的工具类,方便我们直接使用
```
 /**
     * 用在显示标题的时候在标题的开头加上标签
     *
     * @param context        上下文
     * @param textView       需要显示的view
     * @param tip            提示文字 标签
     * @param result         标题
     * @param tipTextSize    标签的大小 (一般这个标签的大小要小于title的大小)
     * @param resutlTextSize 标题的文字大小
     * @param radius         圆角半径
     */
    public static void titleTipUtils(Context context, TextView textView, String tip, String result, float tipTextSize, int resutlTextSize, int radius) {

        SpannableStringBuilder builder = new SpannableStringBuilder(tip + " "+ result);
        //构造文字背景圆角
        RadiusBackgroundSpan span = new RadiusBackgroundSpan(context.getResources().getColor(R.color.colorAccent)
                , context.getResources().getColor(R.color.colorPrimary), radius, (int) spToPixels(context, resutlTextSize));
        builder.setSpan(span, 0, tip.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //构造文字大小
        AbsoluteSizeSpan spanSize = new AbsoluteSizeSpan((int) spToPixels(context, tipTextSize));
        builder.setSpan(spanSize, 0, tip.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //构造文字大小
        AbsoluteSizeSpan spanSizeLast = new AbsoluteSizeSpan((int) spToPixels(context, resutlTextSize));
        builder.setSpan(spanSizeLast, tip.length(), builder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(builder);
        sb.append(result);

        textView.setText(sb);
    }

    /**
     * SP 转 Pixels
     *
     * @param context 上下文
     * @param sp      sp 字体大小
     * @return pixels
     */
    public static float spToPixels(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }

```
好了，在Activity或者Fragment中使用的时候直接
```
   TitleTipUtils.titleTipUtils(this,tv_text,str,result,10,17,10);
```

好了，是不是很简单，来看下最终效果吧。
![](http://ww1.sinaimg.cn/large/005AQMyXgy1fhphmfxc3pj30f00qo3za.jpg)

###代码链接地址
https://github.com/wuyinlei/RichTextTest
https://github.com/wuyinlei/RichTextTest

###推荐阅读
[教你自定义android中span][1]

  [1]: http://blog.cgsdream.org/2016/07/06/custom-android-span/
