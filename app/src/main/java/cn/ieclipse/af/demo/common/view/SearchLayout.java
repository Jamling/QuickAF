package cn.ieclipse.af.demo.common.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ieclipse.af.adapter.AfBaseAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.graphics.RoundedColorDrawable;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.FileUtils;
import cn.ieclipse.af.util.KeyboardUtils;
import cn.ieclipse.af.view.FlowLayout;

/**
 * 搜索，在4.x上，如果popup windows height为match_parent，弹出时，内容会在status bar 下面。需要修正。
 *
 * @author wangjian
 * @date 2015/9/29.
 */
public class SearchLayout extends LinearLayout implements View.OnClickListener {

    protected SearchPopupWindow mSearchPopupWindow;

    public SearchLayout(Context context) {
        this(context, null);
    }

    public SearchLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mSearchPopupWindow == null) {
            mSearchPopupWindow = new SearchPopupWindow(this);
        }

        mSearchPopupWindow.init();
        mSearchPopupWindow.showWindow();
    }

    public void setSearchPopupWindow(SearchPopupWindow popupWindow) {
        mSearchPopupWindow = popupWindow;
    }

    /**
     * Search popups
     */
    public static class SearchPopupWindow<T> implements View.OnKeyListener, View.OnClickListener,
        AdapterView.OnItemClickListener, TextWatcher, PopupWindow.OnDismissListener {
        public static final String HISTORY_FILE = "search_history";
        protected Context context;
        protected SearchLayout mSearchLayout;
        protected PopupWindow mPopupWindows;
        protected ListView mListView;
        protected View mListHeader;
        protected TextView mEmptyText;
        protected EditText mEditInput;
        // Top Search Bar
        protected View mSearchBar;
        protected View mClearInput;
        protected View mCancelView;
        protected View mClearHistory;
        protected FlowLayout mFlHotTags;
        protected AfBaseAdapter mAdapter;
        protected String mHintText;

        protected boolean mInitialized = false;
        protected View mAnchor;

        public SearchPopupWindow(SearchLayout searchLayout) {
            context = searchLayout.getContext();
            mSearchLayout = searchLayout;
            mSearchLayout.setSearchPopupWindow(this);
        }

        public void init() {
            if (mInitialized) {
                return;
            }
            mInitialized = true;
            View layout = LayoutInflater.from(context).inflate(R.layout.common_search_popup, null);
            mSearchBar = layout.findViewById(R.id.top_view);
            mClearInput = layout.findViewById(R.id.iv_clear);
            mCancelView = layout.findViewById(R.id.btn_cancel);
            mEditInput = (EditText) layout.findViewById(R.id.et_text);
            mFlHotTags = (FlowLayout) layout.findViewById(R.id.fl_hot_tags);
            mClearHistory = layout.findViewById(R.id.tv_clear);

            if (mClearInput != null) {
                mClearInput.setOnClickListener(this);
            }
            if (mCancelView != null) {
                mCancelView.setOnClickListener(this);
            }
            if (mClearHistory != null) {
                mClearHistory.setOnClickListener(this);
            }

            RoundedColorDrawable bg = new RoundedColorDrawable(AppUtils.dp2px(context, 6), 0);
            bg.setBorder(AppUtils.getColor(context, R.color.colorPrimary), 1).applyTo(
                layout.findViewById(R.id.ll_input));

            mEditInput.setOnKeyListener(this);
            mEditInput.addTextChangedListener(this);

            mPopupWindows = new PopupWindow(context);
            mPopupWindows.setContentView(layout);
            mPopupWindows.setTouchable(true);
            mPopupWindows.setFocusable(true);
            mPopupWindows.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            mPopupWindows.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
            mPopupWindows.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindows.setAnimationStyle(R.style.anim_slide_top);
            mPopupWindows.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            mPopupWindows.setClippingEnabled(true);

            mPopupWindows.setOnDismissListener(this);

            mListHeader = layout.findViewById(R.id.listHeader);
            mListView = (ListView) layout.findViewById(android.R.id.list);

            mEmptyText = (TextView) layout.findViewById(android.R.id.empty);
            if (mEmptyText != null) {
                mEmptyText.setText("无历史记录");
            }
            if (mListView != null) {
                mListView.setEmptyView(mEmptyText);
                mListView.setOnItemClickListener(this);
            }

            initData();
        }

        protected void initData() {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s)) {
                mEmptyText.setText("无搜索结果");
                mClearInput.setVisibility(View.VISIBLE);
                mListHeader.setVisibility(View.GONE);
                load(s.toString());
            }
            else {
                mClearInput.setVisibility(View.GONE);
                mListHeader.setVisibility(View.VISIBLE);
                mEmptyText.setText("无历史记录");
                loadSearchHistory();
            }
        }

        @Override
        public void onClick(View v) {
            if (v == mClearInput) {
                clearInput();
            }
            else if (v == mCancelView) {
                cancelSearch();
            }
            else if (v == mClearHistory) {
                clearSearchHistory();
            }
        }

        @Override
        public void onDismiss() {
            //cancelSearch();
            //mSearchLayout.setHintText(mEditInput.getText().toString());
        }

        protected void clearInput() {
            mEditInput.setText(null);
        }

        public void cancelSearch() {
            clearInput();
            KeyboardUtils.hideSoftInput(mEditInput);
            mPopupWindows.dismiss();
            if (callback != null) {
                callback.onCancel(this);
            }
        }

        public void showWindow() {
            View anchor = mAnchor == null ? mSearchBar : mAnchor;
            int h = mPopupWindows.getMaxAvailableHeight(anchor);
            int sh = AppUtils.getScreenHeight(context);
            mPopupWindows.setHeight(h);
            mPopupWindows.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, 0);
            if (!TextUtils.isEmpty(mHintText)) {
                mEditInput.setHint(mHintText);
            }
            else {
                mEditInput.setHint(null);
            }
            KeyboardUtils.showSoftInput(mEditInput);
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN && !TextUtils.isEmpty(
                mEditInput.getText())) {
                search(null, mEditInput.getText().toString());
            }
            return false;
        }

        protected void load(String keyword) {

        }

        public void search(T info, String keyword) {
            cancelSearch();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }

        public void setAnchor(View parent) {
            this.mAnchor = parent;
        }

        protected class SearchAdapter<T> extends AfBaseAdapter<T> {

            @Override
            public int getLayout() {
                return R.layout.common_pref_item;
            }

            @Override
            public void onUpdateView(View convertView, int position) {
                TextView tv = null;
                if (tv instanceof TextView) {
                    tv = (TextView) convertView;
                }
                else {
                    tv = (TextView) convertView.findViewById(android.R.id.title);
                }
                String text = getDisplayName(getItem(position));
                String kw = mEditInput.getText().toString();
                SpannableString ss = new SpannableString(text);
                Matcher m = Pattern.compile(kw).matcher(text);
                while (m.find()) {
                    ss.setSpan(new ForegroundColorSpan(getHighlightColor(convertView.getContext())), m.start(), m.end(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tv.setText(ss);
            }

            protected String getDisplayName(T info) {
                return info.toString();
            }

            protected int getHighlightColor(Context context) {
                return AppUtils.getColor(context, R.color.colorAccent);
            }
        }

        Callback callback;

        public void setCallback(Callback callback) {
            this.callback = callback;
        }

        public interface Callback {
            void onCancel(SearchPopupWindow popupWindow);
        }

        /**
         * 加载搜索历史
         */
        protected List<T> loadSearchHistory() {
            List<T> list = (List<T>) FileUtils.readObject(FileUtils.getInternal(context), HISTORY_FILE);
            mAdapter.clear();
            mAdapter.addAll(list);
            mAdapter.notifyDataSetChanged();
            return list;
        }

        /**
         * 将数据存储进入共享参数
         */
        protected void addSearchHistory(T info) {
            List<T> list = (List<T>) FileUtils.readObject(FileUtils.getInternal(context), HISTORY_FILE);
            if (list == null) {
                list = new ArrayList<>(1);
            }
            if (!list.contains(info)) {
                list.add(0, info);
            }
            saveSearchHistory(list);
        }

        protected void saveSearchHistory(List<T> list) {
            FileUtils.writeObject(FileUtils.getInternal(context), HISTORY_FILE, list);
        }

        /**
         * 清空搜索历史
         */
        protected void clearSearchHistory() {
            saveSearchHistory(null);
            loadSearchHistory();
        }
    }
}
