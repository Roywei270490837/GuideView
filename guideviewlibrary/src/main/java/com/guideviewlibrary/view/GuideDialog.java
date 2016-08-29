package com.guideviewlibrary.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Roy
 * Date: 16/8/11
 */
public class GuideDialog extends Dialog {

    public GuideDialog(Context context, int theme, View view) {
        super(context, theme);

        setContentView(view);

        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        setCanceledOnTouchOutside(false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
