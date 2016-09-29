package cn.ieclipse.af.demo.sample.album;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.List;

import cn.ieclipse.af.album.ImageBucket;
import cn.ieclipse.af.demo.R;

public class ImageDirPopupWindow extends BasePopupWindow<ImageBucket> {
    private ListView mListDir;
    private CommonAdapter mAdapter;

    public ImageDirPopupWindow(Context context, List<ImageBucket> data, int id) {
        super(context,data,id);
    }
    
    public void initViews() {
        mAdapter=new CommonAdapter();
        mAdapter.setDataList(mDataList);
        mListDir = (ListView) findViewById(R.id.lv_list_dir);
        mListDir.setAdapter(mAdapter);
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

}
