package com.bento.a.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderSubChat extends RecyclerView.ViewHolder {

    public TextView us_nome, us_status;
    public CircleImageView us_img;

    public ViewHolderSubChat(@NonNull View itemView) {
        super(itemView);
        us_img = itemView.findViewById(R.id.img_us);
        us_nome = itemView.findViewById(R.id.text_us_nom);
        us_status = itemView.findViewById(R.id.text_us_status);

    }
}
