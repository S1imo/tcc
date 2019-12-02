package com.bento.a.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.R;

public class ViewHolderLojaP extends RecyclerView.ViewHolder {
    public TextView nom_prod;
    public ImageView img_prod;
    public ViewHolderLojaP(@NonNull View itemView) {
        super(itemView);
        nom_prod = itemView.findViewById(R.id.text_View_upload_shop);
        img_prod = itemView.findViewById(R.id.image_View_upload_shop);
    }
}
