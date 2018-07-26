package cn.ieclipse.af.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import cn.ieclipse.af.R;

/**
 * A new round implementation of widget.
 * <p>
 * <strong>To enable round, the background must not be null</strong>
 * </p>
 * Sample:
 * <pre>
 * &lt;cn.ieclipse.af.view.RoundFrameLayout
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:background="@color/black_alpha_50"
 * android:elevation="10dp"
 * android:paddingBottom="0dp"
 * android:radius="8dp"
 * app:af_borderColor="@color/colorPrimary"
 * app:af_borderWidth="1dp"
 * app:af_corners="top|bottom_right"&gt;
 * </pre>
 *
 * @author Jamling
 * @since 3.0.1
 */
public class RoundFrameLayout extends FrameLayout {
    public RoundFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr,
                            @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    float mBorderWidth = 0;
    int mBorderColor = Color.TRANSPARENT;
    int mRadius;
    int mCorners;
    protected Path mPath;

    protected Paint mPaint;

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundFrameLayout);
            mBorderColor = a.getColor(R.styleable.RoundFrameLayout_af_borderColor, Color.TRANSPARENT);
            mBorderWidth = a.getDimensionPixelOffset(R.styleable.RoundFrameLayout_af_borderWidth, (int) mBorderWidth);
            mRadius = a.getDimensionPixelOffset(R.styleable.RoundFrameLayout_android_radius, mRadius);
            mCorners = a.getInt(R.styleable.RoundFrameLayout_af_corners, 0);
            a.recycle();
        }
    }

    protected void buildPath() {
        mPath = new Path();
        mPath.setFillType(Path.FillType.EVEN_ODD);
        int corners = mCorners > 0 ? mCorners : 0x0f;
        mPath.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), getRadiis(mRadius, corners), Path.Direction.CW);
    }

    float[] getRadiis(int radius, int corners) {
        assert (radius >= 0);
        assert (corners >= 0);
        float[] radii = new float[8];
        if ((corners & 1) == 1) {
            radii[0] = radius;
            radii[1] = radius;
        }
        if ((corners & 2) == 2) {
            radii[2] = radius;
            radii[3] = radius;
        }
        if ((corners & 4) == 4) {
            radii[4] = radius;
            radii[5] = radius;
        }
        if ((corners & 8) == 8) {
            radii[6] = radius;
            radii[7] = radius;
        }
        return radii;
    }

    protected void buildPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mPath == null) {
            buildPath();
        }
        canvas.save();
        canvas.clipPath(mPath);
        super.draw(canvas);
        if (mBorderWidth > 0) {
            if (mPaint == null) {
                buildPaint();
            }
            canvas.drawPath(mPath, mPaint);
        }
        canvas.restore();
    }
}
