package com.example.newkey;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
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
import java.util.List;

import android.widget.Button;

public class notification1 extends AppCompatActivity {

    private RecyclerView rv_noti;
    private AlrimAdapter alrimAdapter;
    // private MyReceiver myReceiver; // 수신기 인스턴스 변수 추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification1);

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
                    findViewById(R.id.Newkey123456).setVisibility(View.VISIBLE);
                    findViewById(R.id.새로운알림생기면).setVisibility(View.VISIBLE);
                }
            });
        }

        // 리사이클러뷰 초기화
        rv_noti = findViewById(R.id.rv_noti);
        rv_noti.setLayoutManager(new LinearLayoutManager(notification1.this));

        RecyclerDecoration spaceDecoration = new RecyclerDecoration(8);
        rv_noti.addItemDecoration(spaceDecoration);

        // 어댑터 초기화
        alrimAdapter = new AlrimAdapter();
        rv_noti.setAdapter(alrimAdapter);

        // TODO : 알림 데이터 추가 (테스트 데이터)
        List<AlrimItem> alrimItems = new ArrayList<>();
        alrimItems.add(new AlrimItem("알림 제목 1", "몇시간 전", 0));
        alrimItems.add(new AlrimItem("알림 제목 2", "몇시간 전", 1));
        alrimItems.add(new AlrimItem("알림 제목 3", "몇시간 전", 0));

        // 어댑터에 데이터 설정
        alrimAdapter.setAlrimItems(alrimItems);

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