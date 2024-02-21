package com.example.newkey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class news1_fragment_adapter extends FragmentStateAdapter {
    public news1_fragment_adapter(AppCompatActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new news1_politics();
            case 1:
                return new news1_economy();
            case 2:
                return new news1_society();
            case 3:
                return new news1_living();
            case 4:
                return new news1_world();
            case 5:
                return new news1_it();
            case 6:
                return new news1_opinion();
            case 7:
                return new news1_sports();
            default:
                return new Fragment(); // 기본값으로 빈 Fragment 반환
        }
    }

    @Override
    public int getItemCount() {
        return 8; // 탭의 총 개수
    }
}
