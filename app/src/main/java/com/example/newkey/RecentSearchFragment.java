package com.example.newkey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecentSearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecentSearchAdapter adapter;
    private List<String> recentSearchList;


    public RecentSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search1, container, false);

        // 최근 검색어를 표시할 RecyclerView 초기화
        RecyclerView recyclerViewRecentSearch = view.findViewById(R.id.recyclerViewRecentSearch);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewRecentSearch.setLayoutManager(layoutManager);

        // 최근 검색어 데이터를 가져오거나 초기화
        recentSearchList = new ArrayList<>(); // TODO : 여기에 실제 데이터를 가져와서 넣어주세요

        // 어댑터를 생성하고 RecyclerView에 설정
        adapter = new RecentSearchAdapter(getContext(),recentSearchList);
        recyclerViewRecentSearch.setAdapter(adapter);

        // 테스트 데이터 추가
        addTestData(); // 이 부분 추가

        return view;
    }

    // 모든 검색어 삭제 메소드
    public void clearAllItems() {
        recentSearchList.clear();
        adapter.notifyDataSetChanged();
    }

    // 최근 검색어 테스트 데이터 추가
    public void addTestData() {
        // recentSearchList 객체가 null인지 확인하고 null이면 새로운 ArrayList로 초기화합니다.
        if (recentSearchList == null) {
            recentSearchList = new ArrayList<>();
        }

        // 테스트 검색어를 recentSearchList에 추가합니다.
        recentSearchList.add("테스트 검색어 1");
        recentSearchList.add("테스트 검색어 2");
        recentSearchList.add("테스트 검색어 3");

        // 어댑터에 데이터가 변경되었음을 알리고 RecyclerView를 업데이트합니다.
        adapter.notifyDataSetChanged();
    }


}

