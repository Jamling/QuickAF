package cn.ieclipse.af.demo.my;

import cn.ieclipse.af.demo.common.api.AppController;
import cn.ieclipse.af.demo.common.api.BaseRequest;
import cn.ieclipse.af.demo.common.api.BaseResponse;
import cn.ieclipse.af.demo.common.api.LogicError;
import cn.ieclipse.af.demo.common.api.URLConst;
import cn.ieclipse.af.volley.RestError;

public class LoginController extends AppController<LoginController.LoginListener> {
    
    public LoginController(LoginListener l) {
        super(l);
    }

    public void login(BaseRequest request) {
        LoginTask task = new LoginTask();
        task.load(request, LoginResponse.class, false);
    }

    public void getCode(BaseRequest request) {
        CodeTask task = new CodeTask();
        task.load(request, BaseResponse.class, false);
    }
    
    /**
     * 登录回调
     */
    public interface LoginListener {
        void onLoginSuccess(LoginResponse out);

        void onLoginFailure(RestError error);

        void onLoginCodeSuccess(BaseResponse ou);

        void onLoginCodeFail(RestError error);
    }
    
    private class LoginTask extends AppBaseTask<BaseRequest, LoginResponse> {

        @Override
        public void onSuccess(LoginResponse out, boolean fromCache) {
            mListener.onLoginSuccess(out);
        }
        
        @Override
        public URLConst.Url getUrl() {
            if (input instanceof LoginRequest) {
                LoginRequest req = (LoginRequest) input;
                if ("loginpwd".equals(req.type)) {
                    return URLConst.User.LOGIN_WITH_PWD;
                }
                else if ("logincode".equals(req.type)) {
                    return URLConst.User.LOGIN_WITH_CODE;
                }
            }
            return null;
        }
        
        @Override
        public void onError(RestError error) {
            mListener.onLoginFailure(error);
        }
        
        /**
         * 可选的，像注册，登录这种，需要给出接口调用业务逻辑错误的原因，比如，用户不存在等
         */
        @Override
        public void onLogicError(LogicError error) {
            mListener.onLoginFailure(new RestError(error));
        }
    }

    private class CodeTask extends AppBaseTask<BaseRequest, BaseResponse> {

        @Override
        public void onSuccess(BaseResponse out, boolean fromCache) {
            mListener.onLoginCodeSuccess(out);
        }

        @Override
        public URLConst.Url getUrl() {
            return URLConst.User.LOGIN_CODE;
        }

        @Override
        public void onError(RestError error) {
            mListener.onLoginCodeFail(error);
        }

        @Override
        public void onLogicError(LogicError error) {
            mListener.onLoginCodeFail(new RestError(error));
        }
    }
}
