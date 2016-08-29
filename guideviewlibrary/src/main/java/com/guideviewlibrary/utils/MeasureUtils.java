package com.guideviewlibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by Roy
 * Date: 16/8/10
 */
public class MeasureUtils {

    private static int statusBarHeight = -1;

    public static Rect getLocationInView(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        rect.offset(0, -getStatusBarHeight(view.getContext()));
        return rect;
    }

    public static int[] getScreeSize(Context context) {
        WindowManager wmgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        int[] result = new int[2];
        result[0] = -1;
        result[1] = -1;

        if (wmgr != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            wmgr.getDefaultDisplay().getMetrics(displayMetrics);

            result[0] = displayMetrics.widthPixels;
            result[1] = displayMetrics.heightPixels;
            return result;
        }
        return result;
    }

    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight < 0) {
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int sbar = 0;
            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e1) {

            }
        }

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            statusBarHeight = 44;
//        }

        return statusBarHeight;
    }

    public static Bitmap createBitmapSafely(int width, int height, Bitmap.Config config, int retryCount) {
        try {
            return Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (retryCount > 0) {
                System.gc();
                return createBitmapSafely(width, height, config, retryCount - 1);
            }
            return null;
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
