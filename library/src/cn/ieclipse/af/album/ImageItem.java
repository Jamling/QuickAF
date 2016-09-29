package cn.ieclipse.af.album;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author Administrator
 *         
 */
public class ImageItem implements Serializable {
    public String imageId;
    public String thumbnailPath;
    public String imagePath;
    public String path;
    public boolean isSelected = false;
}
