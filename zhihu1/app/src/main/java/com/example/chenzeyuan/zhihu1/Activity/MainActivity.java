package com.example.chenzeyuan.zhihu1.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.chenzeyuan.zhihu1.Adapter.ListAdapter;
import com.example.chenzeyuan.zhihu1.Entity.ArticleBefore;
import com.example.chenzeyuan.zhihu1.Entity.ArticleContent;
import com.example.chenzeyuan.zhihu1.Entity.ArticleLatest;
import com.example.chenzeyuan.zhihu1.Entity.ListNews;
import com.example.chenzeyuan.zhihu1.Entity.Stories;
import com.example.chenzeyuan.zhihu1.Listener.OnLoadDataListener;
import com.example.chenzeyuan.zhihu1.Loader.GlideImageLoader;
import com.example.chenzeyuan.zhihu1.Net.HttpUtil;
import com.example.chenzeyuan.zhihu1.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnBannerListener {
    private ArrayList<Stories> newsList;
    private SwipeRefreshLayout refreshLayout;
    private ListAdapter adapter;
    private Banner banner;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    private OnLoadDataListener listener;
    private HttpUtil httpUtil;
    private ArticleLatest articleLatest;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private String date;
    private OnLoadDataListener beforeListener;
    private int lastVisibleItem;
    private ImageButton list;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initLatestNews();
    }
    private void initView(){
        httpUtil=HttpUtil.getInstance();
        recyclerView=(RecyclerView)findViewById(R.id.recylerview);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        banner=(Banner)findViewById(R.id.banner);
        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.layRefresh);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        newsList=new ArrayList<>();
        adapter=new ListAdapter(newsList,MainActivity.this);
        recyclerView.setAdapter(adapter);
        list=(ImageButton)findViewById(R.id.article_favorite);
        back=(ImageButton)findViewById(R.id.main_back);
    }
    private void initListener(){
        listener=new OnLoadDataListener() {
            @Override
            public void onSuccess(Object object) {
                newsList=new ArrayList<>();
                //放图片地址的集合
                list_path = new ArrayList<>();
                //放标题的集合
                list_title = new ArrayList<>();
                articleLatest=(ArticleLatest)object;
                ArrayList<Stories>StoriesList=articleLatest.getStories();
                newsList.clear();
                newsList.addAll(StoriesList);
                list_path.clear();
                for(int i=0;i<articleLatest.getTop_stories().size();i++){
                    list_path.add(articleLatest.getTop_stories().get(i).getImage());
                }
                list_title.clear();
                for(int i=0;i<articleLatest.getTop_stories().size();i++){
                    list_title.add(articleLatest.getTop_stories().get(i).getTitle());
                }
                adapter.replaceAll(newsList);
                initBanner();
                setDate(articleLatest.getDate());
            }

            @Override
            public void onFailure() {

            }
        };
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    initLatestNews();
                    refreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this,"已经是最新的文章了！",Toast.LENGTH_SHORT).show();
            }
        });
        beforeListener=new OnLoadDataListener() {
            @Override
            public void onSuccess(Object object) {
                ArticleBefore articleBefore=(ArticleBefore)object;
                ArrayList<Stories>StoriesList=articleBefore.getStories();
                newsList.addAll(StoriesList);
                adapter.replaceAll(newsList);
                setDate(articleBefore.getDate());
            }

            @Override
            public void onFailure() {

            }
        };
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState ==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==adapter.getItemCount()){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"正在加载ing",Toast.LENGTH_SHORT).show();
                            initBeforeNews();
                        }
                    },1000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem=layoutManager.findLastVisibleItemPosition();
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,FavoriteActivity.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder bb=new AlertDialog.Builder(MainActivity.this);
                bb.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                });
                bb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                bb.setMessage("你确定要退出吗？");
                bb.setTitle("提示");
                bb.show();
            }
        });
    }
    private void initLatestNews(){
//        ArrayList<ListNews> list=new ArrayList<>();
//        for(int i=0;i<10;i++) {
//            ListNews a = new ListNews("hhhhhh", R.drawable.ic_launcher_background);
//            list.add(a);
//        }
//        return list;
        if(httpUtil.isNetworkConnected(MainActivity.this)){
            httpUtil.getLatestArticleList(listener);
        }
    }
//    private ArrayList<ListNews> init(){
//        ArrayList<ListNews> list=new ArrayList<>();
//        for(int i=0;i<10;i++) {
//            ListNews a = new ListNews("jabdjkasbjkasb", R.drawable.ic_launcher_background);
//            list.add(a);
//        }
//        return list;
//    }
    private void initBeforeNews(){
        if(httpUtil.isNetworkConnected(MainActivity.this)) {
            httpUtil.getBeforeArticleList(getDate(), beforeListener);
        }
    }
    private void initBanner(){
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new GlideImageLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
    }
    //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
        Log.i("tag", "你点了第"+position+"张轮播图");
        int id=articleLatest.getTop_stories().get(position).getId();
        String title=articleLatest.getTop_stories().get(position).getTitle();
        String image=articleLatest.getTop_stories().get(position).getImage();
        Intent intent=new Intent(MainActivity.this, ArticleContentActivity.class);
        intent.putExtra("ID",id);
        intent.putExtra("Title",title);
        intent.putExtra("Image",image);
        startActivity(intent);
    }
    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date=date;
    }
    }
