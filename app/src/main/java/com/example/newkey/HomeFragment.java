package com.example.newkey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private List<news1_item> recommendList;
    private boolean isOpened = false;
    RequestQueue recommendQueue, hotQueue, nameQueue;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    TextView userName,name;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 안내 내용 - 맞춤 설정 버튼
        LinearLayout layout_goto_mypage = view.findViewById(R.id.layout_goto_mypage);
        layout_goto_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MypageActivity.class);
                startActivity(intent);
            }
        });

        preferences=view.getContext().getSharedPreferences(preference, Context.MODE_PRIVATE);
        String email=preferences.getString("email", null);
        userName=view.findViewById(R.id.userName);
        name=view.findViewById(R.id.name);

        StringBuilder nameUrl = new StringBuilder();
        nameUrl.append("http://13.124.230.98:8080/user/info").append("?email=").append(email);
        nameQueue=Volley.newRequestQueue(view.getContext());

        //사용자 프로필,이름 가져오기
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, nameUrl.toString(), jsonRequest, new Response.Listener<JSONObject>() {
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
                        userName.setText(result.getString("name"));
                        name.setText("뉴키는 "+result.getString("name")+"님을 위한 뉴스를 추천해 드려요. \n관심사를 설정하러 가볼까요?");
                    }
                    else{
                        Toast.makeText(getContext(), "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.d("test", "Mypage error: " + error.toString());
            }
        });

        //이 부분에 추가
        request.setRetryPolicy(new DefaultRetryPolicy(
                100000000,  // 기본 타임아웃 (기본값: 2500ms)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        request.setShouldCache(false);
        nameQueue.add(request);

        // 추천뉴스
        recommendList = new ArrayList<>();
        recommendQueue = Volley.newRequestQueue(view.getContext());
        String recommendUrl = "http://15.164.199.177:5000/politic";

        final JsonArrayRequest recommendRequest = new JsonArrayRequest(Request.Method.GET, recommendUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
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

                        // NewsData 클래스를 사용하여 데이터를 저장하고 리스트에 추가
                        news1_item newsData = new news1_item(id,title,content,press,date,img,summary,key,reporter,mediaImg);
                        recommendList.add(newsData);

                        // 이후에 newsList를 사용하여 원하는 처리를 진행
                        //Adapter
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                        RecyclerView recyclerView=view.findViewById(R.id.recommendation_recyclerview);
                        recyclerView.setLayoutManager(layoutManager);
                        RecommendationAdapter adapter=new RecommendationAdapter(recommendList);
                        recyclerView.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        recommendRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000000,  // 기본 타임아웃 (기본값: 2500ms)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        recommendRequest.setShouldCache(false);
        recommendQueue.add(recommendRequest);

        // 추천뉴스 - 더보기 버튼
        LinearLayout layout_goto_recommendation = view.findViewById(R.id.layout_goto_recommendation);
        layout_goto_recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecommendationActivity.class);
                startActivity(intent);
            }
        });

        // HOT 키워드 - 물음표
        ImageView button_question = view.findViewById(R.id.button_question);

        button_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 툴팁으로 사용할 레이아웃 인플레이트
                View tooltipView = inflater.inflate(R.layout.tooltip_layout, (ViewGroup) v.getRootView(), false);
                final PopupWindow tooltipPopup = new PopupWindow(tooltipView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                // 바깥 클릭을 감지하도록 설정
                tooltipPopup.setOutsideTouchable(true);
                tooltipPopup.setFocusable(true);

                // 버튼의 위치와 크기를 가져오기 위해 배열을 생성합니다.
                int[] location = new int[2];
                button_question.getLocationOnScreen(location);

                // dp 값을 픽셀로 변환합니다. getResources()와 DisplayMetrics를 사용합니다.
                Resources resources = v.getContext().getResources();
                int pxOffset = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, resources.getDisplayMetrics()));

                // 툴팁을 버튼 위로 위치시킵니다. 버튼의 Y 좌표에서 버튼의 높이와 변환된 픽셀 값을 빼줍니다.
                tooltipPopup.showAtLocation(v.getRootView(), Gravity.NO_GRAVITY, location[0], location[1] - button_question.getHeight() - pxOffset);


                // 툴팁이 사라졌을 때의 동작 설정
                tooltipPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        // 모든 오버레이를 제거하고 원래의 뷰로 복원
                        LinearLayout container = view.findViewById(R.id.container_layout);
                        for (int i = 0; i < container.getChildCount(); i++) {
                            View child = container.getChildAt(i);
                            if (child instanceof FrameLayout) {
                                FrameLayout frameLayout = (FrameLayout) child;
                                if (frameLayout.getChildCount() > 1) {
                                    View originalChild = frameLayout.getChildAt(0);
                                    frameLayout.removeViewAt(1); // 오버레이 제거

                                    // 필요한 경우, 여기서 추가적인 복원 로직을 수행할 수 있습니다.
                                }
                            }
                        }
                    }
                });

                LinearLayout container = view.findViewById(R.id.container_layout);
                for (int i = 0; i < container.getChildCount(); i++) {
                    View child = container.getChildAt(i);

                    // 특정 레이아웃을 제외하고 투명한 회색 뷰 추가
                    if (child.getId() != R.id.hot_keyword_layout) {
                        FrameLayout frameLayout = new FrameLayout(v.getContext());
                        ViewGroup.LayoutParams originalParams = child.getLayoutParams();
                        frameLayout.setLayoutParams(originalParams);

                        // 기존 뷰를 부모에서 제거하고 FrameLayout에 추가
                        ViewGroup parent = (ViewGroup) child.getParent();
                        int index = parent.indexOfChild(child);
                        parent.removeView(child);
                        frameLayout.addView(child, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                        // 투명한 회색 뷰(overlay)를 FrameLayout에 추가
                        View overlay = new View(v.getContext());
                        overlay.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        overlay.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.gray_600_90)); // 투명한 회색
                        frameLayout.addView(overlay);

                        // FrameLayout을 원래의 부모 뷰에 추가
                        parent.addView(frameLayout, index);
                    }
                }
            }
        });

        // HOT 키워드 - 새로고침
        ImageView button_refresh = view.findViewById(R.id.button_refresh);
        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // *********** 새로고치는 event *********** //
            }
        });

        // HOT 키워드 - 키워드 클릭 시 카드뉴스로 이동
        TextView top_1 = view.findViewById(R.id.top_1);
        TextView top_2 = view.findViewById(R.id.top_2);
        TextView top_3 = view.findViewById(R.id.top_3);
        TextView top_4 = view.findViewById(R.id.top_4);
        TextView top_5 = view.findViewById(R.id.top_5);

        // 실시간 인기 키워드 순위 열기 접기
        TextView top2_1 = view.findViewById(R.id.top2_1);
        TextView top2_2 = view.findViewById(R.id.top2_2);
        TextView top2_3 = view.findViewById(R.id.top2_3);
        TextView top2_4 = view.findViewById(R.id.top2_4);
        TextView top2_5 = view.findViewById(R.id.top2_5);
        TextView top2_6 = view.findViewById(R.id.top2_6);
        TextView top2_7 = view.findViewById(R.id.top2_7);
        TextView top2_8 = view.findViewById(R.id.top2_8);
        TextView top2_9 = view.findViewById(R.id.top2_9);
        TextView top2_10 = view.findViewById(R.id.top2_10);

        hotQueue = Volley.newRequestQueue(view.getContext());
        String hotUrl = "http://15.164.199.177:5000/hot";

        JsonArrayRequest hotRequest = new JsonArrayRequest(Request.Method.GET, hotUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    // JSON 배열에서 각각의 단어 추출
                    String t1 = response.getString(0);
                    String t2 = response.getString(1);
                    String t3 = response.getString(2);
                    String t4 = response.getString(3);
                    String t5 = response.getString(4);
                    String t2_6 = response.getString(5);
                    String t2_7 = response.getString(6);
                    String t2_8 = response.getString(7);
                    String t2_9 = response.getString(8);
                    String t2_10 = response.getString(9);

                    // 각 TextView에 설정
                    top_1.setText(t1);
                    top_2.setText(t2);
                    top_3.setText(t3);
                    top_4.setText(t4);
                    top_5.setText(t5);

                    top2_1.setText(t1);
                    top2_2.setText(t2);
                    top2_3.setText(t3);
                    top2_4.setText(t4);
                    top2_5.setText(t5);
                    top2_6.setText(t2_6);
                    top2_7.setText(t2_7);
                    top2_8.setText(t2_8);
                    top2_9.setText(t2_9);
                    top2_10.setText(t2_10);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 처리
            }
        });

        hotRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000000,  // 기본 타임아웃 (기본값: 2500ms)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        hotRequest.setShouldCache(false);
        hotQueue.add(hotRequest);


        top_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView의 텍스트를 가져옵니다.
                String textToSend = top_1.getText().toString();

                // 새로운 액티비티를 시작하고 텍스트를 전달합니다.
                Intent intent = new Intent(getActivity(), KeywordActivity.class);
                intent.putExtra("keyword", textToSend);
                startActivity(intent);
            }
        });
        top_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView의 텍스트를 가져옵니다.
                String textToSend = top_2.getText().toString();

                // 새로운 액티비티를 시작하고 텍스트를 전달합니다.
                Intent intent = new Intent(getActivity(), KeywordActivity.class);
                intent.putExtra("keyword", textToSend);
                startActivity(intent);
            }
        });
        top_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView의 텍스트를 가져옵니다.
                String textToSend = top_3.getText().toString();

                // 새로운 액티비티를 시작하고 텍스트를 전달합니다.
                Intent intent = new Intent(getActivity(), KeywordActivity.class);
                intent.putExtra("keyword", textToSend);
                startActivity(intent);
            }
        });
        top_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView의 텍스트를 가져옵니다.
                String textToSend = top_4.getText().toString();

                // 새로운 액티비티를 시작하고 텍스트를 전달합니다.
                Intent intent = new Intent(getActivity(), KeywordActivity.class);
                intent.putExtra("keyword", textToSend);
                startActivity(intent);
            }
        });
        top_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView의 텍스트를 가져옵니다.
                String textToSend = top_5.getText().toString();

                // 새로운 액티비티를 시작하고 텍스트를 전달합니다.
                Intent intent = new Intent(getActivity(), KeywordActivity.class);
                intent.putExtra("keyword", textToSend);
                startActivity(intent);
            }
        });

        //실시간 인기 키워드 순위
        top2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        TextView keyword_open_close_text = view.findViewById(R.id.keyword_open_close_text);
        ImageView keyword_open_close_image = view.findViewById(R.id.keyword_open_close_image);

        ScrollView home_scrollview = view.findViewById(R.id.home_scrollview);

        LinearLayout keyword_open_close = view.findViewById(R.id.keyword_open_close);

        FrameLayout keyword_6 = view.findViewById(R.id.keyword_6);
        FrameLayout keyword_7 = view.findViewById(R.id.keyword_7);
        FrameLayout keyword_8 = view.findViewById(R.id.keyword_8);
        FrameLayout keyword_9 = view.findViewById(R.id.keyword_9);
        FrameLayout keyword_10 = view.findViewById(R.id.keyword_10);

        keyword_open_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpened) { // FALSE -> 접혀있는 상태임. 열어야 함
                    // 키워드 open
                    keyword_6.setVisibility(View.VISIBLE);
                    keyword_7.setVisibility(View.VISIBLE);
                    keyword_8.setVisibility(View.VISIBLE);
                    keyword_9.setVisibility(View.VISIBLE);
                    keyword_10.setVisibility(View.VISIBLE);
                    // 열기/접기 세팅 변경
                    keyword_open_close_text.setText("접기");
                    keyword_open_close_image.setImageResource(R.drawable.button_up);
                    // 현재 상태 변경
                    isOpened = true;
                    home_scrollview.post(new Runnable() {
                        @Override
                        public void run() {
                            home_scrollview.smoothScrollTo(0, home_scrollview.getChildAt(0).getBottom());
                        }
                    });
                }
                else {
                    // 키워드 open
                    keyword_6.setVisibility(View.GONE);
                    keyword_7.setVisibility(View.GONE);
                    keyword_8.setVisibility(View.GONE);
                    keyword_9.setVisibility(View.GONE);
                    keyword_10.setVisibility(View.GONE);
                    // 열기/접기 세팅 변경
                    keyword_open_close_text.setText("펼치기");
                    keyword_open_close_image.setImageResource(R.drawable.button_down);
                    // 현재 상태 변경
                    isOpened = false;
                }
            }
        });

        return view;
    }
}