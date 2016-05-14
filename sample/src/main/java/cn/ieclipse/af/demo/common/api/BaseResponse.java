package cn.ieclipse.af.demo.common.api;

import com.google.gson.Gson;

import cn.ieclipse.af.volley.IBaseResponse;

public class BaseResponse implements java.io.Serializable, IBaseResponse {
    private static final long serialVersionUID = -3440061414071692254L;

    /**
     * 状态码
     */
    private int status;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private Object data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        if (data instanceof String){
            return (String) data;
        }
        return new Gson().toJson(data);
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("status=%s, message=%s, data=%s", getStatus(), getMessage(), getData());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseResponse && obj != null) {
            BaseResponse another = (BaseResponse) obj;
            if (getData() == null) {
                if (another.getData() == null) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return getData().equals(another.getData());
            }
        }
        return false;
    }
}
