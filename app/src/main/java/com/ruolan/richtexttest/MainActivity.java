package com.ruolan.richtexttest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_text = (TextView) findViewById(R.id.tv_text);

        String str = "置顶";

        String result = "要我这个置顶有啥用啊，感觉没有啥子用";

        SpannableStringBuilder builder = new SpannableStringBuilder(str + result);
        //构造文字背景圆角
        RadiusBackgroundSpan span = new RadiusBackgroundSpan(getResources().getColor(R.color.colorAccent)
                ,getResources().getColor(R.color.colorPrimary),10,1.5f,(int) spToPixels(this,20f));
        builder.setSpan(span,0,str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //构造文字大小
        AbsoluteSizeSpan spanSize = new AbsoluteSizeSpan((int) spToPixels(this,12f));
        builder.setSpan(spanSize, 0, str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //构造文字大小
        AbsoluteSizeSpan spanSizeLast = new AbsoluteSizeSpan((int) spToPixels(this,20f));
        builder.setSpan(spanSizeLast, str.length(), builder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);


        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(builder);
        sb.append(result);

        tv_text.setText(sb);

    }

    /**
     * SP 转 Pixels
     * @param context
     * @param sp
     * @return
     */
    public  float spToPixels(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }
}
