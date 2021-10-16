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

import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.BaseHttpStack;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015年11月10日
 */
public final class VolleyConfig {
    private final BaseHttpStack mHttpStack;
    private final int mMaxDiskCacheBytes;
    private final Class<? extends IBaseResponse> mBaseResponseClass;
    private final RetryPolicy mRetryPolicy;
    private final boolean mDebug;

    private VolleyConfig(Builder builder) {
        mHttpStack = builder.mHttpStack;
        mMaxDiskCacheBytes = builder.mMaxDiskCacheBytes;
        mBaseResponseClass = builder.mBaseResponseClass;
        mRetryPolicy = builder.mRetryPolicy;
        mDebug = builder.mDebug;
    }

    public BaseHttpStack getHttpStack() {
        return mHttpStack;
    }

    public int getMaxDiskCacheBytes() {
        return mMaxDiskCacheBytes;
    }

    public Class<? extends IBaseResponse> getBaseResponseClass() {
        return mBaseResponseClass;
    }

    public RetryPolicy getRetryPolicy() {
        return mRetryPolicy;
    }

    public boolean isDebug() {
        return mDebug;
    }

    public static final class Builder {
        private BaseHttpStack mHttpStack;
        private int mMaxDiskCacheBytes;
        private Class<? extends IBaseResponse> mBaseResponseClass;
        private RetryPolicy mRetryPolicy;
        private boolean mDebug;

        public Builder setHttpStack(BaseHttpStack httpStack) {
            this.mHttpStack = httpStack;
            return this;
        }

        public Builder setMaxDiskCacheBytes(int maxDiskCacheBytes) {
            this.mMaxDiskCacheBytes = maxDiskCacheBytes;
            return this;
        }

        public Builder setBaseResponseClass(Class<? extends IBaseResponse> baseResponseClass) {
            this.mBaseResponseClass = baseResponseClass;
            return this;
        }

        public Builder setRetryPolicy(RetryPolicy retryPolicy) {
            this.mRetryPolicy = retryPolicy;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.mDebug = debug;
            return this;
        }

        public VolleyConfig build() {
            return new VolleyConfig(this);
        }
    }
}
