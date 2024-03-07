package com.example.newkey;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.transition.TransitionManager;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class news3_activity extends AppCompatActivity {

    private ImageView news3back, news3SummaryArrow, news3Scrap;
    private TextView news3SummaryText;
    private FrameLayout summaryButton;
    private CardView summaryCardView;
    private boolean isBookmarked = false; // Tracks whether the news is bookmarked

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news3);

        news3back = findViewById(R.id.news3_back);
        news3SummaryArrow = findViewById(R.id.news3_summary_arrow);
        news3SummaryText=findViewById(R.id.news3_summary_text);
        news3Scrap = findViewById(R.id.news3_scrap);
        summaryButton = findViewById(R.id.new3_summary);
        summaryCardView = findViewById(R.id.summary_cardview);

        news3back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(news3_activity.this, news1_activity.class);
                startActivity(intent);
            }
        });

        summaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition((ViewGroup) summaryCardView.getParent());

                if (summaryCardView.getVisibility() == View.VISIBLE) {
                    summaryCardView.setVisibility(View.GONE);
                    summaryButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_500)));
                    news3SummaryArrow.setImageResource(R.drawable.news3_up);
                    news3SummaryText.setTextColor(getResources().getColor(R.color.white)); // 글씨색 변경 추가
                } else {
                    summaryCardView.setVisibility(View.VISIBLE);
                    summaryButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.key_green_400)));
                    news3SummaryArrow.setImageResource(R.drawable.news3_down);
                    news3SummaryText.setTextColor(getResources().getColor(R.color.gray_600)); // 글씨색 원래대로 변경 추가
                }
            }
        });


        // Set onClickListener for news3Scrap to toggle the bookmark state
        news3Scrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle bookmark state
                isBookmarked = !isBookmarked;

                // Update the ImageView based on whether the news is bookmarked
                if (isBookmarked) {
                    news3Scrap.setImageResource(R.drawable.bookmark_checked);
                } else {
                    news3Scrap.setImageResource(R.drawable.bookmark_unchecked);
                }
            }
        });
    }
}




