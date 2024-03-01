package com.example.newkey;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyFragmentAdapter extends FragmentStateAdapter {
    public MyFragmentAdapter(KeywordActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Cardnews1Fragment();
            case 1:
                return new Cardnews2Fragment();
            case 2:
                return new Cardnews2Fragment();
            case 3:
                return new Cardnews2Fragment();
            case 4:
                return new Cardnews2Fragment();
            default:
                return new Cardnews1Fragment(); // 기본값
        }
    }

    @Override
    public int getItemCount() {
        return 5; // 전체 페이지 수
    }
}