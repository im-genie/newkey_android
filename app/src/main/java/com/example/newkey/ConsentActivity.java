package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ConsentActivity extends AppCompatActivity {
    ImageView back, next;
    List<ImageView> imageViewCheckedList;
    List<ImageView> imageViewUncheckedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogoActivity.class);
                startActivity(intent);
            }
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EmailRegisterActivity1.class);
                startActivity(intent);
            }
        });

        // 이미지 뷰 체크박스 설정
        imageViewCheckedList = new ArrayList<>();
        imageViewUncheckedList = new ArrayList<>();

        // 이미지 뷰 체크박스들을 리스트에 추가
        imageViewCheckedList.add(findViewById(R.id.conditionBoxChecked));
        imageViewCheckedList.add(findViewById(R.id.conditionBoxChecked2));

        imageViewUncheckedList.add(findViewById(R.id.conditionBoxUnchecked));
        imageViewUncheckedList.add(findViewById(R.id.conditionBoxUnchecked2));

        // 초기에는 체크된 이미지를 보이지 않도록 설정
        for (ImageView imageView : imageViewCheckedList) {
            imageView.setVisibility(View.GONE);
        }

        // 초기에는 체크되지 않은 이미지를 보이도록 설정
        for (ImageView imageView : imageViewUncheckedList) {
            imageView.setVisibility(View.VISIBLE);
        }

        // 이미지 뷰 체크박스들에 클릭 리스너 설정
        for (final ImageView imageViewUnchecked : imageViewUncheckedList) {
            imageViewUnchecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 클릭된 이미지 뷰를 찾아 체크 상태를 변경
                    int index = imageViewUncheckedList.indexOf(imageViewUnchecked);
                    ImageView imageViewChecked = imageViewCheckedList.get(index);

                    imageViewChecked.setVisibility(View.VISIBLE);
                    imageViewUnchecked.setVisibility(View.GONE);
                }
            });
        }

        for (final ImageView imageViewChecked : imageViewCheckedList) {
            imageViewChecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 클릭된 이미지 뷰를 찾아 체크 상태를 변경
                    int index = imageViewCheckedList.indexOf(imageViewChecked);
                    ImageView imageViewUnchecked = imageViewUncheckedList.get(index);

                    imageViewChecked.setVisibility(View.GONE);
                    imageViewUnchecked.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}