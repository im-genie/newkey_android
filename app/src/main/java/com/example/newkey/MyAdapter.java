package com.example.newkey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<String> items; // 데이터 목록

    public MyAdapter(List<String> items) {
        this.items = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topic_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String item = items.get(position);
        holder.itemTitle.setText(item);

        // 드롭다운 토글
        holder.itemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean visible = holder.subItemContainer.getVisibility() == View.VISIBLE;
                holder.subItemContainer.setVisibility(visible ? View.GONE : View.VISIBLE);
            }
        });

        // 체크박스 상태 변경 시 색상 변경
        holder.checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 체크박스의 배경색을 변경하거나, 여기서 원하는 작업을 수행합니다.
                // 예: buttonView.setBackgroundColor(isChecked ? Color.BLUE : Color.WHITE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

