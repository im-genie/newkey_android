package com.example.newkey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecommendationBigsizeAdapter extends RecyclerView.Adapter<RecommendationBigsizeAdapter.ViewHolder> {

    private List<news1_item> newsItems;
    RequestQueue queue;
    String email;
    private SharedPreferences preferences;
    public static final String preference = "newkey";
    Set<String> storedNewsIds = new HashSet<>();

    public RecommendationBigsizeAdapter(Context context, List<news1_item> recommendationItems, Set<String> storedNewsIds) {
        this.newsItems = recommendationItems;
        this.queue = Volley.newRequestQueue(context);
        this.preferences = context.getSharedPreferences("newkey", Context.MODE_PRIVATE);
        this.email = preferences.getString("email", null);
        this.storedNewsIds = storedNewsIds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendation_item_bigsize, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        news1_item newsItem = newsItems.get(position);
        holder.setItem(newsItem);

        // 뉴스 이미지 없으면 언론사, 있으면 해당 이미지로 표시
        if(newsItem.getImg().equals("none")){
            new ImageLoadTask(holder.imageView).loadImage(newsItem.getMediaImg());
        }
        else{
            new ImageLoadTask(holder.imageView).loadImage(newsItem.getImg());
        }

        if(newsItem.getMediaImg().equals("null")) {
            holder.circleImageView.setImageResource(R.drawable.newkey);
        }
        else new ImageLoadTask(holder.circleImageView).loadImage(newsItem.getMediaImg());

        // 저장 뉴스 목록에 해당 뉴스 id 있으면 북마크 표시
        if (storedNewsIds.contains(newsItem.getId())) {
            holder.isClicked = Boolean.TRUE;
            holder.bookmarkBigImageView.setImageResource(R.drawable.bookmark_checked);
        } else {
            holder.isClicked = Boolean.FALSE;
            holder.bookmarkBigImageView.setImageResource(R.drawable.bookmark_unchecked);
        }
        
        // 뉴스 클릭
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), news3_activity.class);
                intent.putExtra("id", newsItem.getId());
                intent.putExtra("title", newsItem.getTitle());
                intent.putExtra("content", newsItem.getContent());
                intent.putExtra("publisher", newsItem.getPublisher());
                intent.putExtra("date", newsItem.getDate());
                intent.putExtra("img", newsItem.getImg());
                intent.putExtra("summary", newsItem.getSummary());
                intent.putExtra("key", newsItem.getKey());
                intent.putExtra("reporter", newsItem.getReporter());
                intent.putExtra("media_img", newsItem.getMediaImg());
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

                request.setRetryPolicy(new DefaultRetryPolicy(
                        1000000,  // 기본 타임아웃 (기본값: 2500ms)
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 기본 재시도 횟수 (기본값: 1)
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));

                request.setShouldCache(false);
                queue.add(request);
            }
        });

        holder.bookmarkBigImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.isClicked == Boolean.FALSE){
                    holder.isClicked = Boolean.TRUE;
                    holder.bookmarkBigImageView.setImageResource(R.drawable.bookmark_checked);

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
                }
                else {
                    holder.isClicked = Boolean.FALSE;
                    holder.bookmarkBigImageView.setImageResource(R.drawable.bookmark_unchecked);

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

    private static class ImageLoadTask {
        private ImageView imageView;
        private ExecutorService executorService = Executors.newSingleThreadExecutor();
        private Handler handler = new Handler(Looper.getMainLooper());

        public ImageLoadTask(ImageView imageView) {
            this.imageView = imageView;
        }

        public void loadImage(String url) {
            executorService.execute(() -> {
                final Bitmap bitmap = downloadImage(url);
                handler.post(() -> imageView.setImageBitmap(bitmap));
            });
        }

        private Bitmap downloadImage(String urlDisplay) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(urlDisplay);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView, bookmarkBigImageView;
        public TextView titleView, pressView, timeView;
        public CircleImageView circleImageView;
        public Boolean isClicked = Boolean.FALSE;

        public ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.news_image_big);
            titleView = v.findViewById(R.id.news_title_big);
            pressView = v.findViewById(R.id.news_press_big);
            timeView = v.findViewById(R.id.news_time_big);
            circleImageView = v.findViewById(R.id.news_press_image_big);
            bookmarkBigImageView = v.findViewById(R.id.item_bookmark_big);
        }

        public void setItem(news1_item item){
            titleView.setText(item.getTitle());
            pressView.setText(item.getPublisher());
            timeView.setText(item.getDate());
        }
    }
}