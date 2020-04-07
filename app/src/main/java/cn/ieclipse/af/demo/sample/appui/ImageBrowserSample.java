package cn.ieclipse.af.demo.sample.appui;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ImagePagerAdapter;
import cn.ieclipse.af.demo.common.ui.ImageBrowserActivity;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.util.RandomUtils;

/**
 * Description
 *
 * @author Jamling
 */

public class ImageBrowserSample extends SampleBaseFragment {
    @Override
    protected int getContentLayout() {
        return 0;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int size = RandomUtils.genInt(1, 5);
        List<ImagePagerAdapter.IImage> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            MyImage image = new MyImage();
            image.url = "http://pic72.nipic.com/file/20150716/21422793_144600530000_2.jpg";
            list.add(image);
        }
        getActivity().overridePendingTransition(R.anim.zoon_in, 0);
        startActivity(ImageBrowserActivity.go(getActivity(), list, 2));
        getActivity().finish();
    }
    
    public static class MyImage implements ImagePagerAdapter.IImage {
        public String url;
        
        @Override
        public String getUrl() {
            return url;
        }
    }
}
