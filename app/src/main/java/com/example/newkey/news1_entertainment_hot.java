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

public class news1_entertainment_hot extends Fragment {
    private RecyclerView recyclerView;
    private news1_hot_news_adapter adapter;
    private List<news1_item> newsItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news1_entertainment_hot, container, false);

        recyclerView = view.findViewById(R.id.news1_entertainment_hot_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        newsItems = new ArrayList<>();

        newsItems.add(new news1_item("제목1", "", "", "https://cdnweb01.wikitree.co.kr/webdata/editor/202005/20/img_20200520151750_336edb31.webp"));
        newsItems.add(new news1_item("제목2", "", "", "https://cdnweb01.wikitree.co.kr/webdata/editor/202005/20/img_20200520151750_336edb31.webp"));
        newsItems.add(new news1_item("제목3", "", "", "https://cdnweb01.wikitree.co.kr/webdata/editor/202005/20/img_20200520151750_336edb31.webp"));

        adapter = new news1_hot_news_adapter(newsItems);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
