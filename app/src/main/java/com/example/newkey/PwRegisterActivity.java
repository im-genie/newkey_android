package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class PwRegisterActivity extends AppCompatActivity {
    EditText pw,pwCheck;
    TextView pwRightText, pwSameText;
    ImageView next, pwRightView, pwSameView;
    private StringBuilder url;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    RequestQueue queue;

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

        next=findViewById(R.id.next);
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

                if (pwText.length() >= 6 && pwText.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$")) {
                    pwRightText.setTextColor(getResources().getColor(R.color.key_green_400));
                    pwRightText.setText("올바른 형태의 비밀번호입니다.");
                    pwRightView.setVisibility(View.VISIBLE);
                } else {
                    pwRightText.setTextColor(getResources().getColor(R.color.key_red_100));
                    pwRightText.setText("영어, 숫자 조합 6자리 이상이어야 합니다.");
                    pwRightView.setVisibility(View.INVISIBLE);
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
                    next.setClickable(true);
                } else {
                    pwSameText.setTextColor(getResources().getColor(R.color.key_red_100));
                    pwSameText.setText("비밀번호가 일치하지 않습니다");
                    pwSameView.setVisibility(View.INVISIBLE);
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
    }
}