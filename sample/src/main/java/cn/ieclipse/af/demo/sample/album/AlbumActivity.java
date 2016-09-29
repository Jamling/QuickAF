package cn.ieclipse.af.demo.sample.album;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.af.adapter.AfBaseAdapter;
import cn.ieclipse.af.album.AlbumHelper;
import cn.ieclipse.af.album.ImageBucket;
import cn.ieclipse.af.album.ImageItem;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.util.AppUtils;

@SuppressLint("HandlerLeak")
public class AlbumActivity extends BaseActivity implements ListImageDirPopupWindow.OnImageDirSelected {
    
    private List<ImageBucket> mImageList;
    private GridView mImageGv;
    private ImageAdapter mAdapter;// 自定义的适配器
    private AlbumHelper mAlbumHelper;
    
    private View mBottomLayout;
    
    private TextView mChooseDirTv;
    private TextView mImageCountTv;
    private int mTotalCount = 0;
    
    private int mSelectCount = 0;
    private int mScreenHeight;
    
    private ListImageDirPopupWindow mListImageDirPopupWindow;
    private TextView mConfirmTv;
    
    private static int MSG_UPDATE_ITEM;
    /**
     * 最大选择数
     */
    private int mMaxPhoto = 9;
    
    @Override
    protected int getContentLayout() {
        return R.layout.activity_album;
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mAlbumHelper = new AlbumHelper();
        mAlbumHelper.init(getApplicationContext());
        mTitleTextView.setText(R.string.group_album);
        mConfirmTv = (TextView) mTitleBar.addRightView(R.layout.title_right_tv);
        mConfirmTv.setText(R.string.common_ok);
        setOnClickListener(mConfirmTv);
    }
    
    @Override
    protected void initContentView() {
        super.initContentView();
        mScreenHeight = AppUtils.getDisplayMetrics(AlbumActivity.this).heightPixels;
        mImageGv = (GridView) findViewById(R.id.gv_image);
        mChooseDirTv = (TextView) findViewById(R.id.tv_choose_dir);
        mImageCountTv = (TextView) findViewById(R.id.tv_total_count);
        
        mBottomLayout = findViewById(R.id.rl_bottom);
    }
    
    @Override
    protected void initData() {
//        super.initData();
        mImageList = mAlbumHelper.getImagesBucketList(false);
        mAdapter = new ImageAdapter();
        mAdapter.setDataList(mImageList.get(0).imageList);
        mImageGv.setAdapter(mAdapter);
        for (ImageBucket bucket : mImageList) {
            mTotalCount += bucket.imageList.size();
        }
        mImageCountTv.setText(mTotalCount + "张");
        initListDirPopupWindw();
        initEvent();
    }
    
    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
            mImageList, LayoutInflater.from(getApplicationContext()).inflate(R.layout.album_list_dir, null));

        mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {
            
            @Override
            public void onDismiss() {
                // 设置背景颜色变亮
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }
    
    @Override
    public void onClick(View v) {
        if (mConfirmTv == v) {
            onSelectCompleted();
        }
        super.onClick(v);
    }
    
    private void initEvent() {
        /**
         * 为底部的布局设置点击事件，弹出popupWindow
         */
        mBottomLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListImageDirPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
                mListImageDirPopupWindow.showAsDropDown(mBottomLayout, 0, 0);

                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .4f;
                getWindow().setAttributes(lp);
            }
        });
    }
    
    @Override
    public void selected(ImageBucket folder) {
        mSelections.clear();
        mAdapter.setDataList(folder.imageList);
        mAdapter.notifyDataSetChanged();
        mImageGv.clearChoices();
        mImageCountTv.setText(folder.count + "张");
        mChooseDirTv.setText(folder.bucketName);
        mListImageDirPopupWindow.dismiss();
    }
    
    private void onSelectCompleted() {
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, (Serializable) mSelections);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
    
    private List<ImageItem> mSelections = new ArrayList<>();
    
    public static void forward(Fragment f, Activity context, int requestCode, int maxPhotos) {
        Intent intent = new Intent(context, AlbumActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(SelectPhotoActivity.EXTRA_MAX_PHOTO, maxPhotos);
        intent.putExtras(bundle);
        if (f != null) {
            f.startActivityForResult(intent, requestCode);
        }
        else if (context instanceof Activity) {
            context.startActivityForResult(intent, requestCode);
        }
        else {
            context.startActivity(intent);
        }
    }
    
    @Override
    protected void initIntent(Bundle bundle) {
        super.initIntent(bundle);
        mMaxPhoto = bundle.getInt(SelectPhotoActivity.EXTRA_MAX_PHOTO, mMaxPhoto);
        if (mMaxPhoto <= 0) {
            mMaxPhoto = 1;
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SelectPhotoActivity.EXTRA_MAX_PHOTO, mMaxPhoto);
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private class ImageAdapter extends AfBaseAdapter<ImageItem> {
        
        @Override
        public int getLayout() {
            return R.layout.album_group_item_image_grid;
        }
        
        @Override
        public void onUpdateView(View convertView, int position) {
            ImageView iv = (ImageView) convertView.findViewById(R.id.iv_image);
            final CheckBox chk = (CheckBox) convertView.findViewById(R.id.iv_chk);
            final ImageItem item = getItem(position);
            String file = TextUtils.isEmpty(item.thumbnailPath) ? item.imagePath : item.thumbnailPath;
            iv.setImageURI(Uri.parse("file:///" + file));
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chk.isChecked()) {
                        if (!mSelections.contains(item)) {
                            mSelections.add(item);
                        }
                    }
                    else {
                        if (mSelections.contains(item)) {
                            mSelections.remove(item);
                        }
                    }
                    if (mMaxPhoto == 1 && !mSelections.isEmpty()) {
                        onSelectCompleted();
                    }
                    if (mMaxPhoto < mSelections.size()) {
                        if (mSelections.contains(item)) {
                            mSelections.remove(item);
                            chk.setChecked(false);
                        }
                    }
                }
            });
        }

    }
}
