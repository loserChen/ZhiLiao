package com.example.chenzeyuan.zhihu1.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.chenzeyuan.zhihu1.Adapter.FavoriteListAdapter;
import com.example.chenzeyuan.zhihu1.Adapter.ListAdapter;
import com.example.chenzeyuan.zhihu1.Entity.ListNews;
import com.example.chenzeyuan.zhihu1.Helper.MyDBHelper;
import com.example.chenzeyuan.zhihu1.R;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private ImageButton back;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ArrayList<ListNews> listNews;
    private MyDBHelper helper;
    private SQLiteDatabase mydb;
    private FavoriteListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        initDB();
        initView();
    }
    public void initView(){
        back=(ImageButton)findViewById(R.id.article_back1);
        recyclerView=(RecyclerView)findViewById(R.id.recylerview1);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        queryData();
        adapter=new FavoriteListAdapter(listNews,FavoriteActivity.this);
        recyclerView.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FavoriteActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void initDB(){
        helper = MyDBHelper.getInstance(FavoriteActivity.this);
        mydb = helper.getWritableDatabase();
    }
    private void queryData() {
        listNews=new ArrayList<>();
        String sql = "SELECT * FROM " + helper.TABLE_NAME;
        Cursor cursor = mydb.rawQuery(sql, null);
        while(cursor.moveToNext()){
            int id=cursor.getInt(0);
            Log.e("1",String.valueOf(id));
            String title=cursor.getString(1);
            Log.e("1",title);
            String image=cursor.getString(2);
            Log.e("1",image);
            listNews.add(new ListNews(id,title,image));
        }
    }
}
