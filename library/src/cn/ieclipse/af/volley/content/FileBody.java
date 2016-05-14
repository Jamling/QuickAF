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
 
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
 
/**
 * Binary body part backed by a file.
 *
 * #@see org.apache.http.entity.mime.MultipartEntityBuilder
 *
 * @since 4.0
 */
public class FileBody extends AbstractContentBody {
 
    private final File file;
    private final String filename;
 
 
    public FileBody(final File file) {
        this(file, "application/octet-stream", file != null ? file.getName() : null);
    }
 
    /**
     * @since 4.3
     */
    public FileBody(final File file, final String contentType, final String filename) {
        super(contentType);
        this.file = file;
        this.filename = filename;
    }
 
    /**
     * @since 4.3
     */
    public FileBody(final File file, final String contentType) {
        this(file, contentType, null);
    }
 
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }
 
    public void writeTo(final OutputStream out) throws IOException {
        final InputStream in = new FileInputStream(this.file);
        try {
            final byte[] tmp = new byte[4096];
            int l;
            while ((l = in.read(tmp)) != -1) {
                out.write(tmp, 0, l);
            }
            out.flush();
        } finally {
            in.close();
        }
    }
 
    public String getTransferEncoding() {
        return ENC_BINARY;
    }
 
    public long getContentLength() {
        return this.file.length();
    }
 
    public String getFilename() {
        return filename;
    }
 
    public File getFile() {
        return this.file;
    }
 
}