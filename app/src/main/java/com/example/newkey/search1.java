package com.example.newkey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // 검색 기능 구현 코드
        search = findViewById(R.id.view_search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchText = query.trim();
                performSearch(searchText); // performSearch 메소드 호출
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Text가 변할 때 이벤트 처리를 원한다면 이 곳에 구현
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

        // TODO: 검색 결과를 처리하고 표시하는 코드 추가

        // NewsListFragment를 표시하기 위한 코드
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // NewsListFragment를 생성하고 검색 결과를 전달할 수 있도록 Bundle에 담아 전달합니다.
        newslist_fragment newsListFragment = new newslist_fragment();
        Bundle bundle = new Bundle();
        bundle.putString("searchText", searchText);
        newsListFragment.setArguments(bundle);

        transaction.replace(R.id.fragment_recentsearch, newsListFragment);
        transaction.addToBackStack(null);  // 뒤로 가기 버튼을 눌렀을 때 이전 Fragment로 돌아갈 수 있도록 합니다.

        transaction.commit();
    }

    // 최근 검색어 테스트 데이터 추가
    private void addTestDataToRecentSearch() {
        RecentSearchFragment fragment = (RecentSearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_recentsearch);
        if (fragment != null) {
            fragment.addTestData(); // RecentSearchFragment에서 테스트 데이터를 추가하는 메소드를 호출합니다.
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        addTestDataToRecentSearch();
    }

}
