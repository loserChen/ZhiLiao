package com.example.chenzeyuan.zhihu1.Listener;


import com.example.chenzeyuan.zhihu1.Entity.Stories;
import com.example.chenzeyuan.zhihu1.Entity.TopStories;

import java.util.ArrayList;
import java.util.List;

public interface OnLoadDataListener {

    void onSuccess(Object object);

    void onFailure();

}
