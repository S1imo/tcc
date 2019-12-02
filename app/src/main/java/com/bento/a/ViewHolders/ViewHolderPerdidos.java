package com.bento.a.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.R;

public class ViewHolderPerdidos extends RecyclerView.ViewHolder {
    public TextView text_idade, text_nome;
    public ImageView an_img;
    public ViewHolderPerdidos(@NonNull View itemView) {
        super(itemView);
        text_nome = itemView.findViewById(R.id.perdido_cid);
        text_idade = itemView.findViewById(R.id.perdido_bairro);
        an_img = itemView.findViewById(R.id.img_item_perd);
    }
}
