package com.example.newkey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class notification1 extends AppCompatActivity {

    RequestQueue queue;
    List<AlrimItem> alrimItems;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    Boolean isAlrim,isAlrimDelete;
    ImageView alrimImage;
    TextView alrimText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification1);

        preferences=getApplicationContext().getSharedPreferences(preference, Context.MODE_PRIVATE);
        isAlrim=preferences.getBoolean("alrim", true);
        isAlrimDelete=preferences.getBoolean("alrimDelete", false);
        alrimImage=findViewById(R.id.alrimImage);
        alrimText=findViewById(R.id.alrimText);

        // 뒤로가기 버튼
        ImageButton button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // 알림설정화면으로 넘어가는 버튼
        ImageButton button_setting = findViewById(R.id.button_setting);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlrimSetting.class);
                startActivity(intent);
            }
        });

        // btn_delete 버튼
        Button btnDelete = findViewById(R.id.btn_delete);
        if (btnDelete != null) {
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // fr_alrim을 숨김
                    hideFragment();

                    // 기존에는 숨겨진 Newkey123456 아이콘과 설명글이 보이도록 변경
                    alrimImage.setVisibility(View.VISIBLE);
                    alrimText.setVisibility(View.VISIBLE);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("alrimDelete",true);
                    editor.apply();
                }
            });
        }

        /*
        // 리사이클러뷰 초기화
        rv_noti = findViewById(R.id.rv_noti);
        rv_noti.setLayoutManager(new LinearLayoutManager(notification1.this));

        RecyclerDecoration spaceDecoration = new RecyclerDecoration(8);
        rv_noti.addItemDecoration(spaceDecoration);

        // 어댑터 초기화
        //alrimAdapter = new AlrimAdapter();
        rv_noti.setAdapter(alrimAdapter);
         */

        // 알림 데이터 추가
        alrimItems = new ArrayList<>();
        queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://15.164.199.177:5000/alrim";

        //isAlrimDelete
        if(isAlrim){
            final JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("success!!", "success!! " + response.toString());

                    //s3에서 기사 받아와 배열에 저장
                    try {
                        // 예시: 응답으로부터 필요한 데이터를 파싱하여 처리
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String title = jsonObject.getString("title");
                            String content = jsonObject.getString("origin_content");
                            String press = jsonObject.getString("media");
                            String date = jsonObject.getString("date");
                            String img = jsonObject.getString("img");
                            String summary=jsonObject.getString("summary");
                            String key=jsonObject.getString("key");
                            String reporter = jsonObject.getString("reporter");
                            String mediaImg = jsonObject.getString("media_img");
                            int type = jsonObject.getInt("type");

                            // NewsData 클래스를 사용하여 데이터를 저장하고 리스트에 추가
                            AlrimItem alrimData = new AlrimItem(id,title,content,press,date,img,summary,key,reporter,mediaImg,type);
                            alrimItems.add(alrimData);
                            Log.d("test!!!!",alrimItems.toString());
                        }

                        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        RecyclerView recyclerView=findViewById(R.id.rv_noti);
                        recyclerView.setLayoutManager(layoutManager);
                        AlrimAdapter adapter=new AlrimAdapter(alrimItems);
                        recyclerView.setAdapter(adapter);

                    } catch (Exception e) {
                        Log.d("test~!",e.toString());
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("fail!!",error.toString());
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(
                    1000000,  // 기본 타임아웃 (기본값: 2500ms)
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            request.setShouldCache(false);
            queue.add(request);
        }
        else{
            alrimImage.setVisibility(View.VISIBLE);
            alrimText.setVisibility(View.VISIBLE);
        }

        // 어댑터에 데이터 설정
        //alrimAdapter.setAlrimItems(alrimItems);

        /*
        // 알림 데이터가 있다면 프래그먼트를 표시하는 메서드 호출
        if (savedInstanceState == null) {
            // 프래그먼트를 동적으로 추가
            showFragment();
        }
         */

        // TODO: 여기에 알림 데이터의 여부를 확인하는 코드를 추가
        /*
        // 예시: 알림 데이터가 있다고 가정
        boolean hasNotificationData = true;

        // 알림 데이터가 있으면 fr_alrim 프래그먼트 표시
        if (hasNotificationData) {
            showFragment();

            // 알림 전송 코드 호출
            sendNotification("알림 제목", "알림 내용");
        } else {
            // 알림 데이터가 없으면 fr_alrim 프래그먼트 숨김
            frAlrim.setVisibility(View.GONE);
        }

        // 수신기 등록
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.newkey.NOTIFICATION_SENT");
        registerReceiver(myReceiver, intentFilter);
         */
    }

    private void hideFragment() {
        // 프래그먼트가 추가된 부분을 숨깁니다.
        findViewById(R.id.fr_alrim).setVisibility(View.GONE);
    }

    // fr_alrim 프래그먼트를 표시하는 메서드
    private void showFragment() {
        // 프래그먼트가 추가될 부분을 표시합니다.
        findViewById(R.id.fr_alrim).setVisibility(View.VISIBLE);
    }


    /*
    // 알림을 수신하는 메서드
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 알림이 수신되었을 때 실행되는 코드를 작성
            // 여기에서는 리사이클러뷰에 데이터를 추가하거나 업데이트하는 등의 작업을 수행

            // 예: 리사이클러뷰 어댑터에 데이터 추가 및 업데이트
            // TODO : alrimAdapter.addData(new AlrimItem("New Notification", "Content of the notification"));
            alrimAdapter.notifyDataSetChanged();
        }
    }


    // 알림을 전송하는 메서드
    private void sendNotification(String title, String content) {
        // NotificationManager 가져오기
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 알림 채널 생성 (Android 8.0 이상에서 필요)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // 알림 빌더 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title) // TODO : 알림 제목
                .setContentText(content) // TODO : 알림 내용
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // 알림 표시
        // TODO : notificationManager.notify(notificationId, builder.build());
    }
     */


}