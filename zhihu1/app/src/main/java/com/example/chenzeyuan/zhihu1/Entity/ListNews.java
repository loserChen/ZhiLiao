package com.example.chenzeyuan.zhihu1.Entity;

import java.util.List;

/**
 * Created by chenzeyuan on 2018/5/15.
 */

public class ListNews {
    private String title;
    private String image;
    private int id;
    public ListNews(int id,String title, String image) {
        this.title = title;
        this.image = image;
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
