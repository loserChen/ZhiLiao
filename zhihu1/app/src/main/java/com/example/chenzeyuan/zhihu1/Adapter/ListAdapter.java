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
import com.example.chenzeyuan.zhihu1.Entity.Stories;
import com.example.chenzeyuan.zhihu1.Listener.OnItemClickListener;
import com.example.chenzeyuan.zhihu1.R;
import com.example.chenzeyuan.zhihu1.Utility.Constant;

import java.util.ArrayList;

/**
 * Created by chenzeyuan on 2018/5/15.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<Stories> storiesList;
    private ViewHolder holder;
    private OnItemClickListener onItemClickListener;
    private Context context;
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
    public void replaceAll(ArrayList<Stories> list) {
        storiesList.clear();
        if (list != null && list.size() > 0) {
            storiesList.addAll(list);
        }
        notifyDataSetChanged();
    }
    public ListAdapter(ArrayList<Stories> storiesList,Context context){
        this.storiesList=storiesList;
        this.context=context;
        init();
    }
    public void init(){
        //文章列表点击事件监听
        onItemClickListener = new OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                int id = storiesList.get(position).getId();
                String title=storiesList.get(position).getTitle();
                String image=storiesList.get(position).getImages().get(0);
                ArticleContentActivity.startActivity(context, id,title,image);
                Log.e("1",String.valueOf(position));
            }
        };
    }
    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        holder.title.setText(storiesList.get(position).getTitle());
        Constant.getImageLoader().displayImage(storiesList.get(position).getImages().get(0),
                holder.imageView, Constant.getDisplayImageOptions());
        holder.setItemClickListener(onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return storiesList.size();
    }
}
