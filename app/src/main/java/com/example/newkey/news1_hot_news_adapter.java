package com.example.newkey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class news1_hot_news_adapter extends RecyclerView.Adapter<news1_hot_news_adapter.HotNewsViewHolder> {
    private List<news1_item> newsItems;
    RequestQueue queue;
    String email;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    Set<String> storedNewsIds = new HashSet<>();

    public news1_hot_news_adapter(Context context, List<news1_item> newsItems, Set<String> storedNewsIds) {
        this.newsItems = newsItems;
        this.queue = Volley.newRequestQueue(context);
        this.preferences = context.getSharedPreferences("newkey", Context.MODE_PRIVATE);
        this.email = preferences.getString("email", null);
        this.storedNewsIds = storedNewsIds;
    }

    @NonNull
    @Override
    public HotNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news1_hot_news, parent, false);
        return new HotNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotNewsViewHolder holder, int position) {
        news1_item newsItem = newsItems.get(position);

        if(newsItem.getImg().equals("none")){
            Glide.with(holder.itemView.getContext()).load(newsItem.getMediaImg()).into(holder.newsImage);
        }
        else{
            Glide.with(holder.itemView.getContext()).load(newsItem.getImg()).into(holder.newsImage);
        }

        // 저장 뉴스 목록에 해당 뉴스 id 있으면 북마크 표시
        if (storedNewsIds.contains(newsItem.getId())) {
            holder.newsBookmark.setTag(true);
            holder.newsBookmark.setImageResource(R.drawable.bookmark_checked);
        } else {
            holder.newsBookmark.setTag(false);
            holder.newsBookmark.setImageResource(R.drawable.bookmark_unchecked);
        }

        holder.setItem(newsItem);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), news3_activity.class);
                intent.putExtra("id", newsItem.getId());
                intent.putExtra("title", newsItem.getTitle());
                intent.putExtra("content", newsItem.getContent());
                intent.putExtra("publisher", newsItem.getPublisher());
                intent.putExtra("reporter", newsItem.getReporter());
                intent.putExtra("date", newsItem.getDate());
                intent.putExtra("img", newsItem.getImg());
                intent.putExtra("summary", newsItem.getSummary());
                intent.putExtra("key", newsItem.getKey());
                intent.putExtra("url", newsItem.getUrl());
                v.getContext().startActivity(intent);

                //클릭 시 사용자 정보 저장
                String flask_url = "http://15.164.199.177:5000/click";

                final StringRequest request=new StringRequest(Request.Method.POST, flask_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //클릭 시 기사 자세히 보여주기
                        Log.d("res",response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }){
                    //@Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("user_id", email);
                        params.put("click_news", newsItem.getId());

                        return params;
                    }
                };

                request.setShouldCache(false);
                queue.add(request);
            }
        });

        holder.newsBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = holder.newsBookmark.getTag() != null && (boolean) holder.newsBookmark.getTag();
                if (!isChecked) {
                    holder.newsBookmark.setImageResource(R.drawable.bookmark_checked);
                    holder.newsBookmark.setTag(true);

                    // 사용자 저장 기사
                    String store_url = "http://15.164.199.177:5000/store";

                    final StringRequest request=new StringRequest(Request.Method.POST, store_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //클릭 시 기사 자세히 보여주기
                            Log.d("res",response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error);
                        }
                    }){
                        //@Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("user_id", email);
                            params.put("stored_news", newsItem.getId());

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
                } else {
                    holder.newsBookmark.setImageResource(R.drawable.bookmark_unchecked);
                    holder.newsBookmark.setTag(false);

                    // 사용자 저장 취소 기사
                    String unstore_url = "http://15.164.199.177:5000/unstore";

                    final StringRequest request=new StringRequest(Request.Method.POST, unstore_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //클릭 시 기사 자세히 보여주기
                            Log.d("res",response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error);
                        }
                    }){
                        //@Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("user_id", email);
                            params.put("stored_news", newsItem.getId());

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
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public static class HotNewsViewHolder extends RecyclerView.ViewHolder {
        public ImageView newsImage;
        public TextView newsTitle;
        public ImageView newsBookmark;

        public HotNewsViewHolder(View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.news1_hot_item);
            newsTitle = itemView.findViewById(R.id.news1_hot_item_title);
            newsBookmark = itemView.findViewById(R.id.news1_bookmark);
            newsBookmark.setTag(false);
        }
        public void setItem(news1_item item){
            newsTitle.setText(item.getTitle());
        }
    }
}
