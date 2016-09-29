/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.demo.sample.appui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.af.adapter.AfBaseAdapter;
import cn.ieclipse.af.album.ImageItem;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.demo.sample.album.SelectPhotoActivity;

/**
 * 图片选择Activity
 *
 * @author Harry
 * @date 2016/9/29.
 */
public class PhotoActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public static final int REQ_CAMERA = 2;

    public List<FileItem> mFiles;
    public FileItem mEmptyFile = new FileItem("");
    public GridView mImageGv;
    public ImageAdapter mAdapter;

    protected int mMaxPhotos = 10;

    public List<FileItem> mImageList = new ArrayList<>();
    public List<FileItem> mDelList = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_photo;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setTitle("photo");
    }

    @Override
    protected void initData() {
        super.initData();
        if (mImageGv != null) {
            mAdapter = new ImageAdapter();
            mAdapter.setDataList(getFiles());
            mImageGv.setAdapter(mAdapter);
        }
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        mImageGv = (GridView) view.findViewById(R.id.gv_image);
        if (mImageGv != null) {
            mImageGv.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent instanceof GridView) {
            if (position < mAdapter.getCount() - 1) {
                FileItem item = mAdapter.getItem(position);
                if (!TextUtils.isEmpty(item.id) && !mDelList.contains(item)) {
                    mDelList.add(item);
                }
                getFiles().remove(position);
                onImageFileChanged();
            }
            else {
                pickPicture(mMaxPhotos - getFiles().size());
            }
        }
    }

    protected void pickPicture(int max) {
        if (max <= 0) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(SelectPhotoActivity.EXTRA_MAX_PHOTO, max);
        SelectPhotoActivity.go(null, this, REQ_CAMERA, bundle);
    }

    @Override
    public void finish() {
        hideLoadingDialog();
        super.finish();
    }

    protected File[] getFile() {
        if (getPics().size() <= 1) {
            return null;
        }
        List<FileItem> pics = getPics();
        File[] fs = new File[pics.size() - 1];
        for (int i = 0; i < pics.size() - 1; i++) {
            fs[i] = pics.get(i).getFile();
        }
        return fs;
    }

    protected void onImageFileChanged() {
        mAdapter.setDataList(getFiles());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CAMERA && data != null) {
                List<ImageItem> list = (List<ImageItem>) data.getSerializableExtra(Intent.EXTRA_RETURN_RESULT);
                if (list != null) {
                    for (ImageItem imageItem : list) {
                        getFiles().add(new FileItem(imageItem));
                    }
                }
                onImageFileChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public List<FileItem> getFiles() {
        if (mFiles == null) {
            mFiles = new ArrayList<>();
        }
        if (mFiles.contains(mEmptyFile)) {
            mFiles.remove(mEmptyFile);
        }
        mFiles.add(mEmptyFile);
        return mFiles;
    }

    public List<FileItem> getPics() {
        List<FileItem> items = new ArrayList<>();
        if (getFiles().size() > 1) {
            for (FileItem item : getFiles()) {
                if (!item.isUploaded()) {
                    items.add(item);
                }
            }
        }
        return items;
    }

    protected static class FileItem implements java.io.Serializable {
        private static final String SCHEMA_HTTP = "http://";
        private static final String SCHEMA_FILE = "file://";
        public String path;
        public String id;
        public File file;
        public String thumb;

        public FileItem(String path) {
            this.path = path;
        }

        public FileItem(ImageItem item) {
            this.path = item.imagePath;
            this.thumb = item.thumbnailPath;
            if (TextUtils.isEmpty(thumb)) {
                this.thumb = item.imagePath;
            }
        }

        public FileItem(String id, String path) {
            this.path = path;
            this.id = id;
        }

        public File getFile() {
            if (file == null) {
                file = new File(path);
            }
            return file;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isUploaded() {
            return !TextUtils.isEmpty(this.id) || (path.startsWith(SCHEMA_HTTP));
        }

        public String getUrl() {
            if (isUploaded()) {
                if (this.thumb.startsWith(SCHEMA_HTTP)) {
                    return this.thumb;
                }
                else {
                    return SCHEMA_HTTP + this.thumb;
                }
            }
            else {
                if (this.thumb.startsWith(SCHEMA_FILE)) {
                    return this.thumb;
                }
                else {
                    return SCHEMA_FILE + this.thumb;
                }
            }
        }
    }

    public class ImageAdapter extends AfBaseAdapter<FileItem> {

        @Override
        public int getLayout() {
            return R.layout.post_photo_item;
        }

        @Override
        public void onUpdateView(View convertView, int position) {
            Holder holder = (Holder) convertView.getTag();
            if (holder == null) {
                holder = new Holder();
                holder.mDel = (ImageView) convertView.findViewById(R.id.iv_delete_icon);
                holder.mImage = (ImageView) convertView.findViewById(R.id.iv_image);
                convertView.setTag(holder);
            }
            int count = getCount();
            if (position == count - 1) {
                holder.mDel.setVisibility(View.GONE);
                holder.mImage.setImageResource(R.mipmap.bg_add_photo);
            }
            else {
                FileItem item = getItem(position);
                holder.mDel.setVisibility(View.VISIBLE);
                holder.mImage.setImageURI(Uri.parse("file:///"+item.path));

            }
        }

        @Override
        public int getCount() {
            return super.getCount();
        }
    }

    private static class Holder {
        public ImageView mImage;
        public ImageView mDel;
    }
}
