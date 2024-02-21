package com.example.newkey;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class news1_adapter extends RecyclerView.Adapter<news1_adapter.NewsViewHolder> {
    private List<news1_item> newsItems;

    public news1_adapter(List<news1_item> newsItems) {
        this.newsItems = newsItems;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news1_recyclerview, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        news1_item newsItem = newsItems.get(position);
        holder.newsTitle.setText(newsItem.getTitle());
        holder.newsPublisher.setText(newsItem.getPublisher());
        holder.newsTime.setText(newsItem.getTime());
        Glide.with(holder.itemView.getContext())
                .load(newsItem.getImg())
                .into(holder.newsImage);

        // 아이템 클릭 리스너 추가
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), news3_activity.class);
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
    }
}
