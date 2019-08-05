package com.example.chenzeyuan.zhihu1.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzeyuan on 2018/5/17.
 * 最新文章，包含今日文章与热门文章
 * 链接:http://news-at.zhihu.com/api/4/news/latest
 */
public class ArticleLatest {

    private String date;

    private ArrayList<Stories> stories;

    private ArrayList<TopStories> top_stories;

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

    public ArrayList<TopStories> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(ArrayList<TopStories> top_stories) {
        this.top_stories = top_stories;
    }

}
