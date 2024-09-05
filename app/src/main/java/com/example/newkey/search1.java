package com.example.newkey;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class search1 extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private androidx.appcompat.widget.SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search1);

        // 뒤로가기 버튼
        ImageButton button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 검색 기능 구현 코드
        search = findViewById(R.id.view_search);

        // SearchView 내부의 TextView 찾아서 텍스트 색상 변경
        int searchTextViewId = search.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchTextView = search.findViewById(searchTextViewId);
        searchTextView.setTextColor(Color.WHITE);

        // 텍스트 색상 강제로 흰색으로 고정
        if (searchTextView != null) {
            searchTextView.setTextColor(Color.WHITE);  // 텍스트 색상을 흰색으로 설정
            searchTextView.setHintTextColor(Color.GRAY);  // 힌트 텍스트 색상도 설정 가능

            // 포커스가 변경될 때마다 색상 다시 설정
            searchTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        searchTextView.setTextColor(Color.WHITE);  // 항상 흰색 유지
                    }
                }
            });
        }

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchText = query.trim();
                performSearch(searchText); // performSearch 메소드 호출
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Text가 변할 때마다 텍스트 색상을 다시 흰색으로 고정
                if (searchTextView != null) {
                    searchTextView.setTextColor(Color.WHITE);  // 항상 흰색 유지
                }
                return false;
            }
        });

        // 전체삭제 버튼
        Button btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragment_recentsearch에서 전체 삭제 로직 추가
                RecentSearchFragment fragment = (RecentSearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_recentsearch);
                if (fragment != null) {
                    fragment.clearAllItems();
                }
            }
        });

        // 최근 검색어를 표시할 Fragment 추가
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        RecentSearchFragment recentSearchFragment = new RecentSearchFragment();
        transaction.replace(R.id.fragment_recentsearch, recentSearchFragment);

        transaction.commit();
    }

    // 검색 기능 메소드
    private void performSearch(String searchText) {
        // 검색 결과가 없는 경우 처리
        if (searchText.isEmpty()) {
            Toast.makeText(this, "해당하는 내용이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: 검색어를 받아서, 실제로 데이터를 검색하고, 검색 결과를 화면에 표시하는 로직을 추가해야 함.

        // NewsListFragment를 표시하기 위한 코드
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // NewsListFragment를 생성하고 검색 결과를 전달할 수 있도록 Bundle에 담아 전달합니다.
        newslist_fragment newsListFragment = new newslist_fragment();
        Bundle bundle = new Bundle();
        bundle.putString("searchText", searchText);
        newsListFragment.setArguments(bundle);

        // 현재 newslist_fragment가 화면에 표시되어 있다면 제거합니다.
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_recentsearch);
        if (currentFragment instanceof newslist_fragment) {
            transaction.remove(currentFragment);
        }

        // recyclerView_SearchNews를 표시하기 위해 해당 View를 화면에 표시합니다.
        RecyclerView recyclerViewSearchNews = findViewById(R.id.recyclerView_SearchNews);
        recyclerViewSearchNews.setVisibility(View.VISIBLE);

        transaction.commit();
    }

    // 최근 검색어 테스트 데이터 추가
    private void addTestDataToRecentSearch() {
        RecentSearchFragment fragment = (RecentSearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_recentsearch);
        if (fragment != null) {
            fragment.addTestData(); // RecentSearchFragment에서 테스트 데이터를 추가하는 메소드를 호출합니다.
        }
    }

    // activity가 활성화될 때마다 최근 검색어에 테스트 데이터가 추가되도록 함.
    @Override
    protected void onStart() {
        super.onStart();
        addTestDataToRecentSearch();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
