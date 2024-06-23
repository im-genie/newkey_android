package com.example.newkey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    ImageView yellow,green,blue,newkey;
    Button complete;
    String selected="";
    String url="http://43.201.113.167:8080/user/profileSave";
    SharedPreferences preferences;
    public static final String preference = "newkey";
    RequestQueue queue;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        preferences=getSharedPreferences(preference, Context.MODE_PRIVATE);
        email=preferences.getString("email", null);

        queue=Volley.newRequestQueue(getApplicationContext());

        yellow = findViewById(R.id.profile_yellow);
        green = findViewById(R.id.profile_green);
        blue = findViewById(R.id.profile_blue);
        newkey = findViewById(R.id.profile_newkey);

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = "yellow";
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = "green";
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = "blue";
            }
        });

        newkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = "newkey";
            }
        });

        //프로필 저장
        complete = findViewById(R.id.completeButton);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //프로필 설정하지 않은 경우 토스트 메시지
                Log.d("선택한 프로필",selected);
                if(selected.equals("")){
                    Toast.makeText(getApplicationContext(), "프로필 사진이 설정되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    JSONObject jsonRequest = new JSONObject();
                    try {
                        jsonRequest.put("email", email);
                        jsonRequest.put("profile", selected);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("success!!", response.toString());

                            Toast.makeText(getApplicationContext(), "프로필 설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error
                            Log.d("test", "Profile error: " + error.toString());
                        }
                    });

                    request.setRetryPolicy(new DefaultRetryPolicy(
                            100000000,  // 기본 타임아웃 (기본값: 2500ms)
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));

                    request.setShouldCache(false);
                    queue.add(request);
                }
            }
        });
    }
}