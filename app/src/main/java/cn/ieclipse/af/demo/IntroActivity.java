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
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;

import androidx.viewpager.widget.ViewPager;

import java.util.Arrays;
import java.util.List;

import cn.ieclipse.af.demo.common.ImagePagerAdapter;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.demo.common.view.IntroViewPager;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.view.AutoPlayView;

/**
 * Description
 *
 * @author Jamling
 */
public class IntroActivity extends BaseActivity {

    private AutoPlayView mAutoPlay;
    private IntroAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.common_activity_intro;
    }

    @Override
    protected void initWindowFeature() {
        super.initWindowFeature();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setShowTitleBar(false);
    }

    @Override
    protected int getStatusBarColor() {
        return AppUtils.getColor(this, android.R.color.transparent);
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mAutoPlay = (AutoPlayView) view.findViewById(R.id.auto_play);
        mAutoPlay.setAutoStart(false);
        mAutoPlay.setLoop(false);
        mAdapter = new IntroAdapter();
        mAdapter.setDataList(getImages());
        if (mAutoPlay.getViewPager() instanceof IntroViewPager) {
            IntroViewPager vp = (IntroViewPager) mAutoPlay.getViewPager();
            vp.setFlingListener(new IntroViewPager.FlingListener() {
                @Override
                public void onFlingNext() {
                    goHome();
                }

                @Override
                public void onFlingPrevious() {

                }
            });
        }
        mAutoPlay.setIndicatorItemSize(AppUtils.dp2px(this, 12));
        mAutoPlay.setIndicatorItemPadding(AppUtils.dp2px(this, 12));
        mAutoPlay.setIndicatorItemColor(AppUtils.getColor(this, R.color.black_cccccc),
            AppUtils.getColor(this, R.color.colorPrimary));
        mAutoPlay.setAdapter(mAdapter);
        mAutoPlay.initIndicatorLayout();
    }

    private void goHome() {
        startActivity(MainActivity.create(this));
        finish();
    }

    public static Intent create(Context context) {
        Intent intent = new Intent(context, IntroActivity.class);
        return intent;
    }

    private List<ImagePagerAdapter.IImage> getImages() {
        return Arrays.asList(new Image(R.mipmap.logo).get(), new Image(R.color.black_alpha_50).get(),
            new Image("mipmap", "logo").get());
    }

    private class Image implements ImagePagerAdapter.IImage {
        public String url;

        public Image(int id) {
            this.url = AppUtils.getRes(getApplicationContext(), id);
        }

        public Image(String url) {
            this.url = url;
        }

        public Image(String type, String name) {
            this.url = AppUtils.getRes(getApplicationContext(), type, name);
        }

        @Override
        public String getUrl() {
            return this.url;
        }

        public ImagePagerAdapter.IImage get() {
            return this;
        }
    }

    private class IntroAdapter extends ImagePagerAdapter implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mAutoPlay.getCount() == mAutoPlay.getCurrent() + 1) {
                goHome();
            }
        }
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);
            }
            else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);
            }
            else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            }
            else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
