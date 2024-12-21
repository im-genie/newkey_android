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

    ImageView yellow, green, blue, newkey, back;
    Button complete;
    String selected = "";
    String url = "http://15.165.181.204:8080/user/profileSave";
    SharedPreferences preferences;
    public static final String preference = "newkey";
    RequestQueue queue;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        preferences = getSharedPreferences(preference, Context.MODE_PRIVATE);
        email = preferences.getString("email", null);

        queue = Volley.newRequestQueue(getApplicationContext());

        yellow = findViewById(R.id.profile_yellow);
        green = findViewById(R.id.profile_green);
        blue = findViewById(R.id.profile_blue);
        newkey = findViewById(R.id.profile_newkey);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MypageActivity.class);
                startActivity(intent);
            }
        });

        final int yellowSelectedResId = R.drawable.yellow_check; // 선택된 상태 이미지
        final int yellowDefaultResId = R.drawable.profile_yellow; // 기본 이미지

        final int greenSelectedResId = R.drawable.green_check;
        final int greenDefaultResId = R.drawable.profile_green;

        final int blueSelectedResId = R.drawable.blue_check;
        final int blueDefaultResId = R.drawable.profile_blue;

        final int newkeySelectedResId = R.drawable.newkey_check;
        final int newkeyDefaultResId = R.drawable.profile_newkey;

        // XML에서 정의된 둥근 배경과 크기 유지하면서 이미지 변경
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = "yellow";
                yellow.setImageResource(yellowSelectedResId);
                green.setImageResource(greenDefaultResId);
                blue.setImageResource(blueDefaultResId);
                newkey.setImageResource(newkeyDefaultResId);
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = "green";
                yellow.setImageResource(yellowDefaultResId);
                green.setImageResource(greenSelectedResId);
                blue.setImageResource(blueDefaultResId);
                newkey.setImageResource(newkeyDefaultResId);
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = "blue";
                yellow.setImageResource(yellowDefaultResId);
                green.setImageResource(greenDefaultResId);
                blue.setImageResource(blueSelectedResId);
                newkey.setImageResource(newkeyDefaultResId);
            }
        });

        newkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = "newkey";
                yellow.setImageResource(yellowDefaultResId);
                green.setImageResource(greenDefaultResId);
                blue.setImageResource(blueDefaultResId);
                newkey.setImageResource(newkeySelectedResId);
            }
        });

        // 프로필 저장
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