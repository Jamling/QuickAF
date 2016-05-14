package cn.ieclipse.af.demo.my;

import cn.ieclipse.af.demo.common.api.AppController;
import cn.ieclipse.af.demo.common.api.BaseRequest;
import cn.ieclipse.af.demo.common.api.BaseResponse;
import cn.ieclipse.af.demo.common.api.URLConst;
import cn.ieclipse.af.volley.RestError;

public class RegisterController extends AppController<RegisterController.RegisterListener> {

    public RegisterController(RegisterListener l) {
        super(l);
    }

    public void getCode(BaseRequest request) {
        CodeTask task = new CodeTask();
        task.load(request, BaseResponse.class, false);
    }
    
    public void register(RegisterRequest code) {
        RegisterTask task = new RegisterTask();
        task.load(code, RegisterResponse.class, false);
    }

    public void login(LoginRequest req){
        LoginTask task = new LoginTask();
        task.load(req, LoginResponse.class, false);
    }
    
    /**
     * 回调
     */
    public interface RegisterListener {

        void onCodeSuccess(BaseResponse out);

        void onRegisterSuccess(RegisterResponse out);

        void onRegisterFail(RestError error);

        void onCodeFail(RestError error);

        void onLoginSuccess(LoginResponse out);

        void onLoginFail(RestError error);
    }
    
    private class CodeTask extends AppBaseTask<BaseRequest, BaseResponse> {
        
        @Override
        public void onSuccess(BaseResponse out, boolean fromCache) {
            mListener.onCodeSuccess(out);
        }
        
        @Override
        public URLConst.Url getUrl() {
            return URLConst.User.REG_CODE;
        }
        
        @Override
        public void onError(RestError error) {
            mListener.onCodeFail(error);
        }
    }
    
    private class RegisterTask extends AppBaseTask<RegisterRequest, RegisterResponse> {
        
        @Override
        public void onSuccess(RegisterResponse out, boolean fromCache) {
            mListener.onRegisterSuccess(out);
        }
        
        @Override
        public URLConst.Url getUrl() {
            return URLConst.User.REG;
        }
        
        @Override
        public void onError(RestError error) {
            mListener.onRegisterFail(error);
        }
    }

    private class LoginTask extends AppBaseTask<LoginRequest, LoginResponse> {

        @Override
        public void onSuccess(LoginResponse out, boolean fromCache) {
            mListener.onLoginSuccess(out);
        }

        @Override
        public URLConst.Url getUrl() {
            return URLConst.User.LOGIN_WITH_PWD;
        }

        @Override
        public void onError(RestError error) {
            mListener.onLoginFail(error);
        }
    }
}
