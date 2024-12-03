package com.example.newkey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Window;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MypageActivity extends Activity {

    private SharedPreferences preferences;
    public static final String preference = "newkey";
    Long userIdx;
    String email;
    ImageView profileImg;
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

        url = new StringBuilder();
        url.append("http://15.165.181.204:8080/user/info").append("?email=").append(email);

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

        // 네비게이션 바
        ImageView button_home = findViewById(R.id.button_home);
        ImageView button_feed = findViewById(R.id.button_feed);
        ImageView button_person = findViewById(R.id.button_person);

        // Home 버튼 클릭
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MypageActivity.this, MainActivity.class);
                startActivity(intent);
                button_home.setImageResource(R.drawable.home);
                button_feed.setImageResource(R.drawable.feed);
                button_person.setImageResource(R.drawable.person_green);
            }
        });
        // News 버튼 클릭
        button_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MypageActivity.this, news1_activity.class);
                startActivity(intent);
                button_home.setImageResource(R.drawable.home);
                button_feed.setImageResource(R.drawable.feed_green);
                button_person.setImageResource(R.drawable.person);
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
        ConstraintLayout viewHistory = findViewById(R.id.btn_history);
        ConstraintLayout viewScrap = findViewById(R.id.btn_scrap);
        ConstraintLayout chooseTopics = findViewById(R.id.btn_topic);
        TextView changePassword = findViewById(R.id.changePassword);
        TextView logout = findViewById(R.id.logout);
        TextView deleteId = findViewById(R.id.deleteId);

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
                intent.putExtra("join",false);
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        deleteId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteAccountDialog();
            }
        });

        // 검색 클릭이벤트
        ImageView main_activity_linearlayout1_imageview2 = findViewById(R.id.main_activity_linearlayout1_imageview2);
        main_activity_linearlayout1_imageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MypageActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        // 알림 클릭이벤트
        ImageView main_activity_linearlayout1_imageview3 = findViewById(R.id.main_activity_linearlayout1_imageview3);
        main_activity_linearlayout1_imageview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MypageActivity.this, notification1.class);
                startActivity(intent);
            }
        });
    }

    private void showLogoutDialog() {
        // 팝업 다이얼로그를 띄우는 메서드
        AlertDialog.Builder builder = new AlertDialog.Builder(MypageActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.logout_dialog, null);
        builder.setView(dialogView);

        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));}

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그아웃 처리
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("userIdx");
                editor.remove("email");
                editor.apply();

                Intent intent = new Intent(MypageActivity.this, LogoActivity.class);
                startActivity(intent);
                dialog.dismiss(); // 다이얼로그 닫기
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // 다이얼로그 닫기
            }
        });

        dialog.show();
    };

    private void showDeleteAccountDialog() {
        // 회원탈퇴 팝업 다이얼로그를 띄우는 메서드
        AlertDialog.Builder builder = new AlertDialog.Builder(MypageActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.delete_account_dialog, null); // delete_account_dialog 레이아웃 사용
        builder.setView(dialogView);

        Button btnConfirmDelete = dialogView.findViewById(R.id.btn_confirm_delete);
        Button btnCancelDelete = dialogView.findViewById(R.id.btn_cancel_delete);

        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btnConfirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클라이언트 측 회원탈퇴 처리
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("userIdx");
                editor.remove("email");
                editor.apply();

                // 스프링 서버 측 회원탈퇴 처리
                url = new StringBuilder();
                url.append("http://15.165.181.204:8080/user/delete");

                JSONObject jsonRequest = new JSONObject();
                try {
                    jsonRequest.put("email", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest deleteRequest1=new JsonObjectRequest(Request.Method.POST, url.toString(), jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("test", "이메일 전송 응답 - " + response.toString());
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

                deleteRequest1.setShouldCache(false); //이전 결과가 있어도 새로 요청하여 응답을 보여준다.
                deleteRequest1.setRetryPolicy(new DefaultRetryPolicy(100000000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(deleteRequest1);

                /*
                // redis 서버 측 회원탈퇴 처리
                String url = "http://15.164.199.177:5000/delete";
                final StringRequest deleteRequest2=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("userDelete",response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("userDeleteError",error.toString());
                    }
                }){
                    //@Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("user_id", email);
                        return params;
                    }
                };
                deleteRequest2.setRetryPolicy(new DefaultRetryPolicy(
                        1000000,  // 기본 타임아웃 (기본값: 2500ms)
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));

                deleteRequest2.setShouldCache(false);
                queue.add(deleteRequest2);
                 */

                Intent intent = new Intent(MypageActivity.this, LogoActivity.class);
                startActivity(intent);
                dialog.dismiss(); // 다이얼로그 닫기

                Toast.makeText(MypageActivity.this, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // 다이얼로그 닫기
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 버튼을 눌렀을 때 MainActivity를 시작하도록 설정
        Intent intent = new Intent(MypageActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}