package com.example.chenzeyuan.zhihu1.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenzeyuan.zhihu1.Entity.ArticleContent;
import com.example.chenzeyuan.zhihu1.Helper.MyDBHelper;
import com.example.chenzeyuan.zhihu1.Listener.OnLoadDataListener;
import com.example.chenzeyuan.zhihu1.Net.HttpUtil;
import com.example.chenzeyuan.zhihu1.R;
import com.example.chenzeyuan.zhihu1.Utility.Constant;

import java.io.File;

import ren.yale.android.cachewebviewlib.CacheWebView;
import ren.yale.android.cachewebviewlib.WebViewCache;

public class ArticleContentActivity extends AppCompatActivity {
    private ImageButton back;
    private ImageButton favorite;
    private WebView webView;
    private OnLoadDataListener onLoadDataListener;
    private WebSettings webSettings;
    private boolean flag;
    private MyDBHelper helper;
    private SQLiteDatabase mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);
        initView();
        initDB();
        initListener();
        HttpUtil.getInstance().getArticleContent(getIntent().getIntExtra("ID", 0), onLoadDataListener);
    }
    private void initView(){
        back=(ImageButton)findViewById(R.id.article_back);
        favorite=(ImageButton)findViewById(R.id.article_favorite);
        webView=(WebView)findViewById(R.id.article_content);
        webSettings=webView.getSettings();
        webSettings.setAppCacheEnabled(true);
        // 设置缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
    }
    private void initDB() {
        helper = MyDBHelper.getInstance(ArticleContentActivity.this);
        mydb = helper.getWritableDatabase();
        queryData();
    }
    private void initListener(){
        onLoadDataListener = new OnLoadDataListener() {
            @Override
            public void onSuccess(Object object) {
                ArticleContent content = (ArticleContent) object;
                webView.loadDataWithBaseURL("x-data://base", content.getBody(), "text/html", "UTF-8", null);
                ImageView imageView=(ImageView)findViewById(R.id.top_image);
                Constant.getImageLoader().displayImage(content.getImage(), imageView, Constant.getDisplayImageOptions());
            }

            @Override
            public void onFailure() {
                if (!HttpUtil.getInstance().isNetworkConnected(ArticleContentActivity.this)) {
                    Toast.makeText(ArticleContentActivity.this,"似乎没有网络连接！",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ArticleContentActivity.this,"文章加载失败！",Toast.LENGTH_SHORT).show();
                }
            }
        };
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ArticleContentActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(flag==false) {
                        favorite.setImageResource(R.drawable.ic_favorite_red_300_24dp);
                        insertData(getIntent().getIntExtra("ID", 0), getIntent().getStringExtra("Title"), getIntent().getStringExtra("Image"));
                        Toast.makeText(ArticleContentActivity.this, "已添加至收藏！", Toast.LENGTH_SHORT).show();
                        flag = true;
                    }else{
                        favorite.setImageResource(R.drawable.ic_favorite_white_24dp);
                        removeData(getIntent().getIntExtra("ID", 0));
                        Toast.makeText(ArticleContentActivity.this,"已取消收藏！",Toast.LENGTH_SHORT).show();
                        flag=false;
                    }
                }
            });
    }
    public static void startActivity(Context context, int id,String title,String image) {
        Intent intent = new Intent(context, ArticleContentActivity.class);
        intent.putExtra("ID", id);
        intent.putExtra("Title",title);
        intent.putExtra("Image",image);
        context.startActivity(intent);
    }
    private void insertData(int id, String title, String image) {
        String sql = "INSERT INTO " + helper.TABLE_NAME + " (id,title,image) VALUES (" + id + ",'" + title + "','" + image + "')";
        mydb.execSQL(sql);
    }
    private void queryData() {
        String sql = "SELECT * FROM " + helper.TABLE_NAME+" where id="+getIntent().getIntExtra("ID", 0);
        Cursor cursor = mydb.rawQuery(sql, null);
        if (cursor.getCount() ==1) {
            favorite.setImageResource(R.drawable.ic_favorite_red_300_24dp);
            flag=true;
        }
    }
    private void removeData(int id){
        String sql="delete from "+helper.TABLE_NAME+" where id="+id;
        mydb.execSQL(sql);
    }
}
