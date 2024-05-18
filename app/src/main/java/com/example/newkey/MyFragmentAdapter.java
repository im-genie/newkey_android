package com.example.newkey;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFragmentAdapter extends FragmentStateAdapter {

    String keyword;

    public MyFragmentAdapter(KeywordActivity k, String kw) {
        super(k);
        this.keyword=kw;
    }

    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new Cardnews1Fragment(keyword);
            case 1:
                return new Cardnews2Fragment(keyword);
            case 2:
                return new Cardnews3Fragment(keyword);
            case 3:
                return new Cardnews4Fragment(keyword);
            case 4:
                return new Cardnews5Fragment(keyword);
            default:
                return new Cardnews1Fragment(keyword); // 기본값
        }
    }

    @Override
    public int getItemCount() {
        return 5; // 전체 페이지 수
    }
}