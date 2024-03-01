package com.example.newkey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<String> newsList; // 예제를 단순화하기 위해 뉴스 제목만 포함하는 리스트

    public NewsAdapter(List<String> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_news, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        String newsTitle = newsList.get(position);
        holder.newsTitleTextView.setText(newsTitle);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitleTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitleTextView = itemView.findViewById(R.id.textView7); // news_item_layout.xml 내의 TextView ID
        }
    }
}

