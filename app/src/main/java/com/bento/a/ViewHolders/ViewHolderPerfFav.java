package com.bento.a.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.R;

public class ViewHolderPerfFav extends RecyclerView.ViewHolder {
    public TextView an_nome;
    public ImageView an_img, an_fav;
    public ViewHolderPerfFav(@NonNull View itemView) {
        super(itemView);
        an_nome = itemView.findViewById(R.id.text_View_upload_fav);
        an_img = itemView.findViewById(R.id.image_View_upload_fav);
        an_fav = itemView.findViewById(R.id.prof_item_fav);
    }
}
