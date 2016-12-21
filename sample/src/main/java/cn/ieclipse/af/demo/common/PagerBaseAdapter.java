/*
 * Copyright (C) 2015-2016 HongTu
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
package cn.ieclipse.af.demo.common;

import cn.ieclipse.af.adapter.AfBaseAdapter;

/**
 * Description
 *
 * @author Jamling
 */
public abstract class PagerBaseAdapter<T> extends AfBaseAdapter<T> {
    private int page = 1;
    private int pageSize = 10;
    private int total;

    public void calcCurrentPage() {
        int total = getCount();
        int p = total / pageSize;
        if (total % pageSize >= 0) {
            this.page = p + 1;
        }
        else {
            this.page = p;
        }

        if (this.page <= 0) {
            this.page = 1;
        }
    }

    public int getPage() {
        return page;
    }

    public void setPageSize(int i) {
        if (i > 0) {
            this.pageSize = i;
        }
    }

    public void setTotal(int t) {
        if (t > 0) {
            this.total = t;
        }
    }
}
