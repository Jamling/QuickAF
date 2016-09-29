/**
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.demo.sample.album;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.af.album.ImageItem;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.util.DialogUtils;

/**
 * 图片选择
 *
 * @author wangjian
 * @date 2015年7月4日
 */
public class SelectPhotoActivity extends BaseActivity implements View.OnClickListener {
    
    /* 用来标识请求照相功能的activity */
    private static final int REQ_TAKE_PICTURE = 0x3000;
    /* 用来标识请求请求相册 */
    private static final int REQ_PICK_PICTURE = 0x3001;
    /* 用来标识请求裁剪图片后的activity */
    private static final int REQ_CROP_PICTURE = 0x3002;
    
    // 允许选择的最大照片数
    public static final String EXTRA_MAX_PHOTO = "extra.max.photo";
    // 选择完图片后,是否需要裁剪
    public static final String EXTRA_NEED_CROP = "extra.need.crop";
    // 图片裁剪宽高
    public static final String EXTRA_CROP_OUTPUT_WIDTH = "crop.output.width";
    public static final String EXTRA_CROP_OUTPUT_HEIGHT = "crop.output.height";

    private boolean mNeedCrop = false;
    private int mCropOutpoutWidth = 300;
    private int mCropOutpoutHeight = 300;
    private int mMaxPhoto = 1;

    /* 拍照的照片存储位置 */
    private File PHOTO_DIR = Environment.getExternalStorageDirectory();
    // 照相机拍照得到的图片
    private File mCurrentPhotoFile;
    private String mFileName;
    
    private Button mAlbumBtn;
    private Button mCamBtn;
    private Button mCancelBtn;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_choose_photo;
    }

    @Override
    protected void initWindowFeature() {
        super.initWindowFeature();
        setShowTitleBar(false);
        setWindowBackground(R.color.black_alpha_20);
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        mAlbumBtn = (Button) findViewById(R.id.btn_choose_album);
        mCamBtn = (Button) findViewById(R.id.btn_choose_cam);
        mCancelBtn = (Button) findViewById(R.id.btn_choose_cancel);

        mAlbumBtn.setOnClickListener(this);
        mCamBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);

        // 设置点击外围view隐藏选择框
        View outer = findViewById(R.id.outer_view);
        outer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SelectPhotoActivity.this.finish();
                return false;
            }
        });
    }

    protected void initIntent(Bundle bundle) {
        mNeedCrop = bundle.getBoolean(SelectPhotoActivity.EXTRA_NEED_CROP, false);
        mMaxPhoto = bundle.getInt(SelectPhotoActivity.EXTRA_MAX_PHOTO);
        mCropOutpoutWidth = bundle.getInt(SelectPhotoActivity.EXTRA_CROP_OUTPUT_WIDTH);
        mCropOutpoutHeight = bundle.getInt(SelectPhotoActivity.EXTRA_CROP_OUTPUT_HEIGHT);

        if (mNeedCrop) {
            mMaxPhoto = 1;
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EXTRA_NEED_CROP, mNeedCrop);
        outState.putInt(EXTRA_MAX_PHOTO, mMaxPhoto);
        outState.putInt(EXTRA_CROP_OUTPUT_WIDTH, mCropOutpoutWidth);
        outState.putInt(EXTRA_CROP_OUTPUT_HEIGHT, mCropOutpoutHeight);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        // 相册
        if (v == mAlbumBtn) {
            AlbumActivity.forward(null, this, SelectPhotoActivity.REQ_PICK_PICTURE, mMaxPhoto);
        } // 照相机
        else if (v == mCamBtn) {
            doPickPhotoAction();
        }
        // 取消
        else if (v == mCancelBtn) {
            SelectPhotoActivity.this.finish();
        }
    }
    
    /**
     * 从照相机获取
     */
    private void doPickPhotoAction() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            doTakePhoto();
        }
        else {
            DialogUtils.showToast(SelectPhotoActivity.this, "没有可用的存储卡");
        }
    }
    
    /**
     * 拍照获取图片
     */
    protected void doTakePhoto() {
        try {
            mFileName = System.currentTimeMillis() + ".jpg";
            mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            startActivityForResult(intent, REQ_TAKE_PICTURE);
        } catch (Exception e) {
            DialogUtils.showToast(SelectPhotoActivity.this, "未找到系统相机程序");
        }
    }
    
    /**
     * 描述：因为调用了Camera和Gally所以要判断他们各自的返回情况, 他们启动时是这样的startActivityForResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
        if (resultCode == Activity.RESULT_OK ) {
            if (requestCode == REQ_PICK_PICTURE && mIntent != null) {
                List<ImageItem> list = (List<ImageItem>) mIntent.getSerializableExtra(Intent.EXTRA_RETURN_RESULT);

                setResultBack((Serializable) list);
            }
            else if (requestCode == REQ_TAKE_PICTURE) {
                String currentFilePath2 = mCurrentPhotoFile.getPath();

                List<ImageItem> temp = new ArrayList<>();
                ImageItem item = new ImageItem();
                item.imagePath = currentFilePath2;
                temp.add(item);
                setResultBack((Serializable) temp);
            }
            else if (requestCode == REQ_CROP_PICTURE) {
                String path = mIntent.getStringExtra(Intent.EXTRA_RETURN_RESULT);
                // 获取图片后返回的图片路径
                List<ImageItem> temp = new ArrayList<>();
                if (!TextUtils.isEmpty(path)) {
                    ImageItem item = new ImageItem();
                    item.imagePath = path;
                    temp.add(item);
                }
                
                setResultBack((Serializable) temp);
            }
        }
        else {
            if (requestCode == REQ_TAKE_PICTURE) {
                String currentFilePath2 = mCurrentPhotoFile.getPath();
                if(!mCurrentPhotoFile.exists()){
                    return;
                }
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mCurrentPhotoFile)));
                List<ImageItem> temp = new ArrayList<>();
                ImageItem item = new ImageItem();
                item.imagePath = currentFilePath2;
                temp.add(item);
                setResultBack((Serializable) temp);
            }
        }
    }
    
    public static void go(Fragment f, Activity context, int requestCode, Bundle bundle) {
        Intent intent = new Intent(context, SelectPhotoActivity.class);
        intent.putExtras(bundle);
        if (f != null) {
            f.startActivityForResult(intent, requestCode);
        }
        else if (context instanceof Activity) {
            context.startActivityForResult(intent, requestCode);
        }
    }
    
    private void setResultBack(Serializable temp) {
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, temp);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
