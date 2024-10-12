package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class EmailRegisterActivity2 extends AppCompatActivity {
    EditText code;
    Button codeCheck;
    private ImageView nextArrow, back;
    TextView codeRightText,nextText;
    private StringBuilder url;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    RequestQueue queue;
    LinearLayout next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register2);

        code = findViewById(R.id.code);
        codeCheck = findViewById(R.id.codeCheck);
        codeRightText = findViewById(R.id.codeRightText);
        next = findViewById(R.id.next);
        nextText=findViewById(R.id.next_text);
        next=findViewById(R.id.next);
        nextArrow=findViewById(R.id.next_arrow);
        preferences = getSharedPreferences(preference, Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(this);

        next.setEnabled(false);

        // Initially disable the codeCheck button
        codeCheck.setEnabled(false);
//        codeCheck.setBackgroundColor(Color.GRAY); // Set initial color to gray

        // Add TextWatcher to EditText to monitor text changes
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Enable the button and change color if text length is 6 or more
                if (charSequence.length() == 6) {
                    codeCheck.setEnabled(true);
                    codeCheck.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.key_green_400));
                    codeCheck.setTextColor(ContextCompat.getColor(EmailRegisterActivity2.this, R.color.gray_600));
                } else {
                    codeCheck.setEnabled(false);
                    codeCheck.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
                    codeCheck.setTextColor(ContextCompat.getColor(EmailRegisterActivity2.this, R.color.gray_300));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed after text changes
            }
        });

        // 인증코드 확인 버튼
        codeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = new StringBuilder();
                JSONObject jsonRequest = new JSONObject();

                try {
                    url.append("http://43.201.113.167:8080/user/emails/verifications"); //43.201.113.167

                    String correctCode = preferences.getString("correctCode", "");
                    jsonRequest.put("correctCode", correctCode);
                    jsonRequest.put("inputCode", code.getText().toString());
                    Log.d("test", "EmailRegisterActivity2 : 입력한 인증코드 - " + code.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("test", "EmailRegisterActivity2 : 응답 - " + response.toString());

                        try {
                            // 서버 응답에서 필요한 정보 추출
                            boolean isSuccess = response.getBoolean("isSuccess");

                            if (isSuccess) {
                                JSONObject result = response.getJSONObject("result");
                                boolean isCorrected = result.getBoolean("isCorrected");

                                // 인증코드 맞는 경우
                                if (isCorrected) {
                                    codeRightText.setTextColor(getResources().getColor(R.color.key_green_400));
                                    codeRightText.setText("인증이 완료되었습니다");

                                    nextText.setTextColor(getResources().getColor(R.color.gray_600));
                                    nextArrow.setImageResource(R.drawable.next_black);
                                    next.setEnabled(true);
                                } else {
                                    nextText.setTextColor(getResources().getColor(R.color.gray_100));
                                    nextArrow.setImageResource(R.drawable.next);
                                    next.setEnabled(false);

                                    Toast.makeText(EmailRegisterActivity2.this, "인증코드가 올바르지 않습니다", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                nextText.setTextColor(getResources().getColor(R.color.gray_100));
                                nextArrow.setImageResource(R.drawable.next);
                                next.setEnabled(false);

                                Toast.makeText(EmailRegisterActivity2.this, "인증코드가 올바르지 않습니다", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EmailRegisterActivity2.this, "JSON 파싱 오류", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", "에러뜸!!" + error.toString());
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String errorResponse = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(errorResponse);
                                String errorMessage = jsonObject.getString("errorMessage");
                                // Handle BaseException
                                Log.d("test", "BaseException: " + errorMessage);
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PwRegisterActivity.class);
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