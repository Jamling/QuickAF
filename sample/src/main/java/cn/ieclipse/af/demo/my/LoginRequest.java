/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.demo.my;

import cn.ieclipse.af.demo.common.api.BaseRequest;

/**
 * 类/接口描述
 *
 * @author Jamling
 */
public class LoginRequest extends BaseRequest {
    public String phone;
    public String code;
    public String password;
    public String type;//    类型

    public String username;
    public String pwd;
}
