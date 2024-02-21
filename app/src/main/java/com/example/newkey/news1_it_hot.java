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

public class news1_it_hot extends Fragment {
    private RecyclerView recyclerView;
    private news1_hot_news_adapter adapter;
    private List<news1_item> newsItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news1_it_hot, container, false);

        recyclerView = view.findViewById(R.id.news1_it_hot_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        newsItems = new ArrayList<>();

        newsItems.add(new news1_item("제목 1", "", "", "https://file2.nocutnews.co.kr/newsroom/image/2022/08/18/202208181538367694_0.jpg"));
        newsItems.add(new news1_item("제목 2", "", "", "https://file2.nocutnews.co.kr/newsroom/image/2022/08/18/202208181538367694_0.jpg"));
        newsItems.add(new news1_item("제목 3", "", "", "https://file2.nocutnews.co.kr/newsroom/image/2022/08/18/202208181538367694_0.jpg"));

        adapter = new news1_hot_news_adapter(newsItems);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
