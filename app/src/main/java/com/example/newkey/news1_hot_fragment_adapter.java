package com.example.newkey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class news1_hot_fragment_adapter extends FragmentStateAdapter {
    public news1_hot_fragment_adapter(AppCompatActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new news1_politics_hot();
            case 1:
                return new news1_economy_hot();
            case 2:
                return new news1_society_hot();
            case 3:
                return new news1_living_hot();
            case 4:
                return new news1_world_hot();
            case 5:
                return new news1_it_hot();
            case 6:
                return new news1_opinion_hot();
            case 7:
                return new news1_sports_hot();
            default:
                return new Fragment(); // 기본값으로 빈 Fragment 반환
        }
    }

    @Override
    public int getItemCount() {
        return 8; // 탭의 총 개수
    }
}
