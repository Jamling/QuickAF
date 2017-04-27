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
    public FileInfo file;
    
    public static class FileInfo {
        /**
         * name : 1445305026535.jpg type : image/jpeg size : 28724 key : file
         * ext : jpg md5 : dc829c394060dfd78c24b11247ea777f sha1 :
         * bb523a90e5caeab1198197320eda034c98cc300e savename : 5695b070338b7.jpg
         * savepath : file/2016-01-13/ file_path :
         * /uploads/file/2016-01-13/5695b070338b7.jpg
         */
        
        public String name;
        public String type;
        public int size;
        public String key;
        public String ext;
        public String md5;
        public String sha1;
        public String savename;
        public String savepath;
        public String file_path;
        
        @Override
        public String toString() {
            return "FileInfo@[" + file_path + "]";
        }
    }
    
    @Override
    public String toString() {
        return file.toString();
    }
}
