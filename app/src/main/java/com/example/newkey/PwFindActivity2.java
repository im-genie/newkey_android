package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class PwFindActivity2 extends AppCompatActivity {
    EditText pw, pwCheck;
    TextView pwRightText, pwSameText, next;
    StringBuilder url;
    ImageView pwRightView, pwSameView;
    SharedPreferences preferences;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_find2);

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
        queue= Volley.newRequestQueue(this);

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

        // 비밀번호 변경 완료
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = new StringBuilder();
                JSONObject jsonRequest = new JSONObject();
                try {
                    url.append("http://13.124.230.98:8080/user/password");
                    preferences = getSharedPreferences("newkey", MODE_PRIVATE);
                    String email = preferences.getString("email", "");
                    Log.d("changeEmail",email);

                    jsonRequest.put("email", email);
                    jsonRequest.put("password", pw.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url.toString(), jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("asdf", "PwFindActivity2 : 응답 - " + response.toString());

                        try {
                            // 서버 응답에서 필요한 정보 추출
                            boolean isSuccess = response.getBoolean("isSuccess");

                            if (isSuccess) {
                                Toast.makeText(PwFindActivity2.this, "비밀번호가 성공적으로 변경되었습니다", Toast.LENGTH_LONG).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 딜레이 후 동작할 코드
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                    }
                                }, 2000); // 2초 정도 딜레이 준 후 페이지 이동
                            } else {
                                next.setClickable(false);
                                Log.d("asdf", "비밀번호 변경 실패");
                                Toast.makeText(PwFindActivity2.this, "비밀번호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PwFindActivity2.this, "JSON 파싱 오류입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("asdf", "에러뜸!!" + error.toString());
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String errorResponse = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(errorResponse);
                                String errorMessage = jsonObject.getString("errorMessage");
                                // Handle BaseException
                                Log.d("asdf", "BaseException: " + errorMessage);
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                request.setShouldCache(false); //이전 결과가 있어도 새로 요청하여 응답을 보여준다.
                request.setRetryPolicy(new DefaultRetryPolicy(100000000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(request);
            }
        });
    }
}