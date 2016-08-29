package com.guideviewlibrary.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.guideviewlibrary.Constants;
import com.guideviewlibrary.Guide;
import com.guideviewlibrary.bean.ViewPosInfo;
import com.guideviewlibrary.utils.MeasureUtils;


/**
 * Created by Roy
 * Date: 16/8/10
 */
public class GuideView extends FrameLayout {

    private Context mContext;

    private Bitmap mMaskBitmap;
    private Paint mPaint;

    private Guide mGuide;

    int screenW, screenH;

    int boardOffest;

    public GuideView(Context context, Guide guide) {
        super(context);
        this.mContext = context;
        this.mGuide = guide;
        setWillNotDraw(false);
        init();
    }

    private void init() {
        mPaint = new Paint();

        mPaint.setDither(true);
        mPaint.setAntiAlias(true);        // 消除锯齿
        mPaint.setStyle(Paint.Style.FILL);// 设置实线

        if (mGuide.getMaskBlursStyle() != Guide.MASKBLURSTYLE_NONE) {
            BlurMaskFilter.Blur blurStyle = null;
            switch (mGuide.getMaskBlursStyle()) {
                case Guide.MASKBLURSTYLE_SOLID:
                    blurStyle = BlurMaskFilter.Blur.SOLID;
                    break;
                case Guide.MASKBLURSTYLE_NORMAL:
                    blurStyle = BlurMaskFilter.Blur.NORMAL;
                    break;
                case Guide.MASKBLURSTYLE_OUTER:
                    blurStyle = BlurMaskFilter.Blur.OUTER;
                    break;
                case Guide.MASKBLURSTYLE_INNER:
                    blurStyle = BlurMaskFilter.Blur.INNER;
                    break;
            }
            mPaint.setMaskFilter(new BlurMaskFilter(Constants.DEFAULT_WIDTH_BLUR, blurStyle));
        }

        addIntroView();

        int[] screenSize = MeasureUtils.getScreeSize(mContext);
        screenW = screenSize[0];
        screenH = screenSize[1];

        boardOffest = mGuide.getBorderOffest();
    }

    /* 添加高亮区域的介绍View */
    private void addIntroView() {
        for (ViewPosInfo viewPosInfo : mGuide.getViewPosInfos()) {
            View view = LayoutInflater.from(mContext).inflate(viewPosInfo.introViewId, this, false);
            FrameLayout.LayoutParams lp = buildIntroLayoutParams(view, viewPosInfo);
            if (lp == null) continue;
            addView(view, lp);
        }
    }

    private void buildMask() {
        mMaskBitmap = MeasureUtils.createBitmapSafely(screenW, screenH, Bitmap.Config.ARGB_8888, Constants.CREATE_BITMAP_TRY_NUM);
        if (mMaskBitmap == null) {
            throw new RuntimeException("out of memery cause mMaskBitmap create fail");
        }

        Canvas canvas = new Canvas(mMaskBitmap);
        canvas.drawColor(mGuide.getMaskColor());

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        for (ViewPosInfo viewPosInfo : mGuide.getViewPosInfos()) {
            float vWidth = viewPosInfo.rectF.width();
            float vHeight = viewPosInfo.rectF.height();

            float left = viewPosInfo.rectF.left;
            float top = viewPosInfo.rectF.top;
            float right = viewPosInfo.rectF.right;
            float bottom = viewPosInfo.rectF.bottom;

            if (left == 0) {
                left += boardOffest;
            } else if (top == 0) {
                top += boardOffest;
            } else if (right == screenW) {
                right -= boardOffest;
            } else if (bottom == screenH) {
                bottom -= boardOffest;
            }

            float radius;
            RectF rect = new RectF(left, top, right, bottom);
            switch (mGuide.getHighLightStyle()) {
                case Guide.HIGHLIGHT_RECT:
                    canvas.drawRoundRect(rect, boardOffest, boardOffest, mPaint);
                    break;
                case Guide.HIGHLIGHT_CIRCLE:
                    radius = vWidth > vHeight ? vWidth / 2 + 2 * boardOffest : vHeight / 2 + 2 * boardOffest;
                    canvas.drawCircle(left + vWidth / 2, top + vHeight / 2, radius, mPaint);
                    break;
                case Guide.HIGHLIGHT_OVAL:
                    canvas.drawOval(rect, mPaint);
                    break;
            }
        }
    }

    private LayoutParams buildIntroLayoutParams(View view, ViewPosInfo viewPosInfo) {
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        if (lp.leftMargin == (int) viewPosInfo.mMarginInfo.leftMargin &&
                lp.topMargin == (int) viewPosInfo.mMarginInfo.topMargin &&
                lp.rightMargin == (int) viewPosInfo.mMarginInfo.rightMargin &&
                lp.bottomMargin == (int) viewPosInfo.mMarginInfo.bottomMargin) return null;

        lp.leftMargin = (int) viewPosInfo.mMarginInfo.leftMargin;
        lp.topMargin = (int) viewPosInfo.mMarginInfo.topMargin;
        lp.rightMargin = (int) viewPosInfo.mMarginInfo.rightMargin;
        lp.bottomMargin = (int) viewPosInfo.mMarginInfo.bottomMargin;

//        lp.gravity = viewPosInfo.mMarginInfo.gravity;
        if (lp.rightMargin != 0) {
            lp.gravity = Gravity.RIGHT;
        } else {
            lp.gravity = Gravity.LEFT;
        }

        if (lp.bottomMargin != 0) {
            lp.gravity |= Gravity.BOTTOM;
        } else {
            lp.gravity |= Gravity.TOP;
        }
        return lp;
    }


    private void updateTipPos() {
        for (int i = 0, n = getChildCount(); i < n; i++) {
            View view = getChildAt(i);
            ViewPosInfo viewPosInfo = mGuide.getViewPosInfos().get(i);

            LayoutParams lp = buildIntroLayoutParams(view, viewPosInfo);
            if (lp == null) continue;
            view.setLayoutParams(lp);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            buildMask();
            updateTipPos();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mMaskBitmap, 0, 0, null);
        super.onDraw(canvas);
    }
}
