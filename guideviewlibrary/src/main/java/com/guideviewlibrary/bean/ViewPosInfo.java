package com.guideviewlibrary.bean;

import android.graphics.RectF;

/**
 * Created by Roy
 * Date: 16/8/10
 */
public class ViewPosInfo {

    /* 被指引控件的Rectf */
    public RectF rectF;

    /* 被指引控件的介绍View */
//    public View introView;

    public int introViewId;

    /* 介绍View的位置分布 */
    public MarginInfo mMarginInfo;

    public ViewPosInfo() {
    }

    public ViewPosInfo(RectF rectF, int introViewId, MarginInfo marginInfo) {
        this.rectF = rectF;
        this.introViewId = introViewId;
        this.mMarginInfo = marginInfo;
    }
}
