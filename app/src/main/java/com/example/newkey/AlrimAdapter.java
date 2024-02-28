package com.example.newkey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlrimAdapter extends RecyclerView.Adapter<AlrimAdapter.CustomViewHolder>{

    private ArrayList<AlrimData> arrayList;

    public AlrimAdapter(ArrayList<AlrimData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AlrimAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alrim_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlrimAdapter.CustomViewHolder holder, int position) {
        holder.alrim_icon.setImageResource(arrayList.get(position).getAlrim_icon());
        holder.alrim_todaynews.setText(arrayList.get(position).getAlrim_todaynews());
        holder.alrim_content.setText(arrayList.get(position).getAlrim_content());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curName = holder.alrim_todaynews.getText().toString();
                Toast.makeText(v.getContext(), curName, Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(holder.getAdapterPosition());

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position) {
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView alrim_icon;
        protected TextView alrim_todaynews;
        protected TextView alrim_content;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.alrim_icon = (ImageView) itemView.findViewById(R.id.alrim_icon);
            this.alrim_todaynews = (TextView) itemView.findViewById(R.id.alrim_todaynews);
            this.alrim_content = (TextView) itemView.findViewById(R.id.alrim_content);
        }
    }
}
