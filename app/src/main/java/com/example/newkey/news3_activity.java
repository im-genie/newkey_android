package com.example.newkey;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

public class news3_activity extends AppCompatActivity {

    private ImageView news3back, news3SummaryArrow;
    private FrameLayout summaryButton;
    private CardView summaryCardView;
    TextView Title,Content,Date;
    ImageView Img;
    RequestQueue queue;
    String fiveWOneHUrl="http://15.164.210.22:5000/5w1h";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news3);

        news3back = findViewById(R.id.news3_back);
        news3SummaryArrow = findViewById(R.id.news3_summary_arrow);
        summaryButton = findViewById(R.id.new3_summary);
        summaryCardView = findViewById(R.id.summary_cardview);

        Title=findViewById(R.id.title);
        Content=findViewById(R.id.content);
        Date=findViewById(R.id.date);
        Img=findViewById(R.id.newsImg);
        queue=Volley.newRequestQueue(getApplicationContext());

        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String date = getIntent().getStringExtra("date");
        String summary = getIntent().getStringExtra("summary");
        String imgUrl = getIntent().getStringExtra("img");

        Title.setText(title);
        Content.setText(content);
        Date.setText(date);

        final ImageRequest imageRequest = new ImageRequest(imgUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Log.d("test","image get");
                try {
                    Img.setImageBitmap(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },
                500, 500, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 처리
                System.out.println(error);
            }
        });

        imageRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000000,  // 기본 타임아웃 (기본값: 2500ms)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        imageRequest.setShouldCache(false);
        queue.add(imageRequest);

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
                } else {
                    summaryCardView.setVisibility(View.VISIBLE);
                    summaryButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.key_green_400)));
                    news3SummaryArrow.setImageResource(R.drawable.news3_down);
                }
            }
        });
    }
}


