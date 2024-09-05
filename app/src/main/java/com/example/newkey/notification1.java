package com.example.newkey;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import java.util.Calendar;

public class notification1 extends AppCompatActivity {

    RequestQueue queue;
    List<AlrimItem> alrimItems;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    Boolean isAlrim,isAlrimDelete;
    ImageView alrimImage;
    TextView alrimText;
    private static final String CHANNEL_ID = "newkey_channel";
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;


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
                finish();
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

                    // 기존에는 숨겨진 Newkey123456 아이콘과 설명글이 보이도록 함
                    alrimImage.setVisibility(View.VISIBLE);
                    alrimText.setVisibility(View.VISIBLE);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("alrimDelete",true);
                    editor.apply();
                }
            });
        }

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
                            String typeString = jsonObject.getString("type");


                            // 첫 번째 인덱스의 문자 추출 ('1')
                            char firstChar = typeString.charAt(0);

                            // 문자를 문자열로 변환한 후 정수로 변환
                            int type = Integer.parseInt(String.valueOf(firstChar));

                            // NewsData 클래스를 사용하여 데이터를 저장하고 리스트에 추가
                            AlrimItem alrimData = new AlrimItem(id,title,content,press,date,img,summary,key,reporter,mediaImg,type);
                            alrimItems.add(alrimData);
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

        // 알림 설정
        setDailyAlarms(this);

        // 알림 클릭 시 앱 실행 권한 허용 목적
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 이상에서만 실행
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            }
        }
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NewKeyChannel";
            String description = "Channel for NewKey notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }

            Log.d("Notification", "Notification channel created");
        }
    }

    private void sendNotification(String title, String content) {
        createNotificationChannel();

        // 앱을 실행시키기 위한 Intent 생성
        Intent intent = new Intent(this, notification1.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); // 플래그 조정

        // 고유한 requestCode 생성
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // 알림 빌더 구성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.noti_icon_2) // 작은 아이콘 설정
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // 알림을 눌렀을 때 실행될 Intent
                .setAutoCancel(true); // 알림을 누르면 자동으로 제거되도록 설정

        // 알림 ID를 고유하게 생성
        int notificationId = (int) System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(notificationId, builder.build());
        }

        // 디버깅 로그 추가
        Log.d("Notification", "Notification sent with ID: " + notificationId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static void setDailyAlarms(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);

        setAlarm(context, alarmManager, intent, 0, 8, 0); //첫번째 알림 시간 설정
        setAlarm(context, alarmManager, intent, 1, 20, 0); //두번째 알림 시간 설정
    }

    public static void setAlarm(Context context, AlarmManager alarmManager, Intent intent, int requestCode, int hour, int minute) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // 현재 시간을 기준으로 알람 시간을 설정
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Log.d("Alarm", "Alarm set for " + hour + ":" + minute);
    }
}