package cn.ieclipse.af.demo.sample.album;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.List;

import cn.ieclipse.af.album.BasePopupWindowForListView;
import cn.ieclipse.af.album.CommonAdapter;
import cn.ieclipse.af.album.ImageBucket;
import cn.ieclipse.af.album.ViewHolder;
import cn.ieclipse.af.demo.R;

public class ListImageDirPopupWindow extends BasePopupWindowForListView<ImageBucket> {
    private ListView mListDir;
    
    public ListImageDirPopupWindow(int width, int height, List<ImageBucket> datas, View convertView) {
        super(convertView, width, height, true, datas);
    }
    
    public void initViews() {
        mListDir = (ListView) findViewById(R.id.lv_list_dir);
        mListDir.setAdapter(new CommonAdapter<ImageBucket>(mContext, mDataList, R.layout.album_list_item_dir) {
            @Override
            public void convert(ViewHolder helper, ImageBucket item) {
                helper.setText(R.id.tv_dir_item_name, item.bucketName);
                helper.setImageByUrl(R.id.iv_dir_item_image, item.imageList.get(0).imagePath);
                helper.setText(R.id.tv_dir_item_count, item.count + "张");
            }
        });
    }
    
    public interface OnImageDirSelected {
        void selected(ImageBucket bucket);
    }
    
    private OnImageDirSelected mImageDirSelected;
    
    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
        this.mImageDirSelected = mImageDirSelected;
    }
    
    @Override
    public void initEvents() {
        mListDir.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
                if (mImageDirSelected != null) {
                    mImageDirSelected.selected(mDataList.get(position));
                }
            }
        });
    }
    
    @Override
    public void init() {
    }
    
    @Override
    protected void beforeInitWeNeedSomeParams(Object... params) {
    }
    
}
