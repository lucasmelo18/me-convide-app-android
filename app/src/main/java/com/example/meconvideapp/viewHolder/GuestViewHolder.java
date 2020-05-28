package com.example.meconvideapp.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meconvideapp.R;

public class GuestViewHolder extends RecyclerView.ViewHolder  {

    private TextView mTextName;

    public GuestViewHolder(@NonNull View itemView) {
        super(itemView);
        this.mTextName = (TextView) itemView.findViewById(R.id.text_name);
    }
}