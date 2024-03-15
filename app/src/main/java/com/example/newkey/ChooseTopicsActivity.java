package com.example.newkey;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ChooseTopicsActivity extends AppCompatActivity {

    private HashMap<Integer, Boolean> buttonStates = new HashMap<>();
    private List<Topic> topics = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_topics);

        initializeTopics();
        initializeButtons();
        setupResetButton();
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
                {R.id.btn_sports, R.id.small_topics_sports, R.id.text_sports, R.id.img_sports, Arrays.asList(R.id.sports_1, R.id.sports_2, R.id.sports_3, R.id.sports_4, R.id.sports_5, R.id.sports_6, R.id.sports_7, R.id.sports_8, R.id.sports_9)}
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
                R.id.sports_1, R.id.sports_2, R.id.sports_3, R.id.sports_4, R.id.sports_5, R.id.sports_6, R.id.sports_7, R.id.sports_8, R.id.sports_9};

        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            buttonStates.put(buttonId, false);

            button.setOnClickListener(v -> {
                boolean isActive = buttonStates.get(buttonId);
                buttonStates.put(buttonId, !isActive);

                if (!isActive) {
                    button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.key_yellow_300));
                    button.setTextColor(ContextCompat.getColor(this, R.color.gray_600));
                } else {
                    button.setBackgroundTintList(null);
                    button.setBackgroundResource(R.drawable.radius_100);
                    button.setTextColor(ContextCompat.getColor(this, R.color.gray_100));
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
}
