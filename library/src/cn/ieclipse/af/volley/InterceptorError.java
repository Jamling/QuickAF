/*
 * Copyright 2014-2015 ieclipse.cn.
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
package cn.ieclipse.af.volley;

/**
 * There is identifier exception class. You can throw this exception to interrupt the {@linkplain
 * cn.ieclipse.af.volley.Controller.RequestObjectTask#onResponse(IBaseResponse) RequestObjectTask.onResponse}
 *
 * @author Jamling
 */
public class InterceptorError extends Exception {

}
