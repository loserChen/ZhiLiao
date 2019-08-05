package com.example.chenzeyuan.zhihu1.Utility;


import com.example.chenzeyuan.zhihu1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by chenzeyuan on 2018/5/17.
 * 常量
 */
public class Constant {

    public final static String ArticleContentUrl = "http://news-at.zhihu.com/api/4/news/";

    public final static String LatestArticleUrl = "http://news-at.zhihu.com/api/4/news/latest";

    public final static String BeforeArticleUrl = "http://news.at.zhihu.com/api/4/news/before/";

    public static ImageLoader getImageLoader() {
        return ImageLoader.getInstance();
    }

    public static DisplayImageOptions getDisplayImageOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher_background) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher_background)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher_background)  //设置图片加载/解码过程中错误时候显示的图片
                .displayer(new FadeInBitmapDisplayer(0))//是否图片加载好后渐入的动画时间
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

}
