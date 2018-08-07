package cn.ieclipse.af.view.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ieclipse.af.R;
import cn.ieclipse.af.util.ViewUtils;
import cn.ieclipse.af.volley.RestError;

/**
 * RefreshableListView的empty view。 如果是网络原因，则显示网络加载失败层，并提供重试callback；
 * 如果返回数据为空，则显示内容为空的提示
 *
 * @author lijiaming2
 * @date 2015/6/5
 */
public class EmptyView extends FrameLayout implements View.OnClickListener {
    private View mLoadingLayout;
    private View mErrorLayout;
    private View mDataEmptyLayout;
    private ImageView mIvEmpty;
    private TextView mTvEmpty;
    private ImageView mIvError;
    private TextView mTvError;
    private TextView mTvLoading;

    private RetryListener mListener;

    public static final int LAYER_LOADING = 0;
    public static final int LAYER_ERROR = 1;
    public static final int LAYER_EMPTY = 2;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLoadingLayout = findViewById(R.id.ptr_empty_layout_loading);
        mErrorLayout = findViewById(R.id.ptr_empty_layout_error);
        mDataEmptyLayout = findViewById(R.id.ptr_empty_layout_empty);
        mErrorLayout.setOnClickListener(this);
        mDataEmptyLayout.setOnClickListener(this);
        mTvEmpty = (TextView) findViewById(R.id.ptr_empty_tv_empty);
        mIvEmpty = (ImageView) findViewById(R.id.ptr_empty_iv_empty);
        mTvError = (TextView) findViewById(R.id.ptr_empty_tv_error);
        mIvError = (ImageView) findViewById(R.id.ptr_empty_iv_error);
        mTvLoading = (TextView) findViewById(R.id.ptr_empty_tv_loading);
    }

    @Override
    public void onClick(View v) {
        if (v == mErrorLayout) {
            if (mListener != null) {
                showLoadingLayout();
                mListener.onErrorClick();
            }
        }
        else if (mDataEmptyLayout == v) {
            if (mListener != null) {
                showLoadingLayout();
                mListener.onDataEmptyClick();
            }
        }
    }

    public void showLoadingLayout() {
        mLoadingLayout.setVisibility(View.VISIBLE);
        mDataEmptyLayout.setVisibility(View.INVISIBLE);
        mErrorLayout.setVisibility(View.INVISIBLE);
    }

    public void showErrorLayout(RestError error) {
        // TODO
        if (error != null) {
            mErrorLayout.setVisibility(View.VISIBLE);
            mDataEmptyLayout.setVisibility(View.INVISIBLE);
        }
        else {
            mDataEmptyLayout.setVisibility(View.VISIBLE);
            mErrorLayout.setVisibility(View.INVISIBLE);
        }
        mLoadingLayout.setVisibility(View.INVISIBLE);
        postInvalidate();
    }

    public void showEmptyLayout() {
        mLoadingLayout.setVisibility(View.INVISIBLE);
        mErrorLayout.setVisibility(View.INVISIBLE);
        mDataEmptyLayout.setVisibility(View.VISIBLE);
    }

    public void setDesc(int layer, CharSequence desc) {
        if (layer == LAYER_LOADING) {
            if (mTvLoading != null) {
                mTvLoading.setText(desc);
            }
        }
        else if (layer == LAYER_ERROR) {
            if (mTvError != null) {
                mTvError.setText(desc);
            }
        }
        else if (layer == LAYER_EMPTY) {
            if (mTvEmpty != null) {
                mTvEmpty.setText(desc);
            }
        }
    }

    public void setImage(int layer, int resId) {
        if (layer == LAYER_LOADING) {

        }
        else if (layer == LAYER_ERROR) {
            ViewUtils.setImageResource(mIvError, resId);
        }
        else if (layer == LAYER_EMPTY) {
            ViewUtils.setImageResource(mIvEmpty, resId);
        }
    }

    public void setRetryListener(RetryListener listener) {
        this.mListener = listener;
    }

    public interface RetryListener {
        void onErrorClick();

        void onDataEmptyClick();
    }
}
