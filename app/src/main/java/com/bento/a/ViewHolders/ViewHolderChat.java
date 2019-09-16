package com.bento.a.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.R;

public class ViewHolderChat extends RecyclerView.ViewHolder {
    public TextView an_raca, an_uid;
    public RecyclerView recyclerView_Sub;
    public ViewHolderChat(@NonNull View itemView) {
        super(itemView);
        an_raca = itemView.findViewById(R.id.an_dig_raca);
        an_uid = itemView.findViewById(R.id.an_dig_uid);
        recyclerView_Sub = itemView.findViewById(R.id.rvChat);
    }
}
