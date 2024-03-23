package com.example.newkey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newkey.AlrimItem;
import com.example.newkey.R;

import java.util.ArrayList;
import java.util.List;

public class AlrimAdapter extends RecyclerView.Adapter<AlrimAdapter.AlrimViewHolder> {
    private List<AlrimItem> alrimItems;

    // 추가: 기본 생성자
    public AlrimAdapter() {
        this.alrimItems = new ArrayList<>();
    }

    // 추가: 매개변수를 받는 생성자
    public AlrimAdapter(List<AlrimItem> alrimItems) {
        this.alrimItems = alrimItems;
    }

    public void setAlrimItems(List<AlrimItem> alrimItems) {
        this.alrimItems = alrimItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlrimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = (viewType == 0) ? R.layout.alrim_list : R.layout.alrim_list_todayhot;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new AlrimViewHolder(view, viewType);  // viewType을 전달
    }

    @Override
    public void onBindViewHolder(@NonNull AlrimViewHolder holder, int position) {
        AlrimItem item = alrimItems.get(position);

        // type에 따라 텍스트뷰 설정
        if (item.getType() == 0) {
            holder.tvNewsTitle.setText(item.getAlrim_newstitle());
            holder.tvTime.setText(item.getAlrim_time());
        } else if (item.getType() == 1) {
            holder.tvNewsTitle.setText(item.getAlrim_newstitle());
            holder.tvTime.setText(item.getAlrim_time());
        }
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
    }

}
