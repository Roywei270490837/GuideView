package com.guideviewlibrary.bean;

import android.view.Gravity;

/**
 * Created by Roy
 * Date: 16/8/11
 */
public class MarginInfo {
    public float topMargin;
    public float leftMargin;
    public float rightMargin;
    public float bottomMargin;

    public int gravity;

    public MarginInfo() {

    }

    public MarginInfo(int gravity, float marginVerticle, float marginHorizontal) {
        if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
            topMargin = marginVerticle;
            bottomMargin = 0;
        }

        if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
            topMargin = 0;
            bottomMargin = marginVerticle;
        }

        if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.LEFT) {
            leftMargin = marginHorizontal;
            rightMargin = 0;
        }

        if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.RIGHT) {
            rightMargin = marginHorizontal;
            leftMargin = 0;
        }
    }

}