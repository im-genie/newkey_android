package com.example.newkey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {

    List<String> recentSearchList;
    SharedPreferences preferences;
    public static final String preference = "newkey";
    RequestQueue queue;
    String email;
    Context context;

    // 데이터 리스트를 여기에 추가해야 합니다.
    public RecentSearchAdapter(Context context,List<String> recentSearchList) {
        this.context=context;
        this.recentSearchList = recentSearchList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recentsearch, parent, false);
        queue= Volley.newRequestQueue(v.getContext());
        preferences=v.getContext().getSharedPreferences("newkey",  Context.MODE_PRIVATE);
        email=preferences.getString("email", null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String searchItem = recentSearchList.get(position);
        holder.textSearch.setText(searchItem);

        holder.textSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchViewActivity.class);
                intent.putExtra("search", holder.textSearch.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        // 삭제 버튼 클릭 이벤트 처리
        holder.buttonDelete.setOnClickListener(v -> {
            // 해당 검색어 삭제 로직 추가
            recentSearchList.remove(position);
            notifyDataSetChanged();

            String url = "http://15.164.199.177:5000/searchDelete";
            final StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("searchDelete",response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("searchDeleteError",error.toString());
                }
            }){
                //@Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", email);
                    params.put("keyword", searchItem);
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
        });
    }

    @Override
    public int getItemCount() {
        return recentSearchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textSearch;
        ImageButton buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textSearch = itemView.findViewById(R.id.textSearch);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}

