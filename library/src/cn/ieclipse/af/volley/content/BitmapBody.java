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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cn.ieclipse.af.util.FileUtil;
import cn.ieclipse.af.util.IOUtils;
import cn.ieclipse.af.volley.Controller;
import cn.ieclipse.af.volley.UploadRequest;

/**
 * Binary body part backed by a file.
 * #@see org.apache.http.entity.mime.MultipartEntityBuilder
 *
 * @since 4.0
 */
public class BitmapBody extends FileBody {
    private UploadRequest.UploadOption uploadOption;
    private boolean compressed;
    private FileOutputStream bos;
    //private FileOutputStream fos;
    private File temp;
    private boolean debug = Controller.DEBUG;

    public BitmapBody(final File file) {
        this(file, "application/octet-stream", file != null ? file.getName() : null);
    }

    /**
     * @since 4.3
     */
    public BitmapBody(final File file, final String contentType, final String filename) {
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
            bos = compress(getFile(), uploadOption);
            compressed = true;
        }
        if (bos != null) {
            //out.write(bos.toByteArray());
            writeTo(out, this.temp);
            deleteCache();
        }
        else {
            super.writeTo(out);
        }
    }

    @Override
    public long getContentLength() {
        if (!compressed) {
            bos = compress(getFile(), uploadOption);
            compressed = true;
        }
        if (bos != null) {
            return temp.length();
        }
        return super.getContentLength();
    }

    public void setUploadOption(UploadRequest.UploadOption uploadOption) {
        this.uploadOption = uploadOption;
    }

    protected FileOutputStream createCache() throws IOException {
        deleteCache();
        if (temp == null) {
            try {
                temp = File.createTempFile("upload_temp", ".jpg");
                if (debug) {
                    log("Upload temp file: " + temp.getAbsolutePath());
                }
                return new FileOutputStream(temp);
            } catch (IOException e) {
                throw e;
            }
        }
        return null;
    }

    private void deleteCache() {
        if (temp != null) {
            temp.delete();
            temp = null;
        }
    }

    protected FileOutputStream compress(File src, UploadRequest.UploadOption uploadOption) {
        long length = src.length();
        if (length > uploadOption.bitmapMaxLength) {
            try {
                dumpMemory("start");
                Bitmap bitmap;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = uploadOption.bitmapConfig;

                // get image width and height
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(src.getAbsolutePath(), options);
                final int height = options.outHeight;
                final int width = options.outWidth;
                // need scale?

                if (uploadOption.isScaleEnable(width, height)) {
                    // see BitmapUtil.calculateInSampleSize
                    int inSampleSize = 1;
                    if (height > uploadOption.scaleTargetHeight || width > uploadOption.scaleTargetWidth) {
                        final int heightRatio = Math.round((float) height / (float) uploadOption.scaleTargetHeight);
                        final int widthRatio = Math.round((float) width / (float) uploadOption.scaleTargetWidth);
                        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
                    }
                    options.inSampleSize = inSampleSize;
                    if (debug) {
                        log(String.format(
                            "Upload image scale enabled, image scaled from %sx%s to %sx%s, " + "inSampleSize=%d", width,
                            height, width / options.inSampleSize, height / options.inSampleSize, inSampleSize));
                    }
                }
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(src.getAbsolutePath(), options);
                if (debug) {
                    log(String.format("Upload image size: %dx%d", options.outWidth, options.outHeight));
                }
                dumpMemory("decode bitmap");
                int quality = 100;
                if (uploadOption.quickCompressEnable && length > uploadOption.quickCompressLength) {
                    quality = uploadOption.quickCompressQuality;
                }
                bos = compress(bitmap, quality, uploadOption);
                bitmap.recycle();
                if (debug) {
                    log(String.format("picture (%s) length is %s exceed the limit(%s) compressed to %s",
                        src.getAbsolutePath(), FileUtil.formatFileSize(length),
                        FileUtil.formatFileSize(uploadOption.bitmapMaxLength), FileUtil.formatFileSize(temp.length())));
                }
                return bos;
            } catch (OutOfMemoryError error) {
                throw new RuntimeException(error);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                System.gc();
            }
        }
        return null;
    }

    protected FileOutputStream compress(Bitmap bitmap, int quality,
                                        UploadRequest.UploadOption option) throws IOException {
        FileOutputStream fos = createCache();
        bitmap.compress(option.compressFormat, quality, fos);
        int i = 1;
        dumpMemory(String
            .format("%d times compress to %s (quality=%d)", i++, FileUtil.formatFileSize(this.temp.length()), quality));
        while (temp.length() > option.bitmapMaxLength && quality > option.compressMinQuality) {
            IOUtils.closeStream(fos);
            fos = createCache();
            quality = quality - uploadOption.compressQualityStep;
            bitmap.compress(uploadOption.compressFormat, quality, fos);
            dumpMemory(String
                .format("%d times compress to %s (quality=%d)", i++, FileUtil.formatFileSize(this.temp.length()),
                    quality));
            System.gc();
        }
        return fos;
    }

    protected void dumpMemory(String tip) {
        if (debug) {
            log(tip);
            long xmx = Runtime.getRuntime().maxMemory();
            long xms = Runtime.getRuntime().totalMemory();
            long free = Runtime.getRuntime().freeMemory();
            String msg = String.format("xmx=%s,xms=%s,free=%s", FileUtil.formatFileSize(xmx),
                FileUtil.formatFileSize(xms), FileUtil.formatFileSize(free));
            log(msg);
        }
    }

    protected void log(String msg) {
        Controller.log(msg);
    }
}