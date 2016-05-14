package cn.ieclipse.af.demo.common.api;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

public class LogicError extends VolleyError {
    private static final long serialVersionUID = 7739186324576518504L;
    private String status;
    private String desc;
    
    public LogicError(NetworkResponse response, String status, String message) {
        super(response);
        this.desc = message;
        this.status = status;
    }
    
    /**
     * 获取服务端返回的status
     * 
     * @return
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * 获取服务端返回的message
     * 
     * @return
     */
    public String getDesc() {
        return desc == null ? "null" : desc;
    }
}
