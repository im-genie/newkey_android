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
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class PwRegisterActivity extends AppCompatActivity {
    EditText pw,pwCheck;
    TextView pwRightText,pwSameText;
    Button next;
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
        pwSameText=findViewById(R.id.pwSameText);
        pwSameText.setVisibility(View.VISIBLE);
        pwSameText.setTextColor(getResources().getColor(R.color.red));
        pwSameText.setText("비밀번호가 일치하지 않습니다");
        next=findViewById(R.id.next);
        next.setClickable(false);
        queue= Volley.newRequestQueue(this);
        preferences=getSharedPreferences(preference, Context.MODE_PRIVATE);

        pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!pw.getText().toString().equals("") && pw.getText().toString().equals(pwCheck.getText().toString())) {
                    pwSameText.setTextColor(getResources().getColor(R.color.green));
                    pwSameText.setText("비밀번호가 일치합니다");
                    next.setClickable(true);
                } else {
                    pwSameText.setTextColor(getResources().getColor(R.color.red));
                    pwSameText.setText("비밀번호가 일치하지 않습니다");
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
                if (!pw.getText().toString().equals("") && pw.getText().toString().equals(pwCheck.getText().toString())) {
                    pwSameText.setTextColor(getResources().getColor(R.color.green));
                    pwSameText.setText("비밀번호가 일치합니다");
                    next.setClickable(true);
                } else {
                    pwSameText.setTextColor(getResources().getColor(R.color.red));
                    pwSameText.setText("비밀번호가 일치하지 않습니다");
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