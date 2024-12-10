package com.example.newkey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    private ImageView yellow, green, blue, newkey;
    private Button complete;
    private String selected = "";
    private String url = "http://15.165.181.204:8080/user/profileSave";
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    private RequestQueue queue;
    private String email;

    // 리소스 ID 상수
    private final int yellowSelectedResId = R.drawable.yellow_check;
    private final int greenSelectedResId = R.drawable.green_check;
    private final int blueSelectedResId = R.drawable.blue_check;
    private final int newkeySelectedResId = R.drawable.newkey_check;

    private final int yellowDefaultResId = R.drawable.profile_yellow;
    private final int greenDefaultResId = R.drawable.profile_green;
    private final int blueDefaultResId = R.drawable.profile_blue;
    private final int newkeyDefaultResId = R.drawable.profile_newkey;

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
        complete = findViewById(R.id.completeButton);

        // 이미지뷰에 크기 조정된 Drawable 적용
        setResizedDrawable(yellow, yellowSelectedResId);
        setResizedDrawable(green, greenSelectedResId);
        setResizedDrawable(blue, blueSelectedResId);
        setResizedDrawable(newkey, newkeySelectedResId);

        // 클릭 리스너 설정
        setOnClickListeners();

        // 프로필 저장 버튼 클릭 이벤트
        complete.setOnClickListener(view -> saveProfile());
    }

    // 이미지 크기를 조정하고 ImageView에 설정하는 메서드
    private void setResizedDrawable(ImageView imageView, int resId) {
        Drawable resizedDrawable = getResizedDrawable(this, resId, 120, 120);
        if (resizedDrawable != null) {
            imageView.setImageDrawable(resizedDrawable);
        }
    }

    // 클릭 리스너 설정
    private void setOnClickListeners() {
        yellow.setOnClickListener(v -> updateSelection("yellow", yellowSelectedResId));
        green.setOnClickListener(v -> updateSelection("green", greenSelectedResId));
        blue.setOnClickListener(v -> updateSelection("blue", blueSelectedResId));
        newkey.setOnClickListener(v -> updateSelection("newkey", newkeySelectedResId));
    }

    // 선택된 프로필 이미지 업데이트
    private void updateSelection(String profile, int selectedResId) {
        selected = profile;
        yellow.setImageResource(profile.equals("yellow") ? selectedResId : yellowDefaultResId);
        green.setImageResource(profile.equals("green") ? selectedResId : greenDefaultResId);
        blue.setImageResource(profile.equals("blue") ? selectedResId : blueDefaultResId);
        newkey.setImageResource(profile.equals("newkey") ? selectedResId : newkeyDefaultResId);
    }

    // 프로필 저장
    private void saveProfile() {
        if (selected.isEmpty()) {
            Toast.makeText(getApplicationContext(), "프로필 사진이 설정되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("email", email);
            jsonRequest.put("profile", selected);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                response -> {
                    Log.d("Success", response.toString());
                    Toast.makeText(getApplicationContext(), "프로필 설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MypageActivity.class));
                },
                error -> Log.e("Error", "Profile error: " + error.toString())
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000, // 타임아웃 시간 (ms)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        request.setShouldCache(false);
        queue.add(request);
    }

    // 리소스 ID에 해당하는 이미지를 크기 조정하는 메서드
    private Bitmap resizeDrawable(Context context, int resId, int targetWidth, int targetHeight) {
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        return Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, true);
    }

    // 크기를 조정한 Bitmap을 다시 Drawable로 변환하는 메서드
    private Drawable getResizedDrawable(Context context, int resId, int targetWidth, int targetHeight) {
        Bitmap resizedBitmap = resizeDrawable(context, resId, targetWidth, targetHeight);
        return resizedBitmap != null ? new BitmapDrawable(context.getResources(), resizedBitmap) : null;
    }
}
