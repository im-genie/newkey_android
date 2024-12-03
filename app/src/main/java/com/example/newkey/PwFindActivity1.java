package com.example.newkey;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PwFindActivity1 extends AppCompatActivity {
    private EditText email1,code;
    private Spinner email2;
    private Button codeSend,codeCheck;
    private ImageView nextArrow, codeRightView, back;
    private TextView nextText;
    private String email, selectedEmail;
    private StringBuilder url;
    TextView codeRightText;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    RequestQueue queue;
    LinearLayout next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_find1);

        email1=findViewById(R.id.email1);
        email2=findViewById(R.id.email2);
        next=findViewById(R.id.next);
        nextText=findViewById(R.id.next_text);
        nextArrow=findViewById(R.id.next_arrow);
        preferences=getSharedPreferences(preference, Context.MODE_PRIVATE);
        queue= Volley.newRequestQueue(this);

        code=findViewById(R.id.code);
        codeSend=findViewById(R.id.codeSend);
        codeCheck=findViewById(R.id.codeCheck);
        codeRightText=findViewById(R.id.codeRightText);
        codeRightView=findViewById(R.id.codeRightView);

        preferences=getSharedPreferences(preference, Context.MODE_PRIVATE);
        queue=Volley.newRequestQueue(this);

        codeCheck.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));

        codeSend.setEnabled(false);
        codeSend.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
        codeSend.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray_300));

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
        // email2.addTextChangedListener(emailTextWatcher);

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

        // 인증코드 전송 버튼
        codeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = new StringBuilder();
                url.append("http://15.165.181.204:8080/user/emails/verification-requests");

                email=email1.getText().toString()+"@"+selectedEmail;

                JSONObject jsonRequest = new JSONObject();
                try {
                    jsonRequest.put("email", email);
                    jsonRequest.put("isForJoin", false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", email);
                editor.commit();

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(PwFindActivity1.this, "인증코드가 전송되었습니다", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(PwFindActivity1.this, "메일 전송 오류", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PwFindActivity1.this, "JSON 파싱 오류", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PwFindActivity1.this, "가입되지 않은 이메일입니다", Toast.LENGTH_SHORT).show();
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
                    codeCheck.setTextColor(ContextCompat.getColor(PwFindActivity1.this, R.color.gray_600));
                } else {
                    codeCheck.setEnabled(false);
                    codeCheck.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
                    codeCheck.setTextColor(ContextCompat.getColor(PwFindActivity1.this, R.color.gray_300));
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
                    url.append("http://15.165.181.204:8080/user/emails/verifications");

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
                                    codeRightView.setVisibility(View.VISIBLE);

                                    next.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.key_green_400));
                                    nextText.setTextColor(getResources().getColor(R.color.gray_600));
                                    nextArrow.setImageResource(R.drawable.next_black);
                                    next.setEnabled(true);
                                }
                            } else {
                                next.setEnabled(false);
                                Log.d("test", "인증코드 맞지 않음");
                                Toast.makeText(PwFindActivity1.this, "인증코드가 맞지 않습니다", Toast.LENGTH_SHORT).show();

                                next.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
                                nextText.setTextColor(getResources().getColor(R.color.gray_100));
                                nextArrow.setImageResource(R.drawable.next);
                                next.setEnabled(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PwFindActivity1.this, "JSON 파싱 오류", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(getApplicationContext(),PwFindActivity2.class);
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
            codeSend.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.key_green_400));
            codeSend.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray_600));
            codeSend.setEnabled(true);
        } else { // 알맞지 않은 이메일 형식인 경우
            codeSend.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.gray_400));
            codeSend.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray_300));
            codeSend.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}