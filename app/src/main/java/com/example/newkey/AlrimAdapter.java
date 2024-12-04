package com.example.newkey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlrimAdapter extends RecyclerView.Adapter<AlrimAdapter.AlrimViewHolder> {

    private List<AlrimItem> alrimItems;
    RequestQueue queue;
    String email;
    private SharedPreferences preferences;
    public static final String preference = "newkey";

    public AlrimAdapter(List<AlrimItem> alrimItems) {
        this.alrimItems = alrimItems;
    }


    @NonNull
    @Override
    public AlrimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = (viewType == 0) ? R.layout.alrim_list : R.layout.alrim_list_todayhot;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        queue= Volley.newRequestQueue(view.getContext());
        preferences=view.getContext().getSharedPreferences(preference, Context.MODE_PRIVATE);
        email=preferences.getString("email", null);

        return new AlrimViewHolder(view, viewType);  // viewType을 전달
    }

    @Override
    public void onBindViewHolder(@NonNull AlrimViewHolder holder, int position) {
        AlrimItem alrimItem = alrimItems.get(position);
        holder.setItem(alrimItem);

        // 아이템 클릭 리스너 추가
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), news3_activity.class);
                intent.putExtra("id", alrimItem.getId());
                intent.putExtra("title", alrimItem.getTitle());
                intent.putExtra("content", alrimItem.getContent());
                intent.putExtra("publisher", alrimItem.getPublisher());
                intent.putExtra("date", alrimItem.getDate());
                intent.putExtra("img", alrimItem.getImg());
                intent.putExtra("summary", alrimItem.getSummary());
                intent.putExtra("key", alrimItem.getKey());
                intent.putExtra("reporter", alrimItem.getReporter());
                intent.putExtra("media_img", alrimItem.getMediaImg());
                intent.putExtra("url", alrimItem.getUrl());
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
                        Log.d("clickError",error.toString());
                    }
                }){
                    //@Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("user_id", email);
                        params.put("click_news", alrimItem.getId());

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
    }

    @Override
    public int getItemCount() {
        return (alrimItems != null) ? alrimItems.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return alrimItems.get(position).getType();
    }

    static class AlrimViewHolder extends RecyclerView.ViewHolder {
        TextView tvNewsTitle;
        TextView tvTime;
        int viewType;  // 추가: viewType을 저장할 변수

        public AlrimViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;  // viewType 먼저 초기화

            // 실제 TextView ID로 업데이트
            tvNewsTitle = itemView.findViewById((viewType == 0) ? R.id.rv_noti_content_yesterday : R.id.rv_noti_content_today);
            tvTime = itemView.findViewById((viewType == 0) ? R.id.alrim_time_yesterday : R.id.alrim_time_today);
        }

        public void setItem(AlrimItem item){
            tvNewsTitle.setText(item.getTitle());
            tvTime.setText(item.getDate());
        }
    }
}
