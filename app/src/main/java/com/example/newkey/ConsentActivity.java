package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ConsentActivity extends AppCompatActivity {
    ImageView back, nextArrow;
    List<ImageView> imageViewCheckedList;
    List<ImageView> imageViewUncheckedList;
    TextView agreeText;
    androidx.constraintlayout.widget.ConstraintLayout next;
    boolean[] isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        next.setEnabled(false);

        // 텍스트 뷰 초기화
        agreeText = findViewById(R.id.agree_text);
        nextArrow = findViewById(R.id.next_arrow);

        // 이미지 뷰 체크박스 설정
        imageViewCheckedList = new ArrayList<>();
        imageViewUncheckedList = new ArrayList<>();

        // 이미지 뷰 체크박스들을 리스트에 추가
        imageViewCheckedList.add(findViewById(R.id.conditionBoxChecked));
        imageViewCheckedList.add(findViewById(R.id.conditionBoxChecked2));

        imageViewUncheckedList.add(findViewById(R.id.conditionBoxUnchecked));
        imageViewUncheckedList.add(findViewById(R.id.conditionBoxUnchecked2));

        // 체크 상태를 추적하는 배열 초기화
        isChecked = new boolean[imageViewUncheckedList.size()];

        // 초기에는 체크된 이미지를 보이지 않도록 설정
        for (ImageView imageView : imageViewCheckedList) {
            imageView.setVisibility(View.GONE);
        }

        // 초기에는 체크되지 않은 이미지를 보이도록 설정
        for (ImageView imageView : imageViewUncheckedList) {
            imageView.setVisibility(View.VISIBLE);
        }

        // 이미지 뷰 체크박스들에 클릭 리스너 설정
        for (int i = 0; i < imageViewUncheckedList.size(); i++) {
            final int index = i;
            final ImageView imageViewUnchecked = imageViewUncheckedList.get(index);
            final ImageView imageViewChecked = imageViewCheckedList.get(index);

            imageViewUnchecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageViewChecked.setVisibility(View.VISIBLE);
                    imageViewUnchecked.setVisibility(View.GONE);
                    isChecked[index] = true;
                    checkAllChecked();
                }
            });

            imageViewChecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageViewChecked.setVisibility(View.GONE);
                    imageViewUnchecked.setVisibility(View.VISIBLE);
                    isChecked[index] = false;
                    checkAllChecked();
                }
            });
        }
    }

    private void checkAllChecked() {
        for (boolean checked : isChecked) {
            if (!checked) {
                // 하나라도 체크되지 않으면 원래 상태로 돌아감
                next.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray_400)); // 기본 색상으로 설정
                nextArrow.setImageResource(R.drawable.check_gray); // 기본 이미지로 설정
                agreeText.setTextColor(ContextCompat.getColor(this, R.color.gray_300)); // 기본 색상으로 설정
                return;
            }
        }
        // 모든 체크박스가 체크되면 색상 변경
        next.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.key_green_400)); // R.color.key_green_400는 실제 색상 리소스로 대체
        nextArrow.setImageResource(R.drawable.check_black); // 체크된 이미지로 설정
        agreeText.setTextColor(ContextCompat.getColor(this, R.color.gray_600)); // 검은색으로 설정
        next.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
