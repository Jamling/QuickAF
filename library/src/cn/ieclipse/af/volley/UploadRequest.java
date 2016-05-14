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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import cn.ieclipse.af.volley.content.AbstractContentBody;
import cn.ieclipse.af.volley.content.BitmapBody;
import cn.ieclipse.af.volley.content.ByteArrayBody;
import cn.ieclipse.af.volley.content.FileBody;
import cn.ieclipse.af.volley.content.StringBody;

public class UploadRequest extends GsonRequest {
    /**
     * The pool of ASCII chars to be used for generating a multipart boundary.
     */
    private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            .toCharArray();
    private static final String CR_LF = "\r\n";
    private String boundary = null;
    private static final String TWO_DASHES = "--";
    private final UploadProgressListener progressListener;
    
    private Map<String, List<AbstractContentBody>> params = new TreeMap<>();
    
    public static final long PIC_MAX_LENGTH = 200 << 10; // 200K
    public static final long PIC_LIMIT_LENGTH_OF_QUICK_COMPRESS = 1 << 20;
    private long bitmapMaxLength = PIC_MAX_LENGTH;
    
    public UploadRequest(int method, String url, String body,
            Listener<IBaseResponse> responseListener, ErrorListener listener,
            UploadProgressListener progressListener) {
        super(method, url, body, responseListener, listener);
        this.boundary = generateBoundary();
        this.progressListener = progressListener;
    }
    
    @Override
    public String getBodyContentType() {
        return generateContentType(boundary,
                Charset.forName(getParamsEncoding()));
    }
    
    public List<AbstractContentBody> getParam(String name) {
        List<AbstractContentBody> v = params.get(name);
        if (v == null) {
            v = new ArrayList<>();
            params.put(name, v);
        }
        return v;
    }
    
    private void addBody(String name, AbstractContentBody body) {
        getParam(name).add(body);
    }
    
    public void addParams(String name, String value) {
        StringBody body = new StringBody(value, null, null);
        addBody(name, body);
    }
    
    public void addBody(String name, byte[] data, String mime, String fn) {
        ByteArrayBody body = new ByteArrayBody(data, mime, fn);
        addBody(name, body);
    }
    
    public void addBody(String name, File file, String mime) {
        FileBody body = new FileBody(file, mime);
        addBody(name, body);
    }
    
    public void addBitmapBody(String name, File file) {
        String ext = "*";
        int pos = file.getPath().lastIndexOf('.');
        if (pos > 0){
            ext = file.getPath().substring(pos);
        }
        BitmapBody body = new BitmapBody(file, "image/" + ext);
        body.setLimitThresholdSize(bitmapMaxLength);
        addBody(name, body);
    }
    
    public void setBitmapMaxLength(long bitmapMaxLength) {
        this.bitmapMaxLength = bitmapMaxLength;
    }
    
    private long getTotalSize() {
        long sum = 0L;
        for (String key : params.keySet()) {
            List<AbstractContentBody> v = params.get(key);
            for (AbstractContentBody body : v) {
                sum += body.getContentLength();
            }
        }
        return sum;
    }
    
    @Override
    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        
        CountingOutputStream dos = new CountingOutputStream(bos, getTotalSize(),
                progressListener);
        try {
            for (String key : params.keySet()) {
                List<AbstractContentBody> v = params.get(key);
                for (AbstractContentBody body : v) {
                    dos.writeBytes(TWO_DASHES + boundary + CR_LF);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Content-Disposition: form-data; name=\"");
                    sb.append(key);
                    sb.append("\"");
                    if (body.getFilename() != null) {
                        sb.append("; filename=\"");
                        sb.append(body.getFilename());
                        sb.append("\"");
                    }
                    sb.append(CR_LF);
                    if (body.getContentType() != null) {
                        sb.append("Content-Type: ");
                        sb.append(body.getContentType());
                        sb.append(CR_LF);
                    }
                    sb.append(CR_LF);
                    dos.writeBytes(sb.toString());
                    body.writeTo(dos);
                    dos.writeBytes(CR_LF);
                }
            }
            dos.writeBytes(TWO_DASHES + boundary + TWO_DASHES + CR_LF);
            byte[] ret = bos.toByteArray();
            // File f = new File(Environment.getExternalStorageDirectory(),
            // "aaa_up");
            // if (!f.exists()) {
            // f.createNewFile();
            // }
            // FileOutputStream fos = new FileOutputStream(f);
            // fos.write(ret);
            // fos.flush();
            // fos.close();
            return ret;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static interface MultipartProgressListener {
        void transferred(long transfered, int progress);
    }
    
    private String generateContentType(final String boundary,
            final Charset charset) {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("multipart/form-data; boundary=");
        buffer.append(boundary);
        if (charset != null) {
            buffer.append("; charset=");
            buffer.append(charset.name());
        }
        return buffer.toString();
    }
    
    private String generateBoundary() {
        final StringBuilder buffer = new StringBuilder();
        final Random rand = new Random();
        final int count = rand.nextInt(11) + 30; // a random size from 30 to 40
        for (int i = 0; i < count; i++) {
            buffer.append(
                    MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return buffer.toString();
    }
    
    public static class CountingOutputStream extends FilterOutputStream {
        private final UploadProgressListener progListener;
        private long transferred;
        private long fileLength;
        
        public CountingOutputStream(final OutputStream out, long fileLength,
                final UploadProgressListener listener) {
            super(out);
            this.fileLength = fileLength;
            this.progListener = listener;
            this.transferred = 0;
        }
        
        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            if (progListener != null) {
                this.transferred += len;
                int prog = fileLength <= 0 ? 0
                        : (int) (transferred * 100 / fileLength);
                this.progListener.updateProgress(this.transferred, fileLength, prog);
            }
        }
        
        public void write(int b) throws IOException {
            out.write(b);
            // if (progListener != null) {
            // this.transferred++;
            // int prog = fileLength <= 0 ? 0
            // : (int) (transferred * 100 / fileLength);
            // this.progListener.updateProgress(this.transferred, fileLength,
            // prog);
            // }
        }
        
        public final void writeBytes(String str) throws IOException {
            if (str.length() == 0) {
                return;
            }
            byte[] bytes = new byte[str.length()];
            for (int index = 0; index < str.length(); index++) {
                bytes[index] = (byte) str.charAt(index);
            }
            out.write(bytes, 0, bytes.length);
        }
    }
}