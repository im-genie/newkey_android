package com.example.newkey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeIdActivity extends Activity {
    private EditText editUserId;
    private TextView textViewCount;
    RequestQueue queue;
    String email;
    private SharedPreferences preferences;
    public static final String preference = "newkey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_id); // 레이아웃 설정

        // 컴포넌트 초기화
        editUserId = findViewById(R.id.editUserId);
        LinearLayout backFromProfile = findViewById(R.id.backFromProfile);
        Button completeButton = findViewById(R.id.completeButton);
        textViewCount = findViewById(R.id.textView6);

        preferences=getSharedPreferences(preference, Context.MODE_PRIVATE);
        email=preferences.getString("email", null);

        String url="http://43.201.113.167:8080/user/nameSave";
        queue=Volley.newRequestQueue(getApplicationContext());

        // 글자 수 카운트와 제한 설정
        editUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 글자 수 카운트
                if (charSequence.length() > 10) {
                    editUserId.setText(charSequence.subSequence(0, 10));  // 10자를 초과하면 자름
                    editUserId.setSelection(10);  // 커서를 맨 뒤로 이동
                    Toast.makeText(ChangeIdActivity.this, "최대 10자까지 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                }
                // 글자 수가 10자를 넘지 않도록 제한하여 카운트 표시
                int length = Math.min(charSequence.length(), 10);
                textViewCount.setText(length + "/10");

                // 글자 수가 1 이상일 때 버튼 활성화
                if (length >= 1) {
                    completeButton.setEnabled(true);
                    completeButton.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.key_green_400));
                } else {
                    completeButton.setEnabled(false);
                    completeButton.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed after text changes
            }
        });

        // 이미지 버튼 클릭 이벤트 처리: 화면을 닫고 이전 화면으로 돌아감
        backFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 액티비티를 종료
                finish();
            }
        });

        // 완료 버튼 클릭 이벤트 처리: 사용자 입력을 저장하고 MainActivity로 전달
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 변경한 이름 저장
                JSONObject jsonRequest = new JSONObject();
                try {
                    jsonRequest.put("email", email);
                    jsonRequest.put("name", editUserId.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("success!!", response.toString());

                        Toast.makeText(getApplicationContext(), "별명 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(getApplicationContext(), "별명 변경이 실패했습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("test", "Nickname error: " + error.toString());
                    }
                });

                //이 부분에 추가
                request.setRetryPolicy(new DefaultRetryPolicy(
                        100000000,  // 기본 타임아웃 (기본값: 2500ms)
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));

                request.setShouldCache(false);
                queue.add(request);

                Intent intent = new Intent(ChangeIdActivity.this, MypageActivity.class);
                startActivity(intent);
            }
        });}}

