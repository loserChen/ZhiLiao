package com.example.chenzeyuan.zhihu1.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzeyuan on 2018/5/17.
 * 过往文章，链接后缀加上日期即可获取内容
 * 链接：http://news.at.zhihu.com/api/4/news/before/20160728
 */
public class ArticleBefore {

    private String date;

    private ArrayList<Stories> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Stories> getStories() {
        return stories;
    }

    public void setStories(ArrayList<Stories> stories) {
        this.stories = stories;
    }

}
