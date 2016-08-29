package com.guideview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.guideviewlibrary.Guide;
import com.guideviewlibrary.bean.MarginInfo;
import com.guideviewlibrary.listener.OnDismissListener;


public class MainActivity extends AppCompatActivity {

    private TextView tvHello;
    private TextView tvText;

    private Guide guideFirst;
    private Guide guideSecond;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tvHello = (TextView) findViewById(R.id.tv_hello);
        tvText = (TextView) findViewById(R.id.tv_text);

        findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGuide();
            }
        });
    }

    private void showGuide() {
        guideFirst = new Guide(this)
                .addGuide(tvHello, R.layout.guide_first, new MarginInfo(Gravity.TOP | Gravity.RIGHT, 0, 50))
                .highLightStyle(Guide.HIGHLIGHT_CIRCLE);

        guideSecond = new Guide(this)
                .addGuide(tvText, R.layout.guide_first, new MarginInfo(Gravity.BOTTOM | Gravity.LEFT, 120, 200))
                .highLightStyle(Guide.HIGHLIGHT_RECT)
                .maskBlursStyle(Guide.MASKBLURSTYLE_INNER)
                .BorderOffset(10);

        guideFirst.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (guideSecond != null)
                    guideSecond.show();
            }
        });

        guideFirst.show();

    }
}
