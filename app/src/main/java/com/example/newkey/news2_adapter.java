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

public class news2_adapter extends RecyclerView.Adapter<news2_adapter.NewsViewHolder> {
    private List<news1_item> newsItems;

    public news2_adapter(List<news1_item> newsItems) {
        this.newsItems = newsItems;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news2_recyclerview, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        news1_item newsItem = newsItems.get(position);
        holder.setItem(newsItem);

        // Glide로 이미지 로드
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
                v.getContext().startActivity(intent);

                //클릭 시 사용자 정보 저장
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
            newsTitle = itemView.findViewById(R.id.news2_item_title);
            newsPublisher = itemView.findViewById(R.id.news2_item_name);
            newsTime = itemView.findViewById(R.id.news2_item_time);
            newsImage = itemView.findViewById(R.id.news2_item_image);
        }
        public void setItem(news1_item item){
            newsTitle.setText(item.getTitle());
            newsPublisher.setText(item.getPublisher());
            newsTime.setText(item.getDate());
        }
    }
}
