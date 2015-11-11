package com.facebook.drawee.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * 头像右下角绘制level图标
 *
 * @author wangjian
 * @date 2015年10月21日
 */
public class SimpleDrawViewWithLevelIcon extends SimpleDraweeView {

    private Bitmap mBitmap;
    private Paint mPaint;
    /**
     * level图片默认显示方式
     */
    private ShowType showType = ShowType.OnHalfBorder;

    /**
     * 设置level图片显示方式
     *
     * @param type
     */
    public void setLevelIconShowType(ShowType type) {
        this.showType = type;
    }
    
    public SimpleDrawViewWithLevelIcon(Context context) {
        super(context);
        initData();
    }
    
    public SimpleDrawViewWithLevelIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }
    
    public SimpleDrawViewWithLevelIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData();
    }
    
    private void initData() {
        mPaint = new Paint();
    }
    
    /**
     * 设置level图片资源
     *
     * @param resId 图片id
     */
    public void setLevelIconResource(int resId) {
        mBitmap = BitmapFactory.decodeResource(getResources(), resId);
        invalidate();
    }

    /**
     * 清除level icon
     */
    public void clearLevelIcon() {
        mBitmap = null;
        invalidate();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        // int heigth = getHeight();
        if (mBitmap != null) {
            // 获取绘制图片的宽度
            int iconWidth = mBitmap.getWidth();
            // 获取绘制图片的高度
            // int iconHeight = bitmap.getHeight();
            // 头像半径
            int headRadius = width / 2;

            // 绘制图片的位置计算
            float left = 0;
            Bitmap bitmap = mBitmap;
            if (showType == ShowType.Inscribe) {
                // 图片内切显示
                left = (float) (Math.sqrt(Math.pow((headRadius - iconWidth / 2), 2) / 2) - iconWidth / 2 + headRadius);
            }
            else if (showType == ShowType.OnHalfBorder) {
                // 图片一半显示在头像上
                left = (float) (Math.sqrt(Math.pow(headRadius, 2) / 2) - (iconWidth / 2)) + headRadius;
                // level左边距与level长度大于头像宽度时，对level缩小显示
                if (left + iconWidth > width) {
                    // left = width - iconWidth;
                    float scale = width / (left + iconWidth / 2 * 3);
                    bitmap = resizeBitmap(bitmap, scale);
                }
            }
            canvas.drawBitmap(bitmap, left, left, mPaint);
            canvas.save();
        }
        else {
            // clear 
            canvas.restore();
        }
    }


    /**
     * 进行图片缩放
     *
     * @param bitmap
     * @param scale  放缩倍数
     * @return
     */
    private static Bitmap resizeBitmap(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }


    public static enum ShowType {

        /**
         * 图片内切显示
         */
        Inscribe,
        /**
         * 图片一半显示在头像外一半显示在内
         */
        OnHalfBorder;

        private ShowType() {

        }
    }
}
