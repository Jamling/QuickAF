package cn.ieclipse.af.demo.common.api;

import cn.ieclipse.af.volley.IBaseResponse;
import cn.ieclipse.af.volley.InterceptorError;
import cn.ieclipse.af.volley.RestError;

/**
 * App base controller
 *
 * @author Harry
 */
public abstract class AppController<Listener> extends cn.ieclipse.af.volley.Controller<Listener> {

    public AppController(Listener l) {
        super();
        setListener(l);
    }

    protected abstract class AppBaseTask<Input, Output> extends RequestObjectTask<Input, Output> {

        @Override
        public boolean onInterceptor(IBaseResponse response) throws Exception {
            if (response instanceof BaseResponse) {
                BaseResponse resp = (BaseResponse) response;
                if (!"101".equals(resp.getStatus())) {
                    onLogicError(new LogicError(null, String.valueOf(resp.getStatus()), resp.getMessage()));
                    throw new InterceptorError();
                }
            }
            return false;
        }

        public void onLogicError(LogicError error) {
            if ("105".equals(error.getStatus()) || "104".equals(error.getStatus())) {
                // LoginActivity.go(MyApplication.instance);
                return;
            }
            onError(new RestError(error));
        }
    }
}
