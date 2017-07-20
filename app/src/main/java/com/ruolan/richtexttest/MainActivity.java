package com.ruolan.richtexttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv_text = (TextView) findViewById(R.id.tv_text);

        String str = "专题";  //标签

        String result = "要我这个置顶有啥用啊，感觉没有啥子用";  //标题

        TitleTipUtils.titleTipUtils(this, tv_text, str, result, 10, 17, 10);

    }

}
