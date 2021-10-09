package cn.ieclipse.af.demo.sample.cview;

import android.Manifest;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.PermissionUtils;
import cn.ieclipse.af.view.StaggeredGridView;

public class StaggeredGridViewActivity extends BaseActivity
        implements LoaderCallbacks<Cursor> {

    private StaggeredGridView mGrid;
    private SimpleCursorAdapter mAdapter = null;
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Thumbnails.DATA,
            MediaStore.Images.Thumbnails.DATA,
            MediaStore.Images.Thumbnails._ID};

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_staggered;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        setTitle("StaggeredGridView");
        mGrid = (StaggeredGridView) view.findViewById(R.id.grid);
        mGrid.setColumnCount(3);
        mGrid.setItemMargin(AppUtils.dp2px(this, 10));
        // mGrid.setMinColumnWidth(100);

        mAdapter = new SimpleCursorAdapter(this, R.layout.sample_grid_item_staggered,
                null, STORE_IMAGES, new int[]{R.id.textView, R.id.imageView},
                CursorAdapter.FLAG_AUTO_REQUERY) {
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                final ImageView iv = (ImageView) view
                        .findViewById(R.id.imageView);
                super.bindView(view, context, cursor);
            }

        };
        mGrid.setAdapter(mAdapter);
        if (PermissionUtils.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            PermissionUtils.requestPermissions(this, 0x01, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this,
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, STORE_IMAGES,
                null, null, null);
        return loader;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }
}
