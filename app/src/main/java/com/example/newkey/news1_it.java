package com.example.newkey;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class news1_it extends Fragment {
    private RecyclerView recyclerView;
    private news1_adapter adapter;
    private List<news1_item> itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news1_it, container, false);

        recyclerView = view.findViewById(R.id.news1_it_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemList = new ArrayList<>();

        itemList.add(new news1_item("it 기사 1", "언론사 A", "1시간 전", "https://example.com/imageA.jpg"));
        itemList.add(new news1_item("it 기사 2", "언론사 B", "2시간 전", "https://example.com/imageB.jpg"));
        itemList.add(new news1_item("it 기사 1", "언론사 A", "1시간 전", "https://example.com/imageA.jpg"));
        itemList.add(new news1_item("it 기사 2", "언론사 B", "2시간 전", "https://example.com/imageB.jpg"));
        itemList.add(new news1_item("it 기사 1", "언론사 A", "1시간 전", "https://example.com/imageA.jpg"));
        itemList.add(new news1_item("it 기사 2", "언론사 B", "2시간 전", "https://example.com/imageB.jpg"));
        itemList.add(new news1_item("it 기사 1", "언론사 A", "1시간 전", "https://example.com/imageA.jpg"));
        itemList.add(new news1_item("it 기사 2", "언론사 B", "2시간 전", "https://example.com/imageB.jpg"));
        itemList.add(new news1_item("it 기사 1", "언론사 A", "1시간 전", "https://example.com/imageA.jpg"));
        itemList.add(new news1_item("it 기사 2", "언론사 B", "2시간 전", "https://example.com/imageB.jpg"));
        itemList.add(new news1_item("it 기사 1", "언론사 A", "1시간 전", "https://example.com/imageA.jpg"));
        itemList.add(new news1_item("it 기사 2", "언론사 B", "2시간 전", "https://example.com/imageB.jpg"));
        itemList.add(new news1_item("it 기사 1", "언론사 A", "1시간 전", "https://example.com/imageA.jpg"));
        itemList.add(new news1_item("it 기사 2", "언론사 B", "2시간 전", "https://example.com/imageB.jpg"));
        itemList.add(new news1_item("it 기사 1", "언론사 A", "1시간 전", "https://example.com/imageA.jpg"));
        itemList.add(new news1_item("it 기사 2", "언론사 B", "2시간 전", "https://example.com/imageB.jpg"));
        itemList.add(new news1_item("it 기사 1", "언론사 A", "1시간 전", "https://example.com/imageA.jpg"));
        itemList.add(new news1_item("it 기사 2", "언론사 B", "2시간 전", "https://example.com/imageB.jpg"));
        itemList.add(new news1_item("it 기사 1", "언론사 A", "1시간 전", "https://example.com/imageA.jpg"));
        itemList.add(new news1_item("it 기사 2", "언론사 B", "2시간 전", "https://example.com/imageB.jpg"));
        itemList.add(new news1_item("it 기사 1", "언론사 A", "1시간 전", "https://example.com/imageA.jpg"));
        itemList.add(new news1_item("it 기사 2", "언론사 B", "2시간 전", "https://example.com/imageB.jpg"));
        itemList.add(new news1_item("it 기사 1", "언론사 A", "1시간 전", "https://example.com/imageA.jpg"));
        itemList.add(new news1_item("it 기사 2", "언론사 B", "2시간 전", "https://example.com/imageB.jpg"));


        adapter = new news1_adapter(itemList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
