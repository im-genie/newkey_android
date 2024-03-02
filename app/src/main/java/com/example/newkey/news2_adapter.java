package com.example.newkey;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class news2_adapter extends RecyclerView.Adapter<news2_adapter.NewsViewHolder> {
    private List<news2_item> newsItems;

    public news2_adapter(List<news2_item> newsItems) {
        this.newsItems = newsItems;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news2_recyclerview, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        news2_item newsItem = newsItems.get(position);
        holder.newsTitle.setText(newsItem.getTitle());
        holder.newsPublisher.setText(newsItem.getPublisher());
        holder.newsTime.setText(newsItem.getTime());
        Glide.with(holder.itemView.getContext())
                .load(newsItem.getImg())
                .into(holder.newsImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), news3_activity.class);
            v.getContext().startActivity(intent);
        });

        holder.newsBookmark.setOnClickListener(v -> {
            boolean isChecked = holder.newsBookmark.getTag() != null && (boolean) holder.newsBookmark.getTag();
            if (!isChecked) {
                holder.newsBookmark.setImageResource(R.drawable.bookmark_checked);
                holder.newsBookmark.setTag(true);
            } else {
                holder.newsBookmark.setImageResource(R.drawable.bookmark_unchecked);
                holder.newsBookmark.setTag(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle, newsPublisher, newsTime;
        ImageView newsImage, newsBookmark;

        public NewsViewHolder(View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news2_item_title);
            newsPublisher = itemView.findViewById(R.id.news2_item_name);
            newsTime = itemView.findViewById(R.id.news2_item_time);
            newsImage = itemView.findViewById(R.id.news2_item_image);
            newsBookmark = itemView.findViewById(R.id.news2_bookmark);
            newsBookmark.setTag(false);
        }
    }
}
