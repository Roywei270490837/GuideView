package com.guideview;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import com.guideview.bean.MarginInfo;
import com.guideview.bean.ViewPosInfo;
import com.guideview.listener.OnDismissListener;
import com.guideview.utils.MeasureUtils;
import com.guideview.view.GuideDialog;
import com.guideview.view.GuideView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roy
 * Date: 16/8/10
 */
public class Guide {

    public static final int HIGHLIGHT_RECT = 0;   // 矩形
    public static final int HIGHLIGHT_CIRCLE = 1; // 圆形
    public static final int HIGHLIGHT_OVAL = 2;   // 椭圆形

    public static final int MASKBLURSTYLE_SOLID = 0;
    public static final int MASKBLURSTYLE_NORMAL = 1;
    public static final int MASKBLURSTYLE_OUTER = 2;
    public static final int MASKBLURSTYLE_INNER = 3;
    public static final int MASKBLURSTYLE_NONE = 4;

    public static final int SHOWSTYLE_TOGETHER = 0; // 同时显示
    public static final int SHOWSTYLE_ORDER = 1;    // 按顺序依次显示

    private Context mContext;

    /* 需要显示的引导对象集 */
    private List<ViewPosInfo> mViewPosInfo;
    /* 最终显示的引导View */
    private GuideView mGuideView;
    private GuideDialog mGuidDialog;

    /* 背景颜色 */
    private int maskColor = 0xCC000000;
    /* 高亮区域形状 */
    private int highLightStyle = HIGHLIGHT_RECT;
    /* 高亮区域边缘宽度 */
    private int borderOffest;
    /* 高亮区域边缘高斯模糊类型 */
    private int maskBlursStyle = MASKBLURSTYLE_NONE;
    /* 引导View显示的方式 */
    private int showStyle = SHOWSTYLE_TOGETHER;

    /* 引导结束时的回调 */
    private OnDismissListener dismissListener;

    public Guide(Context context) {
        this.mContext = context;
        mViewPosInfo = new ArrayList<>();
        borderOffest = MeasureUtils.dip2px(mContext, Constants.BOARD_OFFSET);
    }

    public Guide maskColor(int maskColor) {
        this.maskColor = maskColor;
        return this;
    }

    public Guide highLightStyle(int highLightStyle) {
        this.highLightStyle = highLightStyle;
        return this;
    }

    public Guide BorderOffset(int offest) {
        this.borderOffest = offest;
        return this;
    }

    public Guide maskBlursStyle(int maskBlursStyle) {
        this.maskBlursStyle = maskBlursStyle;
        return this;
    }

    public Guide showStyle(int showStyle) {
        this.showStyle = showStyle;
        return this;
    }

    public Guide setOnDismissListener(OnDismissListener listener) {
        this.dismissListener = listener;
        return this;
    }

    public Guide addGuide(View targetView, int introViewId, MarginInfo marginInfo) {
        return addGuide(MeasureUtils.getLocationInView(targetView), introViewId, marginInfo);
    }

    public Guide addGuide(Rect rect, int introViewId, MarginInfo marginInfo) {
        ViewPosInfo viewPosInfo = new ViewPosInfo(new RectF(rect), introViewId, marginInfo);
        mViewPosInfo.add(viewPosInfo);
        return this;
    }

    public void show() {
        if (mGuideView == null) {
            mGuideView = new GuideView(mContext, this);
        }
        mGuidDialog = new GuideDialog(mContext, R.style.dialog, mGuideView);
        mGuidDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (dismissListener != null) {
                    dismissListener.onDismiss();
                }
            }
        });
        mGuidDialog.show();

    }

    public void remove() {
        if (mGuidDialog != null) {
            mGuidDialog.dismiss();
        }
    }

    public boolean isShow() {
        return mGuidDialog != null && mGuidDialog.isShowing();
    }

    public int getMaskColor() {
        return maskColor;
    }

    public int getHighLightStyle() {
        return highLightStyle;
    }

    public int getBorderOffest() {
        return borderOffest;
    }

    public int getMaskBlursStyle() {
        return maskBlursStyle;
    }

    public int getShowStyle() {
        return showStyle;
    }

    public List<ViewPosInfo> getViewPosInfos() {
        return mViewPosInfo;
    }
}
