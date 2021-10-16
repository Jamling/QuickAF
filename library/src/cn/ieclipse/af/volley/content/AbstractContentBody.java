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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @since 4.0
 */
public abstract class AbstractContentBody {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TRANSFER_ENC = "Content-Transfer-Encoding";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    public static final String ENC_8BIT = "8bit";
    public static final String ENC_BINARY = "binary";
    private Charset charset = StandardCharsets.UTF_8;
    private final String contentType;

    /**
     * @since 4.3
     */
    public AbstractContentBody(final String contentType, final Charset charset) {
        this.contentType = contentType;
        if (charset != null) {
            this.charset = charset;
        }
    }

    public AbstractContentBody(final String contentType) {
        this.contentType = contentType;
    }

    /**
     * @since 4.3
     */
    public String getContentType() {
        return this.contentType;
    }

    public String getMimeType() {
        return this.contentType;
    }

    public String getMediaType() {
        final String mimeType = this.contentType;
        final int i = mimeType.indexOf('/');
        if (i != -1) {
            return mimeType.substring(0, i);
        } else {
            return mimeType;
        }
    }

    public String getSubType() {
        final String mimeType = this.contentType;
        final int i = mimeType.indexOf('/');
        if (i != -1) {
            return mimeType.substring(i + 1);
        } else {
            return null;
        }
    }

    public String getCharset() {
        // final Charset charset = this.contentType.getCharset();
        return charset != null ? charset.name() : null;
    }

    public String getFilename() {
        return null;
    }

    public void writeTo(final OutputStream out) throws IOException {

    }

    public long getContentLength() {
        return 0;
    }
}