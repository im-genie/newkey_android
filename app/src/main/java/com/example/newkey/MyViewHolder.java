package com.example.newkey;

import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView itemTitle;
    public LinearLayout subItemContainer;
    public CheckBox checkbox1;

    public MyViewHolder(View itemView) {
        super(itemView);
        itemTitle = itemView.findViewById(R.id.itemTitle);
        subItemContainer = itemView.findViewById(R.id.subItemContainer);
        checkbox1 = itemView.findViewById(R.id.checkbox1);
    }
}



