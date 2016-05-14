/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package cn.ieclipse.af.volley.content;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Binary body part backed by a file.
 *
 * #@see org.apache.http.entity.mime.MultipartEntityBuilder
 *      
 * @since 4.0
 */
public class BitmapBody extends FileBody {
    private long limitThresholdSize;
    private long scaleThresholdSize;
    private boolean compressed;
    private ByteArrayOutputStream bos;
    
    public BitmapBody(final File file) {
        this(file, "application/octet-stream",
                file != null ? file.getName() : null);
    }
    
    /**
     * @since 4.3
     */
    public BitmapBody(final File file, final String contentType,
            final String filename) {
        super(file, contentType, filename);
    }
    
    /**
     * @since 4.3
     */
    public BitmapBody(final File file, final String contentType) {
        this(file, contentType, file.getName());
    }
    
    public void writeTo(final OutputStream out) throws IOException {
        if (!compressed) {
            bos = compress(getFile(), limitThresholdSize);
            compressed = true;
        }
        if (bos != null) {
            out.write(bos.toByteArray());
        }
        else {
            super.writeTo(out);
        }
    }
    
    @Override
    public long getContentLength() {
        if (!compressed) {
            bos = compress(getFile(), limitThresholdSize);
            compressed = true;
        }
        if (bos != null) {
            return bos.size();
        }
        return super.getContentLength();
    }
    
    public void setLimitThresholdSize(long limitThresholdSize) {
        this.limitThresholdSize = limitThresholdSize;
    }
    
    protected static ByteArrayOutputStream compress(File src, long maxLength) {
        long length = src.length();
        if (length > maxLength) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(src.getAbsolutePath(),
                    options);
            // log(String.format("compressed size: %dx%d sample size : %d",
            // options.outWidth, options.outHeight, options.inSampleSize));
            
            int quality = 100;
            if (length > maxLength * 4) {
                quality = 50;
            }
            ByteArrayOutputStream bos = compress(bitmap, quality, maxLength);
            bitmap.recycle();
            // log(String.format("picture (%s) length is %s exceed the limit
            // (%s)",
            // src.getAbsolutePath(), length, maxLength));
            //
            // options.inJustDecodeBounds = true;
            // BitmapFactory.decodeFile(src.getAbsolutePath(), options);
            // int height = options.outHeight;
            // int width = options.outWidth;
            //
            // // log(String.format("picture origin size is %sx%s", width,
            // // height));
            // double scale = Math.sqrt((float) length / maxLength);
            // options.outHeight = (int) (height / scale);
            // options.outWidth = (int) (width / scale);
            // options.inSampleSize = (int) (scale + 0.5);
            // options.inJustDecodeBounds = false;
            return bos;
        }
        return null;
    }
    
    protected static ByteArrayOutputStream compress(Bitmap bitmap, int quality,
            long maxLength) {
        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
        while (fos.size() > maxLength && quality > 20) {
            fos.reset();
            quality = quality - 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
        }
        return fos;
    }
}