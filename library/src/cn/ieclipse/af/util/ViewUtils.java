/*
 * Copyright 2014-2015 ieclipse.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.af.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015/10/29.
 */
public final class ViewUtils {
    private ViewUtils() {

    }

    //--> size

    /**
     * Set background drawable to view.
     *
     * @param view view
     * @param d background drawable
     * @see View#setBackground(Drawable)
     */
    //--> for android sdk compatibility
    public static void setBackground(View view, Drawable d) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(d);
        } else {
            view.setBackgroundDrawable(d);
        }
    }

    /**
     * Instead {@link android.widget.ImageView#setImageResource(int)}
     *
     * @param iv ImageView
     * @param resId resourceId
     * @since 3.0.1
     */
    // don't use ImageView#setImageResource
    public static void setImageResource(ImageView iv, int resId) {
        if (iv != null) {
            Drawable d = AppUtils.getDrawable(iv.getContext(), resId);
            iv.setImageDrawable(d);
        }
    }

    /**
     * Set WebView wrap_content under ScrollView.
     * <p class="note">Must called in UI Thread after
     * {@link android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, String)}</p>
     *
     * @param webView WebView
     * @param minHeightDp min height, dp unit
     * @since 3.0.1
     */
    public static void autoHeightWebView(WebView webView, int minHeightDp) {
        int w = View.MeasureSpec.makeMeasureSpec(AppUtils.getScreenWidth(webView.getContext()),
            View.MeasureSpec.EXACTLY);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        if (h <= 0 && minHeightDp > 0) {
            h = AppUtils.dp2px(webView.getContext(), minHeightDp);
        }
        webView.measure(w, h);
    }

    /**
     * Set WebView background color
     *
     * @param webView WebView
     * @param color color
     * @since 3.0.1
     */
    public static void setWebViewBackgroundColor(WebView webView, int color) {
        webView.setBackgroundColor(color);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /**
     * 测量这个view获取宽度和高度.
     *
     * @param view 要测量的view
     */
    public static void measureView(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 设置输入框的光标到末尾
     */
    public static final void setEditTextSelectionToEnd(EditText editText) {
        Editable editable = editText.getEditableText();
        Selection.setSelection(editable, editable.toString().length());
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            // listItem.measure(0, 0);
            ViewUtils.measureView(listItem);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
