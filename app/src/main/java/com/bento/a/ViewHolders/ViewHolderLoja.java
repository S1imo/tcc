package com.bento.a.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.R;

public class ViewHolderLoja extends RecyclerView.ViewHolder {
    public ImageView img_l;
    public TextView text_l;
    public ViewHolderLoja(@NonNull View itemView) {
        super(itemView);
        img_l = itemView.findViewById(R.id.image_View_loja);
        text_l = itemView.findViewById(R.id.text_View_loja);
    }
}
