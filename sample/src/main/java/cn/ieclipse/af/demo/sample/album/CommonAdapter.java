package cn.ieclipse.af.demo.sample.album;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ieclipse.af.adapter.AfBaseAdapter;
import cn.ieclipse.af.album.ImageBucket;
import cn.ieclipse.af.demo.R;

public class CommonAdapter extends AfBaseAdapter<ImageBucket> {
    @Override
    public int getLayout() {
        return R.layout.album_list_item_dir;
    }

    @Override
    public void onUpdateView(View convertView, int position) {
        ImageBucket item = getItem(position);
        TextView name = (TextView) convertView.findViewById(R.id.tv_dir_item_name);
        ImageView iv = (ImageView) convertView.findViewById(R.id.iv_dir_item_image);
        TextView num = (TextView) convertView.findViewById(R.id.tv_dir_item_count);
        name.setText(item.bucketName);
        ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(item.imageList.get(0).imagePath,
            iv);
        num.setText(item.count + "张");
    }

}
