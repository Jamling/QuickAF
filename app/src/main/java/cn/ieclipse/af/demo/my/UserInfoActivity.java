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
package cn.ieclipse.af.demo.my;

import android.content.Context;
import android.content.Intent;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;

/**
 * Description
 *
 * @author Jamling
 */
public class UserInfoActivity extends BaseActivity {
    @Override
    protected int getContentLayout() {
        return R.layout.my_activity_user_info;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setTitle("个人信息");
    }

    public static Intent create(Context context){
        Intent intent = new Intent(context, UserInfoActivity.class);
        return intent;
    }
}
