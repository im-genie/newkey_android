package com.example.newkey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private List<news1_item> recommendList;
    private boolean isOpened = false;
    RequestQueue recommendQueue, hotQueue, nameQueue, bookQueue;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    TextView userName,name;
    Set<String> storedNewsIds = new HashSet<>();
    String email;
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // 안내 내용 - 맞춤 설정 버튼
        LinearLayout layout_goto_mypage = view.findViewById(R.id.layout_goto_mypage);
        layout_goto_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MypageActivity.class);
                startActivity(intent);
            }
        });

        preferences = view.getContext().getSharedPreferences(preference, Context.MODE_PRIVATE);
        email = preferences.getString("email", null);
        userName = view.findViewById(R.id.userName);
        name = view.findViewById(R.id.name);

        StringBuilder nameUrl = new StringBuilder();
        nameUrl.append("http://15.165.181.204:8080/user/info").append("?email=").append(email);
        nameQueue = Volley.newRequestQueue(view.getContext());

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
                    if (result != null) {
                        userName.setText(result.getString("name"));
                        name.setText("뉴키는 " + result.getString("name") + "님을 위한 뉴스를 추천해 드려요. \n관심사를 설정하러 가볼까요?");
                    } else {
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

        recommendList = new ArrayList<>();
        recommendQueue = Volley.newRequestQueue(view.getContext());
        bookQueue = Volley.newRequestQueue(view.getContext());

        // 저장 & 추천 뉴스 불러오기
        fetchStoredNews();

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

        // TextView 참조
        TextView hotDate = view.findViewById(R.id.hot_date);

        // 현재 시간을 "2024. 12. 05. 14시" 형식으로 설정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH시", Locale.KOREA);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 한국 시간 설정
        String currentDate = sdf.format(new Date()); // 현재 시간 포맷

        hotDate.setText(currentDate); // TextView에 설정

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

        FrameLayout keyword_1 = view.findViewById(R.id.keyword_1);
        FrameLayout keyword_2 = view.findViewById(R.id.keyword_2);
        FrameLayout keyword_3 = view.findViewById(R.id.keyword_3);
        FrameLayout keyword_4 = view.findViewById(R.id.keyword_4);
        FrameLayout keyword_5 = view.findViewById(R.id.keyword_5);
        FrameLayout keyword_6 = view.findViewById(R.id.keyword_6);
        FrameLayout keyword_7 = view.findViewById(R.id.keyword_7);
        FrameLayout keyword_8 = view.findViewById(R.id.keyword_8);
        FrameLayout keyword_9 = view.findViewById(R.id.keyword_9);
        FrameLayout keyword_10 = view.findViewById(R.id.keyword_10);

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

        // top_1 ~ top_5의 클릭 범위 넓히기
        final View parent_1 = (View) top_1.getParent();
        final View parent_2 = (View) top_2.getParent();
        final View parent_3 = (View) top_3.getParent();
        final View parent_4 = (View) top_4.getParent();
        final View parent_5 = (View) top_5.getParent();

        parent_1.post(() -> {
            Rect delegateArea = new Rect();
            top_1.getHitRect(delegateArea);

            // 여기서 값을 조정하여 클릭 범위를 확장할 수 있습니다.
            delegateArea.top -= 50;
            delegateArea.bottom += 50;
            delegateArea.left -= 50;
            delegateArea.right += 50;

            TouchDelegate expandedArea = new TouchDelegate(delegateArea, top_1);
            parent_1.setTouchDelegate(expandedArea);
        });

        parent_2.post(() -> {
            Rect delegateArea = new Rect();
            top_2.getHitRect(delegateArea);

            // 여기서 값을 조정하여 클릭 범위를 확장할 수 있습니다.
            delegateArea.top -= 40;
            delegateArea.bottom += 40;
            delegateArea.left -= 40;
            delegateArea.right += 40;

            TouchDelegate expandedArea = new TouchDelegate(delegateArea, top_2);
            parent_2.setTouchDelegate(expandedArea);
        });

        parent_3.post(() -> {
            Rect delegateArea = new Rect();
            top_3.getHitRect(delegateArea);

            // 여기서 값을 조정하여 클릭 범위를 확장할 수 있습니다.
            delegateArea.top -= 30;
            delegateArea.bottom += 30;
            delegateArea.left -= 30;
            delegateArea.right += 30;

            TouchDelegate expandedArea = new TouchDelegate(delegateArea, top_3);
            parent_3.setTouchDelegate(expandedArea);
        });

        parent_4.post(() -> {
            Rect delegateArea = new Rect();
            top_4.getHitRect(delegateArea);

            // 여기서 값을 조정하여 클릭 범위를 확장할 수 있습니다.
            delegateArea.top -= 20;
            delegateArea.bottom += 20;
            delegateArea.left -= 20;
            delegateArea.right += 20;

            TouchDelegate expandedArea = new TouchDelegate(delegateArea, top_4);
            parent_4.setTouchDelegate(expandedArea);
        });

        parent_5.post(() -> {
            Rect delegateArea = new Rect();
            top_5.getHitRect(delegateArea);

            // 여기서 값을 조정하여 클릭 범위를 확장할 수 있습니다.
            delegateArea.top -= 20;
            delegateArea.bottom += 20;
            delegateArea.left -= 20;
            delegateArea.right += 20;

            TouchDelegate expandedArea = new TouchDelegate(delegateArea, top_5);
            parent_5.setTouchDelegate(expandedArea);
        });

        // top_1 ~ top5의 클릭 이벤트
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
        keyword_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopularKeywordActivity.class);
                intent.putExtra("popular", top2_1.getText().toString());
                startActivity(intent);
            }
        });

        keyword_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopularKeywordActivity.class);
                intent.putExtra("popular", top2_2.getText().toString());
                startActivity(intent);
            }
        });

        keyword_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopularKeywordActivity.class);
                intent.putExtra("popular", top2_3.getText().toString());
                startActivity(intent);
            }
        });

        keyword_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopularKeywordActivity.class);
                intent.putExtra("popular", top2_4.getText().toString());
                startActivity(intent);
            }
        });

        keyword_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopularKeywordActivity.class);
                intent.putExtra("popular", top2_5.getText().toString());
                startActivity(intent);
            }
        });

        keyword_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopularKeywordActivity.class);
                intent.putExtra("popular", top2_6.getText().toString());
                startActivity(intent);
            }
        });

        keyword_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopularKeywordActivity.class);
                intent.putExtra("popular", top2_7.getText().toString());
                startActivity(intent);
            }
        });

        keyword_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopularKeywordActivity.class);
                intent.putExtra("popular", top2_8.getText().toString());
                startActivity(intent);
            }
        });

        keyword_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopularKeywordActivity.class);
                intent.putExtra("popular", top2_9.getText().toString());
                startActivity(intent);
            }
        });

        keyword_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopularKeywordActivity.class);
                intent.putExtra("popular", top2_10.getText().toString());
                startActivity(intent);
            }
        });

        TextView keyword_open_close_text = view.findViewById(R.id.keyword_open_close_text);
        ImageView keyword_open_close_image = view.findViewById(R.id.keyword_open_close_image);

        ScrollView home_scrollview = view.findViewById(R.id.home_scrollview);

        LinearLayout keyword_open_close = view.findViewById(R.id.keyword_open_close);

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

    // 추천 뉴스 불러오기
    private void recommend() {
        // 로딩 시작 시, loading_layout을 보이게 하고 recommendation_recyclerview를 숨김
        LinearLayout loadingLayout = view.findViewById(R.id.loading_layout);
        RecyclerView recyclerView = view.findViewById(R.id.recommendation_recyclerview);
        loadingLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        String recommendUrl = "http://15.164.199.177:5000/recommend";
        final StringRequest recommendRequest = new StringRequest(Request.Method.POST, recommendUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res", response);
                JSONArray jsonArray = null;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

                Date currentDate = new Date();

                try {
                    jsonArray = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
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
                            String url = jsonObject.getString("url");

                            if (!dateStr.isEmpty() && !dateStr.equals("null")) {
                                Date articleDate = sdf.parse(dateStr);
                                long diffInMillis = currentDate.getTime() - articleDate.getTime();
                                String timeAgo = getTimeAgo(diffInMillis);
                                news1_item newsData = new news1_item(id, title, content, url, press, timeAgo, img, summary, key, reporter, mediaImg);
                                recommendList.add(newsData);
                            } else {
                                news1_item newsData = new news1_item(id, title, content, url, press, "", img, summary, key, reporter, mediaImg);
                                recommendList.add(newsData);
                            }

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    // 뉴스가 로드되면 loading_layout을 숨기고 recyclerView를 보이게 설정
                    loadingLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    // Adapter 설정
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    Log.d("2. 북마크한 뉴스 id", storedNewsIds.toString());
                    RecommendationAdapter adapter = new RecommendationAdapter(getContext(), recommendList, storedNewsIds);
                    recyclerView.setAdapter(adapter);
                } else {
                    // 뉴스가 없을 경우 loading_layout을 숨기지 않고 유지할 수도 있음
                    loadingLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("추천 오류", error.toString());
                // 오류 발생 시에도 로딩 화면을 숨기고 recyclerView를 숨김
                loadingLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", email);
                return params;
            }
        };

        recommendRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        recommendRequest.setShouldCache(false);
        recommendQueue.add(recommendRequest);
    }

    // 저장 뉴스 불러오기
    private void fetchStoredNews() {
        String url="http://15.164.199.177:5000/storedNews";

        final StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = new JSONArray(response);
                } catch (JSONException e) {
                    Log.d("JSONParseError", e.toString());
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        storedNewsIds.add(jsonObject.getString("id"));
                        Log.d("1. 북마크한 뉴스 id", storedNewsIds.toString());
                    } catch (JSONException e) {
                        Log.d("res!!",response);
                        e.printStackTrace();
                    }
                }
                // 추천 뉴스 불러오기
                recommend();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("storeViewError",error.toString());
            }
        }){
            //@Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", email); // 로그인 아이디로 바꾸기
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                1000000,  // 기본 타임아웃 (기본값: 2500ms)
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        request.setShouldCache(false);
        bookQueue.add(request);
    }

    // 시간 차이를 "몇 분 전", "몇 시간 전", "며칠 전"으로 변환하는 메서드
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