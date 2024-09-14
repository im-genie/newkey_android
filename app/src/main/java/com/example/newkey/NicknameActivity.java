package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class NicknameActivity extends AppCompatActivity {
    EditText nickname;
    Button next;
    private SharedPreferences preferences;
    public static final String preference = "newkey";

    ImageView back;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);

        nickname=findViewById(R.id.nickname);
        queue=Volley.newRequestQueue(this);
        next=findViewById(R.id.next);
        preferences=getSharedPreferences(preference, Context.MODE_PRIVATE);

        final TextView textViewCount=findViewById(R.id.textView6);
        next.setEnabled(false);

        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>10){
                    nickname.setText(charSequence.subSequence(0,10));
                    nickname.setSelection(10);
                    Toast.makeText(NicknameActivity.this, "최대 10자까지 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                }
                int length = Math.min(charSequence.length(), 10);
                textViewCount.setText(length + "/10");

                // 글자 수가 1 이상일 때 버튼 활성화
                if (length >= 1) {
                    next.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.key_green_400));
                    next.setTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_600));
                    next.setEnabled(true);
                } else {
                    next.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
                    next.setTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_100));
                    next.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed after text changes
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("nickname", nickname.getText().toString());
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), ChooseTopicsActivity.class);
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