package cn.ieclipse.af.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

public class ClearableEditText extends AppCompatEditText {

    private Drawable initDrawable = null;
    private final boolean alwaysVisible = false;

    public ClearableEditText(Context context) {
        super(context);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        initDrawable();
        initDrawable.setBounds(0, 0, initDrawable.getIntrinsicWidth(), initDrawable.getIntrinsicHeight());
        toggleClearButton();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                ClearableEditText et = ClearableEditText.this;

                if (et.getCompoundDrawables()[2] == null) {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getX() > et.getWidth() - et.getPaddingRight() - initDrawable.getIntrinsicWidth()) {
                    et.setText(null);
                    et.removeClearButton();
                }
                return false;
            }
        });

        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ClearableEditText.this.toggleClearButton();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ClearableEditText.this.toggleClearButton();
            }
        });
    }

    public void toggleClearButton() {
        if (TextUtils.isEmpty(getText()) || !isFocused()) {
            removeClearButton();
        } else {
            addClearButton();
        }
    }

    public void setDrawable(Drawable drawable) {
        initDrawable = drawable;
    }

    private void initDrawable() {
        initDrawable = getCompoundDrawables()[2];
        if (initDrawable == null) {
            TextView text = new TextView(getContext());
            text.setText("×");
            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize());
            text.setDrawingCacheEnabled(true);
            text.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            text.layout(0, 0, text.getMeasuredWidth(), text.getMeasuredHeight());
            text.buildDrawingCache();
            Bitmap bitmap = text.getDrawingCache();
            initDrawable = new BitmapDrawable(getResources(), bitmap);
        }
    }

    private void addClearButton() {
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], initDrawable,
            getCompoundDrawables()[3]);
    }

    private void removeClearButton() {
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], null, getCompoundDrawables()[3]);
    }
}
