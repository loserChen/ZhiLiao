package com.example.chenzeyuan.zhihu1.Listener;


import com.example.chenzeyuan.zhihu1.Entity.TopStories;

import java.util.ArrayList;
import java.util.List;

public interface OnLoadTopArticleListener {

    void onSuccess(ArrayList<TopStories> topStoriesList);

    void onFailure();

}
