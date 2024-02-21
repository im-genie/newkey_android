package com.example.newkey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class news1_sports_hot extends Fragment {
    private RecyclerView recyclerView;
    private news1_hot_news_adapter adapter;
    private List<news1_item> newsItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news1_sports_hot, container, false);

        recyclerView = view.findViewById(R.id.news1_sports_hot_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        newsItems = new ArrayList<>();

        newsItems.add(new news1_item("제목 1", "", "", "https://image.static.bstage.in/cdn-cgi/image/metadata=none,dpr=2,width=490,height=200/newsjamm/a63de174-3a77-4144-a277-8b8a818e62d0/b458a76e-b374-48a1-9b74-05f32d4533df/ori.jpg"));
        newsItems.add(new news1_item("제목 2", "", "", "https://image.static.bstage.in/cdn-cgi/image/metadata=none,dpr=2,width=490,height=200/newsjamm/a63de174-3a77-4144-a277-8b8a818e62d0/b458a76e-b374-48a1-9b74-05f32d4533df/ori.jpg"));
        newsItems.add(new news1_item("제목 3", "", "", "https://image.static.bstage.in/cdn-cgi/image/metadata=none,dpr=2,width=490,height=200/newsjamm/a63de174-3a77-4144-a277-8b8a818e62d0/b458a76e-b374-48a1-9b74-05f32d4533df/ori.jpg"));

        adapter = new news1_hot_news_adapter(newsItems);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
