package cn.ieclipse.af.demo.sample.cview;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import org.xml.sax.XMLReader;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import cn.ieclipse.af.util.DialogUtils;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015/8/27.
 */
public class AtTagHandler implements Html.TagHandler {
    public static final String AT_FORMAT = "<input type=\"button\" readonly=\"readonly\" index=\"%2$s\" class=\"receive\" onclick=\"removeObj(this);\" value=\"%1$s\" />";
    private int startIndex = 0;
    private int stopIndex = 0;

    private final WeakReference<Context> mContext;

    public AtTagHandler(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.equalsIgnoreCase("input")) {
            if (opening) {
                startTag(tag, output, xmlReader);
            }
            else {
                endTag(tag, output, xmlReader);
            }
        }
    }

    private void startTag(String tag, Editable output, XMLReader xmlReader) {
        startIndex = output.length();
        parseAt(tag, output, xmlReader);
    }

    private void endTag(String tag, Editable output, XMLReader xmlReader) {
        stopIndex = output.length();
    }

    private void parseAt(String tag, Editable output, XMLReader xmlReader) {
        String value = null;
        String index = null;
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            Field attsField = element.getClass().getDeclaredField("theAtts");
            attsField.setAccessible(true);
            Object atts = attsField.get(element);
            Field dataField = atts.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            String[] data = (String[]) dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField("length");
            lengthField.setAccessible(true);
            int len = (Integer) lengthField.get(atts);
            for (int i = 0; i < len; i++) {
                // 这边的src和type换成你自己的属性名就可以了
                if ("value".equals(data[i * 5 + 1])) {
                    value = data[i * 5 + 4];
                }
                if ("index".equals(data[i * 5 + 1])) {
                    index = data[i * 5 + 4];
                }
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (value != null) {
            ForegroundColorSpan span = new ForegroundColorSpan(0xff90c5e4);
            SpannableString ss = new SpannableString(value);
            ss.setSpan(span, 0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            int uid = 0;
            try {
                uid = Integer.parseInt(index);
            } catch (Exception e) {

            }
            ClickableSpan clickSpan = new AtClickSpan(uid);
            ss.setSpan(clickSpan, 0, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            output.append(ss);
        }
    }

    private class AtClickSpan extends ClickableSpan {
        private final int uid;

        public AtClickSpan(int uid) {
            this.uid = uid;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#5198d4"));//ds.linkColor
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            Context context = null;
            if (mContext != null) {
                context = mContext.get();
            }
            if (context == null) {
                // TODO context = ?
            }
            if (context != null) {
                // TODO goto UserCard
                DialogUtils.showToast(context, "click user " + uid);
            }
        }
    }
}
