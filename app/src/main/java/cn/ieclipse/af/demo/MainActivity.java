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
package cn.ieclipse.af.demo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.ieclipse.af.demo.common.BaseFragmentAdapter;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.demo.common.ui.DevDialogFragment;
import cn.ieclipse.af.demo.common.view.MainBottomTab;
import cn.ieclipse.af.demo.my.CheckUpdateController;
import cn.ieclipse.af.demo.my.MyFragment;
import cn.ieclipse.af.demo.sample.OthersFragment;
import cn.ieclipse.af.demo.sample.appui.AppUIFragment;
import cn.ieclipse.af.demo.sample.volley.VolleyFragment;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.DialogUtils;

/**
 * Description
 *
 * @author Jamling
 */
public class MainActivity extends BaseActivity implements CheckUpdateController.UpdateListener {

    private CheckUpdateController mCheckController = new CheckUpdateController(this);
    private ViewPager mViewPager;
    private MainBottomTab mBottomTab;
    private BaseFragmentAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.base_viewpager;
    }

    @Override
    protected void initWindowFeature() {
        super.initWindowFeature();
        setShowTitleBar(true);
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleLeftView.setVisibility(View.INVISIBLE);
        setOnClickListener(mTitleTextView);
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(2);
        mAdapter = new BaseFragmentAdapter(getFragmentManager());
        mAdapter.setFragments(new AppUIFragment(), new VolleyFragment(), new OthersFragment(), new MyFragment());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(mAdapter.getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setTitle(mAdapter.getPageTitle(0));
        mViewPager.setCurrentItem(0, false);
    }

    @Override
    protected void initBottomView() {
        super.initBottomView();
        mBottomTab = (MainBottomTab) View.inflate(this, R.layout.common_bottom_tab, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomBar.addView(mBottomTab, params);
        mBottomTab.setViewPager(mViewPager);
        mBottomTab.setBadge(0, 1);
    }

    @Override
    protected void initData() {
        super.initData();
        mViewPager.setAdapter(mAdapter);
        // checkUpdate();
    }

    @Override
    public void onClick(View v) {
        if (v == mTitleTextView) {
            DevDialogFragment.detect(getFragmentManager());
        }
        super.onClick(v);
    }

    public static Intent create(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    private void checkUpdate() {
        CheckUpdateController.CheckRequest req = new CheckUpdateController.CheckRequest();
        req.versionName = AppUtils.getAppVersion(this);
        req.versionCode = String.valueOf(AppUtils.getPackageInfo(this).versionCode);
        mCheckController.checkUpdate(req);
    }

    @Override
    public void onCheckSuccess(final CheckUpdateController.CheckResponse info) {
        if (info != null) {
            DialogUtils.showAlert(this, android.R.drawable.ic_dialog_info, "发现新版本",
                TextUtils.isEmpty(info.description) ? "发现新版本，是否升级？" : info.description,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            if (info.force) {
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            }
                            intent.setData(Uri.parse(info.downloadLink));
                            startActivityForResult(Intent.createChooser(intent, null), 0x01);
                        } catch (Exception e) {
                            DialogUtils.showToast(getApplicationContext(), "无法打开浏览器下载");
                        } finally {

                        }
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (info.force) {
                            finish();
                        }
                    }
                });
        }
    }


    private boolean confirmExit;
    @Override
    public void onBackPressed() {
        Drawable d1 = AppCompatResources.getDrawable(this, R.drawable.common_selector_ic_back);
        Drawable d = AppUtils.getDrawable(this, R.drawable.common_selector_ic_back);
        d = AppUtils.getDrawable(this, R.drawable.preference_arrow);
        if (confirmExit){
            finish();
        } else {
            confirmExit = true;
            mBottomTab.postDelayed(new Runnable() {
                @Override
                public void run() {
                    confirmExit = false;
                }
            }, 2000);
            DialogUtils.showToast(this, getString(R.string.common_exit_tip));
        }
    }
}
