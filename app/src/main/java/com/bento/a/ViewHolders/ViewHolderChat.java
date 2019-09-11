package com.bento.a.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.R;

public class ViewHolderChat extends RecyclerView.ViewHolder{

    public ImageView image_Chat;
    public TextView text_Chat;
    public TextView textOn_Chat;

    public ViewHolderChat(@NonNull View itemView) {
        super(itemView);

        image_Chat = itemView.findViewById(R.id.img_us);
        text_Chat = itemView.findViewById(R.id.text_us_nom);
        textOn_Chat = itemView.findViewById(R.id.text_us_status);
    }
}
