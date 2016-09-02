package cn.ieclipse.af.demo.sample.volley;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.volley.VolleyManager;


/**
 * 类/接口描述
 *
 * @author wangjian
 * @date 2016/9/2.
 */
public class AdapterDemoActivity extends BaseActivity {

    private static String json = "{\n" + "\"uid\":\"280\",\n"
        + "    \"avater\":\"http://11.taoxiansheng.com/Uploads/Download/2016-08-30/57c4f81e0add1.jpeg\",\n"
        + "    \"nickname\":\"威威\",\n" + "    \"Ip\":null,\n" + "    \"area\":\"\",\n" + "    \"join\":1,\n"
        + "    \"time\":\"2016-08-31 10:39:36 064\",\n" + "    \"url\":null\n" + "}";

    private Button btn1;
    private TextView tvJson;
    private TextView tvParser;

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_volley_parser;
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        btn1 = (Button) findViewById(R.id.btn1);
        tvJson = (TextView) findViewById(R.id.tv_json_text);
        tvParser = (TextView) findViewById(R.id.tv_json);

        setOnClickListener(btn1);
    }
    
    @Override
    protected void initData() {
        super.initData();
        tvJson.setText(json);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == btn1) {
            parseJson();
        }
    }

    private void parseJson() {
        User user = VolleyManager.getInstance().getPaser().fromJson(json, User.class);
        tvParser.setText(user.toString());
    }

    static class User {
        private String uid;
        private String avater;
        private String nickname;
        // 自定义转换
        private String Ip;// null-->""
        private String area;
        private int join;//1.0--> 1
        private String time;
        private List<String> url;//null --> new List();

        @Override
        public String toString() {
            return "uid=" + uid + "\n" + "avater=" + avater + "\nnickname=" + nickname + "\nIp=" + Ip + "\narea=" + area
                + "\njoin=" + join + "\ntime=" + time + "\nurl.size=" + url.size();
        }
    }
}