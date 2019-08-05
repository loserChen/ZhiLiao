package com.example.chenzeyuan.zhihu1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenzeyuan.zhihu1.Activity.ArticleContentActivity;
import com.example.chenzeyuan.zhihu1.Entity.ListNews;
import com.example.chenzeyuan.zhihu1.Listener.OnItemClickListener;
import com.example.chenzeyuan.zhihu1.R;
import com.example.chenzeyuan.zhihu1.Utility.Constant;

import java.util.ArrayList;

/**
 * Created by chenzeyuan on 2018/5/19.
 */

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>{
    private ArrayList<ListNews> listNews;
    private Context context;
    private ViewHolder holder;
    private OnItemClickListener onItemClickListener;
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView title;
        private OnItemClickListener itemClickListener;
        public ViewHolder(View view){
            super(view);
            imageView=(ImageView)view.findViewById(R.id.image);
            title=(TextView)view.findViewById(R.id.title);
            imageView.setOnClickListener(this);
            title.setOnClickListener(this);
        }
        public void setItemClickListener(OnItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.OnItemClick(getAdapterPosition());
            }
        }
    }
    public void init(){
        //文章列表点击事件监听
        onItemClickListener = new OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                int id = listNews.get(position).getId();
                String title=listNews.get(position).getTitle();
                String image=listNews.get(position).getImage();
                ArticleContentActivity.startActivity(context, id,title,image);
                Log.e("1",String.valueOf(position));
            }
        };
    }
    public FavoriteListAdapter(ArrayList<ListNews> listNews,Context context) {
        this.listNews=listNews;
        this.context=context;
        init();
    }

    @NonNull
    @Override
    public FavoriteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        holder=new FavoriteListAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteListAdapter.ViewHolder holder, int position) {
        holder.title.setText(listNews.get(position).getTitle());
        Constant.getImageLoader().displayImage(listNews.get(position).getImage(),
                holder.imageView, Constant.getDisplayImageOptions());
        holder.setItemClickListener(onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return listNews.size();
    }
}
