package com.example.newkey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MypageActivity extends Activity {

    private SharedPreferences preferences;
    public static final String preference = "newkey";
    Long userIdx;
    String email;
    ImageView profileImg,back;
    TextView profileName;
    RequestQueue queue;
    StringBuilder url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        profileName = findViewById(R.id.profileId);
        profileImg = findViewById(R.id.profileImg);
        queue = Volley.newRequestQueue(getApplicationContext());

        preferences=getSharedPreferences(preference, Context.MODE_PRIVATE);
        email=preferences.getString("email","");

        back=findViewById(R.id.back);

        url = new StringBuilder();
        url.append("http://13.124.230.98:8080/user/info").append("?email=").append(email);

        //사용자 프로필,이름 가져오기
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.toString(), jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("success", response.toString());
                JSONObject result = null;

                try {
                    result = response.getJSONObject("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if(result!=null) {
                        profileName.setText(result.getString("name"));

                        if(result.getString("profile").equals("yellow")){
                            profileImg.setImageResource(R.drawable.profile_yellow);
                        }
                        else if(result.getString("profile").equals("green")){
                            profileImg.setImageResource(R.drawable.profile_green);
                        }
                        else if(result.getString("profile").equals("blue")){
                            profileImg.setImageResource(R.drawable.profile_blue);
                        }
                        else if(result.getString("profile").equals("newkey")){
                            profileImg.setImageResource(R.drawable.profile_newkey);
                        }
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
                Log.d("test", "mypage error: " + error.toString());
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

        ImageView changeProfile = findViewById(R.id.changeProfile);
        ImageView changeProfileId = findViewById(R.id.changeProfileId);
        Button viewHistory = findViewById(R.id.btn_history);
        Button viewScrap = findViewById(R.id.btn_scrap);
        Button chooseTopics = findViewById(R.id.btn_topic);
        TextView changePassword = findViewById(R.id.changePassword);
        TextView logout = findViewById(R.id.logout);

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        changeProfileId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, ChangeIdActivity.class);
                startActivity(intent);
            }
        });

        viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, ViewHistoryActivity.class);
                startActivity(intent);
            }
        });

        viewScrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, ViewScrapActivity.class);
                startActivity(intent);
            }
        });

        chooseTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, ChooseTopicsActivity.class);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, PwFindActivity1.class);
                startActivity(intent);
            }
        });
        
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, HomeFragment.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, LogoActivity.class);
                startActivity(intent);

                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("userIdx");
                editor.remove("email");
                editor.apply();
            }
        });
    }
}