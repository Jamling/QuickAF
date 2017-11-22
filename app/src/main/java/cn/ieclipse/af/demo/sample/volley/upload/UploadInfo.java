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

package cn.ieclipse.af.demo.sample.volley.upload;

import java.io.Serializable;

public class UploadInfo implements Serializable {
    public String name;
    public String type;
    public int size;
    public String key;
    public String ext;
    public String md5;
    public String file_path;

    @Override
    public String toString() {
        return "UploadInfo@[url=" + file_path + "]";
    }
}
