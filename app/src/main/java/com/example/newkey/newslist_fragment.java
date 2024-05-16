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

public class newslist_fragment extends Fragment {

    private RecyclerView recyclerView;
    private newslist_adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search1, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_SearchNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new newslist_adapter();
        recyclerView.setAdapter(adapter);

        // 테스트 데이터 추가
        adapter.addTestData();

        return view;
    }
}