package com.example.newkey;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailRegisterActivity1 extends AppCompatActivity {
    private EditText email1;
    private Spinner email2;
    private Button emailDpCheck;
    private ImageView nextArrow, back;
    private TextView emailDpCheckText, nextText;
    private String email, selectedEmail;
    private StringBuilder url;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    RequestQueue queue;
    LinearLayout next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register1);

        email1=findViewById(R.id.email1);
        email2=findViewById(R.id.email2);
        emailDpCheck=findViewById(R.id.emailDpCheck);
        emailDpCheckText=findViewById(R.id.emailDpCheckText);
        nextArrow=findViewById(R.id.next_arrow);
        nextText=findViewById(R.id.next_text);
        next=findViewById(R.id.next);
        preferences=getSharedPreferences(preference, Context.MODE_PRIVATE);
        queue=Volley.newRequestQueue(this);

        next.setEnabled(false);

        // Spinner에 들어갈 항목들
        String[] items = {"naver.com", "gmail.com", "hanmail.net", "daum.net", "sungshin.ac.kr"};

        // ArrayAdapter를 사용하여 Spinner에 항목 연결
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // 기본 레이아웃을 사용하여 뷰 생성
                View view = super.getView(position, convertView, parent);

                // 텍스트 색상 설정 (gray_100으로 변경)
                TextView text = (TextView) view;
                text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray_100));

                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // 기본 드롭다운 레이아웃을 사용하여 뷰 생성
                View view = super.getDropDownView(position, convertView, parent);

                // 현재 모드 감지 (다크모드인지 화이트모드인지)
                int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

                // 드롭다운 항목의 텍스트 색상 설정 (다크모드일 경우 흰색, 라이트모드일 경우 검정색)
                TextView text = (TextView) view;
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray_100));
                } else {
                    text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray_600));
                }

                return view;
            }
        };

        // Spinner에 어댑터 설정
        email2.setAdapter(adapter);

        // Spinner의 항목이 선택되었을 때의 동작 설정
        email2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택된 항목의 내용 받아서 출력
                selectedEmail = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "Selected item: " + selectedEmail);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 항목도 선택되지 않았을 때의 동작 (필요한 경우 구현)
            }
        });

        emailDpCheck.setEnabled(false);
        emailDpCheck.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
        
        TextWatcher emailTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed during text changes
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEmail();
            }
        };

        email1.addTextChangedListener(emailTextWatcher);
        //email2.addTextChangedListener(emailTextWatcher);

        // 이메일 중복 체크
        emailDpCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = new StringBuilder();
                email=email1.getText().toString()+"@"+selectedEmail;

                try {
                    url.append("http://43.201.113.167:8080/user/check-email").append("?email=").append(email); //43.201.113.167
                    Log.d("test", "EmailRegisterActivity1 : 입력한 이메일 - " + email);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("success", response.toString());

                        try {
                            // 서버 응답에서 필요한 정보 추출
                            boolean isSuccess = response.getBoolean("isSuccess");

                            if (isSuccess) {
                                JSONObject result = response.getJSONObject("result");
                                boolean emailExists = result.getBoolean("emailExists");

                                // 이메일 중복 여부에 따라 처리
                                if (emailExists) {
                                    // 이메일이 중복되면
                                    emailDpCheckText.setTextColor(getResources().getColor(R.color.key_red_100));
                                    emailDpCheckText.setText("중복된 이메일입니다");

                                    next.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
                                    nextText.setTextColor(getResources().getColor(R.color.gray_100));
                                    nextArrow.setImageResource(R.drawable.next);
                                    next.setEnabled(false);
                                } else {
                                    // 이메일이 중복되지 않으면
                                    emailDpCheckText.setTextColor(getResources().getColor(R.color.key_green_400));
                                    emailDpCheckText.setText("사용 가능한 이메일입니다");

                                    next.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.key_green_400));
                                    nextText.setTextColor(getResources().getColor(R.color.gray_600));
                                    nextArrow.setImageResource(R.drawable.next_black);
                                    next.setEnabled(true);
                                }
                                emailDpCheckText.setVisibility(View.VISIBLE);
                            } else {
                                next.setEnabled(false);
                                // 요청 실패
                                Toast.makeText(EmailRegisterActivity1.this, "서버 응답 오류", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EmailRegisterActivity1.this, "JSON 파싱 오류", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test0", "에러뜸!!" + error.toString());
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String errorResponse = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(errorResponse);
                                String errorMessage = jsonObject.getString("errorMessage");
                                // Handle BaseException
                                Log.d("test1", "BaseException: " + errorMessage);
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

        // 다음 버튼 (인증코드 전송)
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = new StringBuilder();
                url.append("http://43.201.113.167:8080/user/emails/verification-requests");

                JSONObject jsonRequest = new JSONObject();
                try {
                    jsonRequest.put("email", email);
                    jsonRequest.put("isForJoin", true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", email);
                editor.commit();

                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url.toString(), jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("test", "이메일 전송 응답 - " + response.toString());

                        try {
                            // 서버 응답에서 필요한 정보 추출
                            boolean isSuccess = response.getBoolean("isSuccess");

                            if (isSuccess) {
                                JSONObject result = response.getJSONObject("result");
                                String correctCode = result.getString("code");
                                editor.putString("correctCode", correctCode);
                                editor.commit();
                            } else {
                                Toast.makeText(EmailRegisterActivity1.this, "메일 전송 오류", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EmailRegisterActivity1.this, "JSON 파싱 오류", Toast.LENGTH_SHORT).show();
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

                Intent intent = new Intent(getApplicationContext(), EmailRegisterActivity2.class);
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

    private void validateEmail() {
        String email = email1.getText().toString() + "@" + selectedEmail;
        String emailRegex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches()) { // 알맞은 이메일 형식인 경우
            emailDpCheck.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.key_green_400));
            emailDpCheck.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray_600));
            emailDpCheck.setEnabled(true);
        } else { // 알맞지 않은 이메일 형식인 경우
            emailDpCheck.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
            emailDpCheck.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray_300));
            emailDpCheck.setEnabled(false);

            emailDpCheckText.setText("");

            next.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
            nextText.setTextColor(getResources().getColor(R.color.gray_100));
            nextArrow.setImageResource(R.drawable.next);
            next.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}