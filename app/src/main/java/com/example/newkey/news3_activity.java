package com.example.newkey;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.transition.TransitionManager;
import androidx.constraintlayout.widget.ConstraintLayout;

public class news3_activity extends AppCompatActivity {

    private ImageView news3back;
    private Button summaryButton; // 기사요약 버튼
    private CardView summaryCardView; // 요약 정보를 담은 CardView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news3);

        news3back = findViewById(R.id.news3_back);
        news3back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(news3_activity.this, news1_activity.class);
                startActivity(intent);
            }
        });

        summaryButton = findViewById(R.id.new3_summary);
        summaryCardView = findViewById(R.id.summary_cardview);

        summaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition((ViewGroup) summaryCardView.getParent());

                if (summaryCardView.getVisibility() == View.VISIBLE) {
                    summaryCardView.setVisibility(View.GONE);
                    summaryButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_500)));
                } else {
                    summaryCardView.setVisibility(View.VISIBLE);
                    summaryButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.key_green_400)));
                }
            }
        });

    }
}
