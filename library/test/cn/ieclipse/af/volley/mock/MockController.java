package cn.ieclipse.af.volley.mock;

import java.util.List;

import cn.ieclipse.af.volley.IUrl;

/**
 * Description
 *
 * @author Jamling
 */

public class MockController extends AppController<MockController.MockListener> {
    public MockController(MockListener l) {
        super(l);
    }

    public void mockRequestObject(Object input, String mockResponseJson) {
        MockTask task = new MockTask();
        task.setMockJson(mockResponseJson);
        task.load(input, false);
    }

    public void mockRequestList(Object input, String mockResponseJson) {
        MockListTask task = new MockListTask();
        task.setMockJson(mockResponseJson);
        task.load(input, false);
    }

    public interface MockListener {

        void onLoadSuccess(BaseInfo out);

        void onLoadSuccess(List<BaseInfo> out);
    }

    private class MockTask extends AppBaseTask<Object, BaseInfo> {

        @Override
        public IUrl getUrl() {
            return new Url("");
        }

        @Override
        public void onSuccess(BaseInfo out, boolean fromCache) {
            mListener.onLoadSuccess(out);
        }
    }

    public class MockListTask extends AppBaseTask<Object, List<BaseInfo>> {

        @Override
        public IUrl getUrl() {
            return new Url("");
        }

        @Override
        public void onSuccess(List<BaseInfo> out, boolean fromCache) {
            mListener.onLoadSuccess(out);
        }
    }
}
