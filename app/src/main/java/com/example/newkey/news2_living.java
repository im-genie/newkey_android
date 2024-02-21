package com.example.newkey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class news2_living extends AppCompatActivity {
    private ImageView news2Back;
    private RecyclerView recyclerView;
    private news2_adapter adapter;
    private List<news2_item> news2Items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news2_living);

        news2Back = findViewById(R.id.news2_back);
        recyclerView = findViewById(R.id.news2_recyclerview);

        news2Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        news2Items = loadNews2Items();

        adapter = new news2_adapter(news2Items);
        recyclerView.setAdapter(adapter);
    }

    private List<news2_item> loadNews2Items() {
        List<news2_item> items2 = new ArrayList<>();
        items2.add(new news2_item("생활 제목1", "출판사1", "시간1", "https://a-static.besthdwallpaper.com/nct-127-jaehyun-neo-zone-album-shoot-wallpaper-2560x1600-113961_7.jpg"));
        items2.add(new news2_item("생활 제목2", "출판사2", "시간2", "https://a-static.besthdwallpaper.com/nct-127-jaehyun-neo-zone-album-shoot-wallpaper-2560x1600-113961_7.jpg"));
        items2.add(new news2_item("생활 제목1", "출판사1", "시간1", "https://a-static.besthdwallpaper.com/nct-127-jaehyun-neo-zone-album-shoot-wallpaper-2560x1600-113961_7.jpg"));
        items2.add(new news2_item("생활 제목2", "출판사2", "시간2", "https://a-static.besthdwallpaper.com/nct-127-jaehyun-neo-zone-album-shoot-wallpaper-2560x1600-113961_7.jpg"));
        return items2;
    }
}
