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

import java.io.File;
import java.util.Arrays;
import java.util.List;

import cn.ieclipse.af.demo.common.api.AppUploadController;
import cn.ieclipse.af.demo.common.api.URLConst;
import cn.ieclipse.af.util.FileUtils;
import cn.ieclipse.af.volley.IUrl;
import cn.ieclipse.af.volley.RestError;
import cn.ieclipse.af.volley.UploadRequest;

/**
 * Sample upload controller
 * 
 * @author Jamling
 *       
 */
public class MyUploadController
        extends AppUploadController<MyUploadController.UploadListener> {

    public MyUploadController(UploadListener l) {
        super(l);
    }

    public interface UploadListener {
        void onUploadFailure(File file, RestError error);

        void onUploadSuccess(File file, UploadInfo out);

        void onUploadSuccess(File[] files, List<UploadInfo> out);

        void onProgress(long transferred, long total, int progress);
    }
    
    public void upload(String url, UploadRequest.UploadOption option, File file) {
        UploadTask task = new UploadTask();
        task.setOption(option);
        task.url = url;
        task.load(file, UploadInfo.class, false);
    }
    
    public void upload(String url, UploadRequest.UploadOption option, File... file) {
        MultiUploadTask task = new MultiUploadTask(file);
        task.url = url;
        task.load2List(file[0], UploadInfo.class, false);
    }
    
    private class UploadTask extends AppUploadTask<UploadInfo> {
        String url;
        UploadRequest.UploadOption option;

        public void setOption(UploadRequest.UploadOption option) {
            this.option = option;
        }

        @Override
        public void onProgress(long transferred, long total, int progress) {
            mListener.onProgress(transferred, total, progress);
        }
        
        @Override
        public void onUploadFailure(File file, RestError error) {
            mListener.onUploadFailure(file, error);
        }
        
        @Override
        public void onUploadSuccess(File file, UploadInfo out) {
            mListener.onUploadSuccess(file, out);
        }
        
        @Override
        public IUrl getUrl() {
            // return new URLConst.AbsoluteUrl(
            // "http://apicn.faceplusplus.com/v2/detection/detect");
            return new URLConst.AbsoluteUrl(url).post();
        }
        
        @Override
        public void upload(UploadRequest request) {
            request.setUploadOption(option);
            String ext = FileUtils.getExtension(input.getName()).toLowerCase();
            if (Arrays.asList("png", "jpg", "jpeg").contains(ext)) {
                request.addBitmapBody("file", input);
            }else {
                request.addBody("file", input, null);
            }
            request.addHeader("api_key", "3e5fcdfb3aed7586fb8cba5f4cd9cf86");
            request.addHeader("api_secret", "q17PVbJtAP-HMgOY0M9k6U1wV4PcezsZ");
        }
    }
    
    private class MultiUploadTask extends AppMultiUploadTask<List<UploadInfo>> {
        String url;
        public MultiUploadTask(File... files) {
            super(files);
        }
        
        @Override
        public void onProgress(long transferred, long total, int progress) {
            mListener.onProgress(transferred, total, progress);
        }
        
        @Override
        public void onUploadFailure(File file, RestError error) {
            mListener.onUploadFailure(file, error);
        }
        
        @Override
        public void onUploadSuccess(File file, List<UploadInfo> out) {
            mListener.onUploadSuccess(files, out);
        }
        
        @Override
        public IUrl getUrl() {
            // return new URLConst.AbsoluteUrl(
            // "http://apicn.faceplusplus.com/v2/detection/detect");
            return new URLConst.AbsoluteUrl(url).post();
        }
    }
}
