package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class PwRegisterActivity extends AppCompatActivity {
    EditText pw,pwCheck;
    TextView pwRightText, pwSameText, nextText;
    ImageView nextArrow, pwRightView, pwSameView, back;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    RequestQueue queue;
    LinearLayout next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_register);

        pw=findViewById(R.id.pw);
        pwCheck=findViewById(R.id.pwCheck);

        pwRightText=findViewById(R.id.pwRightText);
        pwRightText.setTextColor(getResources().getColor(R.color.key_red_100));
        pwRightText.setText("영어, 숫자 조합 6자리 이상이어야 합니다");
        pwRightView=findViewById(R.id.pwRightView);

        pwSameText=findViewById(R.id.pwSameText);
        pwSameText.setTextColor(getResources().getColor(R.color.key_red_100));
        pwSameText.setText("비밀번호가 일치하지 않습니다");
        pwSameView=findViewById(R.id.pwSameView);

        nextText=findViewById(R.id.next_text);
        next=findViewById(R.id.next);
        nextArrow=findViewById(R.id.next_arrow);
        next.setClickable(false);
        queue=Volley.newRequestQueue(this);
        preferences=getSharedPreferences(preference, Context.MODE_PRIVATE);

        pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pwText = pw.getText().toString();

                // 비밀번호가 6자리 이상이고 적절한 형식인 경우
                if (pwText.length() >= 6 && pwText.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$")) {
                    pwRightText.setTextColor(getResources().getColor(R.color.key_green_400));
                    pwRightText.setText("올바른 형태의 비밀번호입니다");
                    pwRightView.setVisibility(View.VISIBLE);

                    if(!pwSameText.getText().equals("") && pwText.equals(pwCheck.getText().toString())) {
                        next.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.key_green_400));
                        nextText.setTextColor(getResources().getColor(R.color.gray_600));
                        nextArrow.setImageResource(R.drawable.next_black);
                        next.setClickable(true);

                        pwSameText.setTextColor(getResources().getColor(R.color.key_green_400));
                        pwSameText.setText("비밀번호가 일치합니다");
                        pwSameView.setVisibility(View.VISIBLE);
                    }
                    else{
                        pwSameText.setTextColor(getResources().getColor(R.color.key_red_100));
                        pwSameText.setText("비밀번호가 일치하지 않습니다");
                        pwSameView.setVisibility(View.INVISIBLE);

                        next.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
                        nextText.setTextColor(getResources().getColor(R.color.gray_100));
                        nextArrow.setImageResource(R.drawable.next);
                        next.setClickable(false);
                    }
                }
                // 비밀번호가 6자리 이상이 아니거나 적절한 형식이 아닌 경우
                else {
                    pwRightText.setTextColor(getResources().getColor(R.color.key_red_100));
                    pwRightText.setText("영어, 숫자 조합 6자리 이상이어야 합니다");
                    pwRightView.setVisibility(View.INVISIBLE);

                    next.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
                    nextText.setTextColor(getResources().getColor(R.color.gray_100));
                    nextArrow.setImageResource(R.drawable.next);
                    next.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pwCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pwText = pw.getText().toString();

                if (!pwText.equals("") && pwText.equals(pwCheck.getText().toString())) {
                    pwSameText.setTextColor(getResources().getColor(R.color.key_green_400));
                    pwSameText.setText("비밀번호가 일치합니다");
                    pwSameView.setVisibility(View.VISIBLE);

                    if(pwText.length() >= 6 && pwText.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$")) {
                        next.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.key_green_400));
                        nextText.setTextColor(getResources().getColor(R.color.gray_600));
                        nextArrow.setImageResource(R.drawable.next_black);
                        next.setClickable(true);
                    }
                    else{
                        pwRightText.setText("영어, 숫자 조합 6자리 이상이어야 합니다");
                        next.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
                        nextText.setTextColor(getResources().getColor(R.color.gray_100));
                        nextArrow.setImageResource(R.drawable.next);
                        next.setClickable(false);
                    }
                } else {
                    pwSameText.setTextColor(getResources().getColor(R.color.key_red_100));
                    pwSameText.setText("비밀번호가 일치하지 않습니다");
                    pwSameView.setVisibility(View.INVISIBLE);
                    next.setClickable(false);

                    next.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
                    nextText.setTextColor(getResources().getColor(R.color.gray_100));
                    nextArrow.setImageResource(R.drawable.next);
                    next.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("pw", pw.getText().toString());
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), NicknameActivity.class);
                startActivity(intent);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}