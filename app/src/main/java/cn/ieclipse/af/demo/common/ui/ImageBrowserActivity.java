/*
 * Copyright (C) 2015-2017 QuickAF
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
package cn.ieclipse.af.demo.common.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.Serializable;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ImagePagerAdapter;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.RandomUtils;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015/7/22.
 */
public class ImageBrowserActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    public static final String EXTRA_INDEX = "current.index";
    protected ViewPager mViewPager;
    protected ImageAdapter mAdapter;
    protected int mCurrentIndex;
    protected List<? extends ImagePagerAdapter.IImage> mImages;
    
    private View mDescLayout;
    protected TextView mDescTv;
    
    protected int getContentLayout() {
        return R.layout.common_photo_view_pager;
    }
    
    @Override
    protected void initWindowFeature() {
        super.initWindowFeature();
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setWindowBackground(android.R.color.transparent);
        setOverlay(true);
    }
    
    @Override
    protected int getStatusBarColor() {
        // hide status bar
        return AppUtils.getColor(this, android.R.color.transparent);
    }
    
    protected String getImageDesc(int index) {
        return RandomUtils.genGBK(1, 200);
    }
    
    public static Intent go(Context context, List<ImagePagerAdapter.IImage> images, int index) {
        Intent intent = new Intent(context, ImageBrowserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXTRA_INDEX, index);
        intent.putExtra(EXTRA_DATA, (Serializable) images);
        return intent;
    }
    
    @Override
    protected void initIntent(Bundle bundle) {
        super.initIntent(bundle);
        mCurrentIndex = bundle.getInt(EXTRA_INDEX);
        mImages = (List<? extends ImagePagerAdapter.IImage>) bundle.getSerializable(EXTRA_DATA);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_INDEX, mCurrentIndex);
        outState.putSerializable(EXTRA_DATA, (Serializable) mImages);
        super.onSaveInstanceState(outState);
    }
    
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleLeftView.setTextColor(AppUtils.getColor(this, R.color.white));
        mTitleTextView.setTextColor(AppUtils.getColor(this, R.color.white));
        mTitleBar.setBackgroundColor(AppUtils.getColor(this, R.color.black));
    }
    
    protected void initContentView(View view) {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.addOnPageChangeListener(this);
        
        mDescLayout = findViewById(R.id.tv_desc);
        mDescTv = (TextView) findViewById(R.id.tv_desc);
        if (mDescTv != null) {
//            mDescTv.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    mDescLayout.getParent().requestDisallowInterceptTouchEvent(true);
//                    return false;
//                }
//            });
            mDescTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        }
    }
    
    @Override
    protected void initData() {
        mAdapter = new ImageAdapter();
        mAdapter.setImages(mImages);
        mViewPager.setAdapter(mAdapter);
        if (mAdapter.getCount() > 0) {
            mViewPager.setCurrentItem(mCurrentIndex, false);
            onPageSelected(mCurrentIndex);
        }
    }
    
    protected void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
        if (mAdapter.getCount() > 0) {
            mViewPager.setCurrentItem(mCurrentIndex, false);
            onPageSelected(mCurrentIndex);
        }
        
        if (mDescLayout != null) {
            mDescLayout.setVisibility(mAdapter.getCount() > 0 ? View.VISIBLE : View.GONE);
        }
    }
    
    @Override
    public void onPageScrolled(int i, float v, int i1) {
        
    }
    
    @Override
    public void onPageSelected(int i) {
        mCurrentIndex = i;
        if (mAdapter.getCount() > 0) {
            mTitleTextView.setText(String.format("%d/%d", (i + 1), mAdapter.getCount()));
            if (mDescTv != null) {
                mDescTv.setText(getImageDesc(i));
            }
        }
    }
    
    @Override
    public void onPageScrollStateChanged(int i) {
        
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO here has an exception in Activity.dispatchTouchEvent();
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }
    
    public static class ImageAdapter extends ImagePagerAdapter {
        
        @Override
        public int getLayout() {
            return R.layout.common_photo_view;
        }
        
        @Override
        public void onUpdateView(View convertView, int position) {
            final PhotoView photoView = (PhotoView) convertView.findViewById(R.id.photo_view);
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(android.R.id.progress);
            String url = getItem(position).getUrl();
            ImageLoader.getInstance().displayImage(url, photoView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                
                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                    photoView.setImageResource(android.R.drawable.ic_menu_report_image);
                }
                
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }
                
                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);
                }
            });
            photoView.setOnClickListener(this);
        }
        
        @Override
        public void onClick(View v) {
            if (v.getContext() instanceof Activity) {
                ((Activity) v.getContext()).finish();
                ((Activity) v.getContext()).overridePendingTransition(0, R.anim.zoon_out);
            }
            super.onClick(v);
        }
    }
}
