package com.example.newkey;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class Cardnews2Fragment extends Fragment {

    RequestQueue hotQueue, fwohQueue;
    ImageView imageView,content;
    CircleImageView mediaCircleImg;
    TextView when,where,who,how,what,why;
    TextView titleText,dateText,reporterText,publisherText;
    String fiveWOneHUrl="http://15.164.199.177:5000/5w1h";
    String keyword;
    news1_item newsData;

    LinearLayout button_content;

    // 키워드 전달받는 생성자
    public Cardnews2Fragment(String kw) {
        this.keyword = kw;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view=(ViewGroup) inflater.inflate(R.layout.fragment_cardnews2, container, false);

        titleText=view.findViewById(R.id.title);
        dateText=view.findViewById(R.id.date);
        content=view.findViewById(R.id.content);
        reporterText=view.findViewById(R.id.reporter);
        publisherText=view.findViewById(R.id.publisher);
        imageView=view.findViewById(R.id.imageView);
        mediaCircleImg=view.findViewById(R.id.mediaCircleImg);
        button_content=view.findViewById(R.id.button_content);

        when=view.findViewById(R.id.when);
        where=view.findViewById(R.id.where);
        who=view.findViewById(R.id.who);
        how=view.findViewById(R.id.how);
        what=view.findViewById(R.id.what);
        why=view.findViewById(R.id.why);

        hotQueue = Volley.newRequestQueue(view.getContext());
        fwohQueue = Volley.newRequestQueue(view.getContext());

        String url = "http://15.164.199.177:5000/hot5";
        final StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res!!",response);
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                } catch (JSONException e) {
                    Log.d("error!!",e.toString());
                    e.printStackTrace();
                }

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

                    Date currentDate = new Date();

                    JSONObject jsonObject = jsonArray.getJSONObject(1);

                    String id = jsonObject.getString("id");
                    String title = jsonObject.getString("title");
                    String content = jsonObject.getString("origin_content");
                    String press = jsonObject.getString("media");
                    String dateStr = jsonObject.getString("date");
                    String img = jsonObject.getString("img");
                    String summary = jsonObject.getString("summary");
                    String key = jsonObject.getString("key");
                    String reporter = jsonObject.getString("reporter");
                    String mediaImg = jsonObject.getString("media_img");

                    if (!dateStr.isEmpty() && !dateStr.equals("null")) {
                        Date articleDate = sdf.parse(dateStr); // 서버에서 받은 날짜 문자열을 Date 객체로 변환
                        long diffInMillis = currentDate.getTime() - articleDate.getTime(); // 시간 차이 계산
                        String timeAgo = getTimeAgo(diffInMillis); // 차이를 "몇 시간 전" 형식으로 변환
                        newsData = new news1_item(id, title, content, press, timeAgo, img, summary, key, reporter, mediaImg);
                    } else {
                        // 날짜가 없을 경우 기본값으로 처리 (예: "방금"으로 설정)
                        newsData = new news1_item(id, title, content, press, "", img, summary, key, reporter, mediaImg);
                    }

                    titleText.setText(newsData.getTitle());
                    dateText.setText(newsData.getDate());
                    reporterText.setText(newsData.getReporter()+" 기자");
                    publisherText.setText(newsData.getPublisher());

                    if(mediaImg.equals("null")) {
                        mediaCircleImg.setImageResource(R.drawable.newkey);
                    }
                    else Glide.with(view.getContext()).load(mediaImg).into(mediaCircleImg);

                    fwoh(); // 육하원칙

                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.d("해당 기사 없음",e.toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("카드 뉴스 에러",error.toString());
            }
        }){
            //@Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("keyword", keyword); // 로그인 아이디로 바꾸기
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                1000000,  // 기본 타임아웃 (기본값: 2500ms)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        request.setShouldCache(false);
        hotQueue.add(request);

        //기사 전문
        button_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), news3_activity.class);
                intent.putExtra("id", newsData.getId());
                intent.putExtra("title", newsData.getTitle());
                intent.putExtra("content", newsData.getContent());
                intent.putExtra("publisher", newsData.getPublisher());
                intent.putExtra("reporter", newsData.getReporter());
                intent.putExtra("date", newsData.getDate());
                intent.putExtra("img", newsData.getImg());
                intent.putExtra("summary", newsData.getSummary());
                intent.putExtra("key", newsData.getKey());
                v.getContext().startActivity(intent);
            }
        });

        return view;
    }

    private void fwoh() {
        final StringRequest fiveWOneHRequest = new StringRequest(Request.Method.POST, fiveWOneHUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);

                try {
                    // JSON 형식의 response를 파싱
                    JSONObject jsonResponse = new JSONObject(response);

                    // 각 5W1H 요소에 맞는 데이터를 가져와 TextView에 설정
                    who.setText(jsonResponse.getString("누가"));
                    when.setText(jsonResponse.getString("언제"));
                    where.setText(jsonResponse.getString("어디서"));
                    how.setText(jsonResponse.getString("어떻게"));
                    why.setText(jsonResponse.getString("왜"));
                    what.setText(jsonResponse.getString("무엇을"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "응답 파싱 중 오류 발생", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("육하원칙 오류",error.toString());
            }
        }){
            //@Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id", newsData.getId()); // 로그인 아이디로 바꾸기
                params.put("summary", newsData.getSummary());
                params.put("key", newsData.getKey());

                return params;
            }
        };
        fiveWOneHRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000000,  // 기본 타임아웃 (기본값: 2500ms)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        fiveWOneHRequest.setShouldCache(false);
        fwohQueue.add(fiveWOneHRequest);
    }

    private String getTimeAgo(long diffInMillis) {
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        if (diffInMinutes < 60) {
            return diffInMinutes + "분 전";
        } else {
            long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
            if (diffInHours < 24) {
                return diffInHours + "시간 전";
            } else {
                // 하루 이상 차이 나면 원래 날짜 반환
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd. a hh:mm", Locale.KOREA);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                Date originalDate = new Date(System.currentTimeMillis() - diffInMillis);
                return sdf.format(originalDate);
            }
        }
    }
}