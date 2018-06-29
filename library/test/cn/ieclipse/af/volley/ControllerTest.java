package cn.ieclipse.af.volley;

import com.android.volley.toolbox.HurlStack;
import com.google.gson.Gson;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static cn.ieclipse.af.util.EncodeUtils.decodeUnicode;
import cn.ieclipse.af.volley.mock.BaseInfo;
import cn.ieclipse.af.volley.mock.BaseResponse;
import cn.ieclipse.af.volley.mock.MockContext;
import cn.ieclipse.af.volley.mock.MockController;

/**
 * Description
 *
 * @author Jamling
 */

public class ControllerTest {
    @Test
    public void testLoad() {
        VolleyManager.init(new MockContext(), new VolleyConfig.Builder().setBaseResponseClass(BaseResponse.class)
            .setHttpStack(new HurlStack()).build());
        MockController.MockListener listener = new MockController.MockListener() {

            @Override
            public void onLoadSuccess(BaseInfo out) {
                System.out.println(out);
                Assert.assertTrue(out != null);
                Assert.assertEquals(out.id, 1);
            }

            @Override
            public void onLoadSuccess(List<BaseInfo> out) {
                System.out.println(out);
                Assert.assertEquals(out.size(), 2);
                Assert.assertEquals(out.get(1).id, 1);
            }
        };
        MockController controller = new MockController(listener);
        BaseInfo info = new BaseInfo();
        info.mock();
        BaseResponse<BaseInfo> response = new BaseResponse<>();
        response.data = info;

        String json = new Gson().toJson(response);
        controller.mockRequestObject(null, json);

        List<BaseInfo> list = new ArrayList<>();
        list.add(info);
        list.add(info);

        BaseResponse<List<BaseInfo>> response2 = new BaseResponse<>();
        response2.data = list;
        json = new Gson().toJson(response2);
        controller.mockRequestList(null, json);
    }

    @Test
    public void testTypeConvert() {
        VolleyManager.init(new MockContext(), new VolleyConfig.Builder().setBaseResponseClass(BaseResponse.class)
            .setHttpStack(new HurlStack()).build());
        MockController.MockListener listener = new MockController.MockListener() {

            @Override
            public void onLoadSuccess(BaseInfo out) {
                System.out.println(out);
                Assert.assertTrue(out != null);
                Assert.assertEquals(out.id, 1);
                Assert.assertEquals(out.name, "111");
            }

            @Override
            public void onLoadSuccess(List<BaseInfo> out) {
                System.out.println(out);
                Assert.assertEquals(out.size(), 1);
                Assert.assertEquals(out.get(0).id, 1);
                Assert.assertEquals(out.get(0).name, "111");
            }
        };
        MockController controller = new MockController(listener);

        // String id, int name
        String json = "{\"code\":0,\"data\":{\"id\":\"1\",\"name\":111,\"weight\":2,\"age\":0}}";
        controller.mockRequestObject(null, json);

        json = "{\"code\":0,\"data\":[{\"id\":\"1\",\"name\":111,\"weight\":2,\"age\":0}]}";
        controller.mockRequestList(null, json);
    }

    @Test
    public void testJson() throws Exception {
        String json = "{\"status\":0,\"message\":\"\\u6210\\u529f\",\"data\":null}\\u6210";
        System.out.println(json);
        String decoded = decodeUnicode(json);
        Assert.assertEquals(decoded, "{\"status\":0,\"message\":\"成功\",\"data\":null}成");
    }
}
