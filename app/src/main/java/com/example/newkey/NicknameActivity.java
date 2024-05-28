package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private StringBuilder url;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);

        nickname=findViewById(R.id.nickname);
        queue=Volley.newRequestQueue(this);
        next=findViewById(R.id.next);
        preferences=getSharedPreferences(preference, Context.MODE_PRIVATE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = new StringBuilder();
                JSONObject jsonRequest = new JSONObject();

                preferences=getSharedPreferences("newkey", MODE_PRIVATE);
                String email=preferences.getString("email", null);
                String pw=preferences.getString("pw", null);
                try {
                    url.append("http://13.124.230.98:8080/user/join");
                    jsonRequest.put("email", email);
                    jsonRequest.put("password", pw);
                    jsonRequest.put("name", nickname.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //회원가입
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("test", "NicknameActivity : 응답 - " + response.toString());

                        try {
                            // 서버 응답에서 필요한 정보 추출
                            boolean isSuccess = response.getBoolean("isSuccess");

                            if (isSuccess) {
                                Intent intent = new Intent(getApplicationContext(), ChooseTopicsActivity.class);
                                intent.putExtra("join","1");
                                startActivity(intent);
                            } else {
                                next.setClickable(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
    }
}