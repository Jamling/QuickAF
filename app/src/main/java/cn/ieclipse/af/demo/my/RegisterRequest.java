/*
 * Copyright (C) 2015-2016 QuickAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.af.demo.my;

import cn.ieclipse.af.demo.common.api.BaseRequest;

/**
 * 类/接口描述
 * 
 * @author Jamling
 *       
 */
public class RegisterRequest extends BaseRequest {
    public String name;
    public String phone;
    public int sex;
    public String code;
    public String password;
    
}
