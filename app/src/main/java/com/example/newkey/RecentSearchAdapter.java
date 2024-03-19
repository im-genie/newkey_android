package com.example.newkey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {

    private List<String> recentSearchList;

    // 데이터 리스트를 여기에 추가해야 합니다.
    public RecentSearchAdapter(List<String> recentSearchList) {
        this.recentSearchList = recentSearchList;
    }

    // 데이터를 외부에서 삭제하는 메서드
    public void removeItem(int position) {
        recentSearchList.remove(position);
        notifyItemRemoved(position);
    }

    // 어댑터의 데이터를 모두 삭제하는 메서드
    public void clearAllItems() {
        recentSearchList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recentsearch, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String searchItem = recentSearchList.get(position);
        holder.textSearch.setText(searchItem);

        // 삭제 버튼 클릭 이벤트 처리
        holder.buttonDelete.setOnClickListener(v -> {
            // 해당 검색어 삭제 로직 추가
            recentSearchList.remove(position);
            notifyDataSetChanged();
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

