package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class LoginActivity extends AppCompatActivity {
    EditText email1,email2,pw;
    Button login,pwFind;
    ImageView back;
    RequestQueue queue;
    SharedPreferences preferences;
    String email;
    public static final String preference = "newkey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=findViewById(R.id.login);
        email1=findViewById(R.id.email1);
        email2=findViewById(R.id.email2);
        pw=findViewById(R.id.pw);
        pwFind=findViewById(R.id.pwFind);
        back=findViewById(R.id.back);
        queue=Volley.newRequestQueue(this);
        preferences=getSharedPreferences(preference, Context.MODE_PRIVATE);
        String url="http://43.201.113.167:8080/user/login";

        login.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.key_green_400));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=email1.getText().toString()+"@"+email2.getText().toString();

                JSONObject jsonRequest = new JSONObject();
                try {
                    jsonRequest.put("email", email);
                    jsonRequest.put("password", pw.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("success", response.toString());
                        JSONObject result = null;
                        long userIdx=0;
                        try {
                            result = response.getJSONObject("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(result!=null) {
                                userIdx = result.getLong("userIdx");

                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putLong("userIdx",userIdx);
                                editor.putString("email",email);
                                editor.commit();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(getApplicationContext(), "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        Log.e("test", "Login error: " + error.toString());
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
            }
        });

        pwFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PwFindActivity1.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogoActivity.class);
                startActivity(intent);
            }
        });
    }
}