package com.example.newkey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<news1_item> newsItems;

    public NewsAdapter(List<news1_item> newsItems) {
        this.newsItems = newsItems;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        news1_item newsItem = newsItems.get(position);
        holder.setItem(newsItem);

        Glide.with(holder.itemView.getContext())
                .load(newsItem.getImg())
                .into(holder.newsImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), news3_activity.class);
                intent.putExtra("id", newsItem.getId());
                intent.putExtra("title", newsItem.getTitle());
                intent.putExtra("content", newsItem.getContent());
                intent.putExtra("publisher", newsItem.getPublisher());
                intent.putExtra("date", newsItem.getDate());
                intent.putExtra("img", newsItem.getImg());
                intent.putExtra("summary", newsItem.getSummary());
                intent.putExtra("key", newsItem.getKey());
                intent.putExtra("url", newsItem.getUrl());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView newsTitle, newsPublisher, newsTime;
        public ImageView newsImage;

        public NewsViewHolder(View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news1_item_title);
            newsPublisher = itemView.findViewById(R.id.news1_item_name);
            newsTime = itemView.findViewById(R.id.news1_item_time);
            newsImage = itemView.findViewById(R.id.news1_item_image);
        }
        public void setItem(news1_item item){
            newsTitle.setText(item.getTitle());
            newsPublisher.setText(item.getPublisher());
            newsTime.setText(item.getDate());
        }
    }
}

