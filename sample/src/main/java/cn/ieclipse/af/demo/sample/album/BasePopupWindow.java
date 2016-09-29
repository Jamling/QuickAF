package cn.ieclipse.af.demo.sample.album;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

import java.util.List;

import cn.ieclipse.af.util.AppUtils;

public abstract class BasePopupWindow<T> extends PopupWindow {
    /**
     * 布局文件的最外层View
     */
    protected View mContentView;
    protected Context mContext;
    /**
     * ListView的数据集
     */
    protected List<T> mDataList;
    
    public BasePopupWindow(Context context, List<T> data, int id) {
        super(context);
        mContext = context;
        mDataList = data;
        mContentView = LayoutInflater.from(context).inflate(id, null);
        init(context);
    }

    private void init(Context context) {
        setContentView(mContentView);
        setWidth(AppUtils.getScreenWidth(context));
        setHeight((int) (AppUtils.getScreenHeight(context) * 0.7));
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        setOutsideTouchable(true);
        setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        initViews();
        initEvents();
    }
    
    public abstract void initViews();
    
    public abstract void initEvents();
    
    public View findViewById(int id) {
        return mContentView.findViewById(id);
    }
}
