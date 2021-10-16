package cn.ieclipse.af.common;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

import org.xml.sax.XMLReader;

public class HtmlTagHandler implements Html.TagHandler {
    private final WeakReference<Context> mContext;

    protected Map<String, String> attributes = new TreeMap<>();

    public HtmlTagHandler(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    /**
     * @return null if context destroyed
     */
    public Context getContext() {
        if (mContext != null) {
            return mContext.get();
        }
        return null;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (matchTag(tag)) {
            if (opening) {
                parseAt(tag, output, xmlReader);
                startTag(tag, output, xmlReader);
            } else {
                endTag(tag, output, xmlReader);
            }
        }
    }

    protected boolean matchTag(String tag) {
        return false;
    }

    protected void startTag(String tag, Editable output, XMLReader xmlReader) {
    }

    protected void endTag(String tag, Editable output, XMLReader xmlReader) {
    }

    private void parseAt(String tag, Editable output, XMLReader xmlReader) {
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
                String key = data[i * 5 + 1];
                String val = data[i * 5 + 4];
                attributes.put(key, val);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static class SizeTagHandler extends HtmlTagHandler {

        private int start;
        private int end;

        public SizeTagHandler(Context context) {
            super(context);
        }

        @Override
        protected boolean matchTag(String tag) {
            return "size".equals(tag);
        }

        @Override
        protected void startTag(String tag, Editable output, XMLReader xmlReader) {
            start = output.length();
        }

        @Override
        protected void endTag(String tag, Editable output, XMLReader xmlReader) {
            end = output.length();
            String size = attributes.get("size");
            String dp = attributes.get("dp");
            if (!TextUtils.isEmpty(size)) {
                boolean isDp = false;
                if (!TextUtils.isEmpty(dp)) {
                    if ("true".equalsIgnoreCase(dp)) {
                        isDp = true;
                    }
                }
                output.setSpan(new AbsoluteSizeSpan(Integer.parseInt(size), isDp), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                String color = attributes.get("color");
                if (!TextUtils.isEmpty(color)) {
                    int c = Integer.parseInt(color);

                }
            }
        }
    }
}
