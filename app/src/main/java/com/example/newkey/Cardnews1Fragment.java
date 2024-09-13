package com.example.newkey;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Cardnews1Fragment extends Fragment {

    RequestQueue queue;
    ImageView imageView,content;
    CircleImageView mediaCircleImg;
    TextView when,where,who,how,what,why;
    TextView titleText,dateText,reporterText,publisherText;
    String fiveWOneHUrl="http://15.164.199.177:5000/5w1h";
    String keyword;
    news1_item newsData;

    LinearLayout button_content;

    // 키워드 전달받는 생성자
    public Cardnews1Fragment(String kw) {
        this.keyword = kw;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view=(ViewGroup) inflater.inflate(R.layout.fragment_cardnews1, container, false);

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

        queue = Volley.newRequestQueue(view.getContext());
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
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String id = jsonObject.getString("id");
                    String title = jsonObject.getString("title");
                    String content = jsonObject.getString("origin_content");
                    String press = jsonObject.getString("media");
                    String date = jsonObject.getString("date");
                    String img = jsonObject.getString("img");
                    String summary = jsonObject.getString("summary");
                    String key = jsonObject.getString("key");
                    String reporter = jsonObject.getString("reporter");
                    String mediaImg = jsonObject.getString("media_img");

                    // NewsData 클래스를 사용하여 데이터를 저장하고 리스트에 추가
                    newsData = new news1_item(id,title,content,press,date,img,summary,key,reporter,mediaImg);

                    titleText.setText(newsData.getTitle());
                    dateText.setText(newsData.getDate());
                    reporterText.setText(newsData.getReporter()+" 기자");
                    publisherText.setText(newsData.getPublisher());

                    Glide.with(view.getContext()).load(mediaImg).into(mediaCircleImg);

                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.d("해당 기사 없음",e.toString());
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
        queue.add(request);

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
}