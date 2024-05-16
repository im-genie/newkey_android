package com.example.newkey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class newslist_adapter extends RecyclerView.Adapter<newslist_adapter.NewsViewHolder> {

    private List<NewsItem> newsItemList = new ArrayList<>();

    public void addItem(NewsItem item) {
        newsItemList.add(item);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem item = newsItemList.get(position);
        holder.newsTitle.setText(item.getTitle());
        holder.newsPublisher.setText(item.getPublisher());
        holder.newsTime.setText(item.getTime());
        // 이미지 설정 등 추가 작업 필요
    }

    @Override
    public int getItemCount() {
        return newsItemList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView newsTitle, newsPublisher, newsTime;
        public ImageView newsImage;

        public NewsViewHolder(View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.item_search_title);
            newsPublisher = itemView.findViewById(R.id.item_search_name);
            newsTime = itemView.findViewById(R.id.item_search_time);
            newsImage = itemView.findViewById(R.id.item_search_image);
        }
    }

    public void addTestData() {
        for (int i = 0; i < 10; i++) {
            NewsItem item = new NewsItem("테스트 제목 " + (i + 1), "출판사 " + (i + 1), "시간 " + (i + 1), null);
            addItem(item);
        }
        notifyDataSetChanged(); // 데이터가 변경되었음을 어댑터에 알립니다.
    }
}
