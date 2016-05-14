package cn.ieclipse.af.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ClearableEditText extends EditText {
    
    private Drawable initDrawable = null;
    private Context context;
    
    public ClearableEditText(Context context) {
        super(context);
        this.context = context;
        init();
    }
    
    public void setDrawable(Drawable drawable) {
        initDrawable = drawable;
    }
    
    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    
    private void init() {
        initDrawable();
        initDrawable.setBounds(0, 0, initDrawable.getIntrinsicWidth(), initDrawable.getIntrinsicHeight());
        manageClearButton();
        
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                
                ClearableEditText et = ClearableEditText.this;
                
                if (et.getCompoundDrawables()[2] == null)
                    return false;
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > et.getWidth() - et.getPaddingRight() - initDrawable.getIntrinsicWidth()) {
                    et.setText("");
                    ClearableEditText.this.removeClearButton();
                }
                return false;
            }
        });
        
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                
                ClearableEditText.this.manageClearButton();
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
                ClearableEditText.this.manageClearButton();
            }
        });
    }
    
    private void manageClearButton() {
        if ("".equals(getText().toString()) || !isFocused())
            removeClearButton();
        else
            addClearButton();
    }
    
    public void initDrawable() {
        if (initDrawable == null) {
            TextView text = new TextView(context);
            text.setText("X");
            text.setTextSize(getTextSize());
            text.setDrawingCacheEnabled(true);
            text.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            text.layout(0, 0, text.getMeasuredWidth(), text.getMeasuredHeight());
            text.buildDrawingCache();
            Bitmap bitmap = text.getDrawingCache();
            initDrawable = new BitmapDrawable(bitmap);
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
