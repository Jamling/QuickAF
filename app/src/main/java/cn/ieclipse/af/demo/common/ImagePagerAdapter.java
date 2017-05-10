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
package cn.ieclipse.af.demo.common;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.ieclipse.af.adapter.AfPagerAdapter;
import cn.ieclipse.af.demo.R;

/**
 * Description
 *
 * @author Jamling
 */
public class ImagePagerAdapter extends AfPagerAdapter<ImagePagerAdapter.IImage> implements View.OnClickListener {
    
    @Override
    public int getLayout() {
        return R.layout.common_pager_item_image;
    }
    
    @Override
    public void onUpdateView(View convertView, int position) {
        ImageView iv = (ImageView) convertView;
        IImage info = getItem(position);
        if (info != null && info.getUrl() != null) {
            String url = info.getUrl();
            if (info.getUrl().startsWith("android.resource://")) {
                iv.setImageURI(Uri.parse(url));
            }
            else {
                ImageLoader.getInstance().displayImage(url, iv);
            }
        }
        iv.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        
    }
    
    public interface IImage extends java.io.Serializable {
        String getUrl();
    }
}
