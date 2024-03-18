package com.example.newkey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class newslist_adapter extends RecyclerView.Adapter<newslist_adapter.NewsViewHolder> {

    // 데이터 리스트를 여기에 추가해야 합니다.

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        // 데이터를 뷰홀더에 바인딩하는 코드를 여기에 추가해야 합니다.
    }

    @Override
    public int getItemCount() {
        // 데이터 리스트의 크기를 반환해야 합니다.
        return 0; // 현재는 데이터가 없기 때문에 0을 반환합니다.
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
}
