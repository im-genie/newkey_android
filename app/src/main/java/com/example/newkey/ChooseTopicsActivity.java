package com.example.newkey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ChooseTopicsActivity extends AppCompatActivity {

    HashMap<Integer, Boolean> buttonStates;
    ImageView back;
    List<Topic> topics = new ArrayList<>();
    Button complete;
    HashMap<Integer, Integer> catDict;
    private ArrayList<Integer> catList;
    RequestQueue queue;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_topics);

        queue = Volley.newRequestQueue(getApplicationContext());
        buttonStates=new HashMap<>();
        catList = new ArrayList<>();
        catDict = new HashMap<>();
        catDict.put(R.id.politics_1, 100264); catDict.put(R.id.politics_2, 100265); catDict.put(R.id.politics_3, 100268); catDict.put(R.id.politics_4, 100266); catDict.put(R.id.politics_5, 100267); catDict.put(R.id.politics_6, 100269);
        catDict.put(R.id.economy_1, 101259); catDict.put(R.id.economy_2, 101258); catDict.put(R.id.economy_3, 101261); catDict.put(R.id.economy_4, 101771); catDict.put(R.id.economy_5, 101260); catDict.put(R.id.economy_6, 101262); catDict.put(R.id.economy_7, 101310); catDict.put(R.id.economy_8, 101263);
        catDict.put(R.id.society_1, 102249); catDict.put(R.id.society_2, 102250); catDict.put(R.id.society_3, 102251); catDict.put(R.id.society_4, 102254); catDict.put(R.id.society_5, 102252); catDict.put(R.id.society_6, 102555); catDict.put(R.id.society_7, 102255); catDict.put(R.id.society_8, 102256); catDict.put(R.id.society_9, 102276); catDict.put(R.id.society_10, 102257);
        catDict.put(R.id.life_1, 103241); catDict.put(R.id.life_2, 103239); catDict.put(R.id.life_3, 103240); catDict.put(R.id.life_4, 103237); catDict.put(R.id.life_5, 103238); catDict.put(R.id.life_6, 103376); catDict.put(R.id.life_7, 103242); catDict.put(R.id.life_8, 103243); catDict.put(R.id.life_9, 103244); catDict.put(R.id.life_10, 103248); catDict.put(R.id.life_11, 103245);
        catDict.put(R.id.global_1,104231); catDict.put(R.id.global_2,104232); catDict.put(R.id.global_3,104233); catDict.put(R.id.global_4,104234); catDict.put(R.id.global_5,104322);
        catDict.put(R.id.it_1,105731); catDict.put(R.id.it_2,105226); catDict.put(R.id.it_3,105227); catDict.put(R.id.it_4,105230); catDict.put(R.id.it_5,105732); catDict.put(R.id.it_6,105283); catDict.put(R.id.it_7,105229); catDict.put(R.id.it_8,105228);
        catDict.put(R.id.opinion_1,110111); catDict.put(R.id.opinion_2,110112); catDict.put(R.id.opinion_3,110113);
        catDict.put(R.id.sports_1,120121); catDict.put(R.id.sports_2,120122); catDict.put(R.id.sports_3,120123); catDict.put(R.id.sports_4,120124); catDict.put(R.id.sports_5,120125); catDict.put(R.id.sports_6,120126); catDict.put(R.id.sports_7,120127); catDict.put(R.id.sports_8,120128);
        catDict.put(R.id.entertain_1,130131); catDict.put(R.id.entertain_2,130132);

        initializeTopics();
        initializeButtons();
        setupResetButton();

        String registerUrl="http://15.164.199.177:5000/register";
        String attentionUrl="http://15.164.199.177:5000/selCat";

        preferences=getSharedPreferences(preference, MODE_PRIVATE);
        String email=preferences.getString("email", null);

        complete = findViewById(R.id.completeButton);
        complete.setClickable(false);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("join").equals("1")){

                    for (Integer buttonId : buttonStates.keySet()) {
                        boolean isActive = buttonStates.get(buttonId);
                        if (isActive) {
                            // Button이 활성화 상태인 경우, 해당 buttonId에 매칭되는 catDict의 key를 찾아서 catList에 추가
                            Integer key = getKey(catDict, buttonId);

                            if (key != null) {
                                Integer value = catDict.get(key);
                                catList.add(value);
                            }
                        }
                    }

                    final StringRequest request=new StringRequest(Request.Method.POST, registerUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("res",response);

                            Intent intent=new Intent(ChooseTopicsActivity.this,LogoActivity.class);
                            startActivity(intent);
                            Toast.makeText(ChooseTopicsActivity.this, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("회원가입 오류",error.toString());
                            Toast.makeText(ChooseTopicsActivity.this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        //@Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("user_id", email); // 로그인 아이디로 바꾸기
                            params.put("click_news", "[]");
                            params.put("select_cat", catList.toString()); // 사용자가 선택한 선호 카테고리로 바꾸기
                            params.put("stored_news", "[]");
                            params.put("search", "[]");

                            return params;
                        }
                    };
                    request.setRetryPolicy(new DefaultRetryPolicy(
                            1000000,  // 기본 타임아웃 (기본값: 2500ms)
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));

                    request.setShouldCache(false);
                    queue.add(request);
                }

                else if(getIntent().getStringExtra("join").equals("0")) {

                    for (Integer buttonId : buttonStates.keySet()) {
                        boolean isActive = buttonStates.get(buttonId);
                        if (isActive) {
                            // Button이 활성화 상태인 경우, 해당 buttonId에 매칭되는 catDict의 key를 찾아서 catList에 추가
                            Integer key = getKey(catDict, buttonId);

                            if (key != null) {
                                Integer value = catDict.get(key);
                                catList.add(value);
                            }
                        }
                    }

                    final StringRequest request=new StringRequest(Request.Method.POST, attentionUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("res",response);

                            Intent intent=new Intent(ChooseTopicsActivity.this,MypageActivity.class);
                            startActivity(intent);
                            Toast.makeText(ChooseTopicsActivity.this, "관심사 설정을 완료했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("관심사 오류",error.toString());
                            Toast.makeText(ChooseTopicsActivity.this, "관심사 설정에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        //@Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("user_id", email); // 로그인 아이디로 바꾸기
                            params.put("select_cat", catList.toString()); // 사용자가 선택한 선호 카테고리로 바꾸기

                            return params;
                        }
                    };
                    request.setRetryPolicy(new DefaultRetryPolicy(
                            1000000,  // 기본 타임아웃 (기본값: 2500ms)
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));

                    request.setShouldCache(false);
                    queue.add(request);
                }
            }
        });
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // buttonId에 해당하는 key를 찾기 위한 함수
    private Integer getKey(Map<Integer, Integer> map, Integer value) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (Objects.equals(entry.getKey(), value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void initializeTopics() {
        Object[][] topicResources = {
                {R.id.btn_politics, R.id.small_topics_politics, R.id.text_politics, R.id.img_politics, Arrays.asList(R.id.politics_1, R.id.politics_2, R.id.politics_3, R.id.politics_4, R.id.politics_5, R.id.politics_6)},
                {R.id.btn_economy, R.id.small_topics_economy, R.id.text_economy, R.id.img_economy, Arrays.asList(R.id.economy_1,R.id.economy_2,R.id.economy_3,R.id.economy_4,R.id.economy_5,R.id.economy_6,R.id.economy_7,R.id.economy_8)},
                {R.id.btn_society, R.id.small_topics_society, R.id.text_society, R.id.img_society, Arrays.asList(R.id.society_1, R.id.society_2, R.id.society_3, R.id.society_4, R.id.society_5, R.id.society_6, R.id.society_7,R.id.society_8, R.id.society_9, R.id.society_10)},
                {R.id.btn_life, R.id.small_topics_life, R.id.text_life, R.id.img_life, Arrays.asList(R.id.life_1, R.id.life_2, R.id.life_3, R.id.life_4, R.id.life_5, R.id.life_6, R.id.life_7, R.id.life_8, R.id.life_9, R.id.life_10, R.id.life_11)},
                {R.id.btn_it, R.id.small_topics_it, R.id.text_it, R.id.img_it, Arrays.asList(R.id.it_1, R.id.it_2, R.id.it_3, R.id.it_4, R.id.it_5, R.id.it_6,R.id.it_7,R.id.it_8)},
                {R.id.btn_global, R.id.small_topics_global, R.id.text_global, R.id.img_global, Arrays.asList(R.id.global_1, R.id.global_2, R.id.global_3, R.id.global_4, R.id.global_5)},
                {R.id.btn_opinion, R.id.small_topics_opinion, R.id.text_opinion, R.id.img_opinion, Arrays.asList(R.id.opinion_1, R.id.opinion_2, R.id.opinion_3)},
                {R.id.btn_entertain, R.id.small_topics_entertain, R.id.text_entertain, R.id.img_entertain, Arrays.asList(R.id.entertain_1, R.id.entertain_2)},
                {R.id.btn_sports, R.id.small_topics_sports, R.id.text_sports, R.id.img_sports, Arrays.asList(R.id.sports_1, R.id.sports_2, R.id.sports_3, R.id.sports_4, R.id.sports_5, R.id.sports_6, R.id.sports_7, R.id.sports_8)}
        };

        for (Object[] res : topicResources) {
            topics.add(new Topic(
                    (ConstraintLayout) findViewById((Integer)res[0]),
                    (ConstraintLayout) findViewById((Integer)res[1]),
                    (TextView) findViewById((Integer)res[2]),
                    (ImageView) findViewById((Integer)res[3]),
                    (List<Integer>) res[4]
            ));
        }

        for (Topic topic : topics) {
            topic.btnLayout.setOnClickListener(v -> topic.toggleState(this));
        }
    }

    private void initializeButtons() {
        int[] buttonIds = {R.id.politics_1, R.id.politics_2, R.id.politics_3, R.id.politics_4, R.id.politics_5, R.id.politics_6,
                R.id.economy_1,R.id.economy_2,R.id.economy_3,R.id.economy_4,R.id.economy_5,R.id.economy_6,R.id.economy_7,R.id.economy_8,
                R.id.society_1, R.id.society_2, R.id.society_3, R.id.society_4, R.id.society_5, R.id.society_6, R.id.society_7,R.id.society_8, R.id.society_9, R.id.society_10,
                R.id.life_1, R.id.life_2, R.id.life_3, R.id.life_4, R.id.life_5, R.id.life_6, R.id.life_7, R.id.life_8, R.id.life_9, R.id.life_10, R.id.life_11,
                R.id.it_1, R.id.it_2, R.id.it_3, R.id.it_4, R.id.it_5, R.id.it_6,R.id.it_7,R.id.it_8,
                R.id.global_1, R.id.global_2, R.id.global_3, R.id.global_4, R.id.global_5,
                R.id.opinion_1, R.id.opinion_2, R.id.opinion_3,
                R.id.entertain_1, R.id.entertain_2,
                R.id.sports_1, R.id.sports_2, R.id.sports_3, R.id.sports_4, R.id.sports_5, R.id.sports_6, R.id.sports_7, R.id.sports_8};

        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            buttonStates.put(buttonId, false);

            button.setOnClickListener(v -> {
                boolean isActive = buttonStates.get(buttonId);
                buttonStates.put(buttonId, !isActive);

                if (!isActive) {
                    button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.key_yellow_300));
                    button.setTextColor(ContextCompat.getColor(this, R.color.gray_600));

                    cnt++;
                    if(cnt >= 1) {
                        complete.setClickable(true);
                        complete.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.key_green_400));
                    }
                } else {
                    button.setBackgroundTintList(null);
                    button.setBackgroundResource(R.drawable.radius_100);
                    button.setTextColor(ContextCompat.getColor(this, R.color.gray_100));

                    cnt--;
                    if(cnt == 0) {
                        complete.setClickable(false);
                        complete.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
                    }
                }
            });
        }
    }


    class Topic {
        ConstraintLayout btnLayout;
        ConstraintLayout smallTopicsLayout;
        TextView textView;
        ImageView imageView;
        List<Integer> childButtonIds;

        boolean isActive = false;

        public Topic(ConstraintLayout btnLayout, ConstraintLayout smallTopicsLayout, TextView textView, ImageView imageView, List<Integer> childButtonIds) {
            this.btnLayout = btnLayout;
            this.smallTopicsLayout = smallTopicsLayout;
            this.textView = textView;
            this.imageView = imageView;
            this.childButtonIds = childButtonIds;
        }

        public void toggleState(Context context) {
            isActive = !isActive;

            if (isActive) {
                btnLayout.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.key_yellow_400));
                textView.setTextColor(ContextCompat.getColor(context, R.color.gray_600));
                imageView.setImageResource(R.drawable.add);
                smallTopicsLayout.setVisibility(View.VISIBLE);
            } else {
                btnLayout.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray_500));
                textView.setTextColor(ContextCompat.getColor(context, R.color.gray_100));
                imageView.setImageResource(R.drawable.add_white);
                smallTopicsLayout.setVisibility(View.GONE);
                disableChildButtons(context);
            }
        }

        private void disableChildButtons(Context context) {
            for (int childButtonId : childButtonIds) {
                Button childButton = ((Activity)context).findViewById(childButtonId);
                childButton.setBackgroundTintList(null);
                childButton.setBackgroundResource(R.drawable.radius_100);
                childButton.setTextColor(ContextCompat.getColor(context, R.color.gray_100));
                // 하위 버튼의 상태를 변경하는 코드 (예: buttonStates HashMap을 사용하는 경우)
                ((ChooseTopicsActivity)context).buttonStates.put(childButtonId, false);
            }
        }
    }

    private void setupResetButton() {
        TextView resetButton = findViewById(R.id.reset);
        resetButton.setOnClickListener(v -> resetAllTopics());
    }

    private void resetAllTopics() {
        for (Topic topic : topics) {
            // 각 Topic의 상태를 비활성화 상태로 변경
            if (topic.isActive) { // 활성 상태인 경우에만 처리
                topic.toggleState(this);
            }
        }

        for (Integer buttonId : buttonStates.keySet()) {
            Button button = findViewById(buttonId);
            button.setBackgroundTintList(null);
            button.setBackgroundResource(R.drawable.radius_100);
            button.setTextColor(ContextCompat.getColor(this, R.color.gray_100));
            buttonStates.put(buttonId, false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}