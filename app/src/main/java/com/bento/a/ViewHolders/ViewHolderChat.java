package com.bento.a.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderChat extends RecyclerView.ViewHolder {
    public CircleImageView image_perf;
    public TextView text_nome, text_status;

    public ViewHolderChat(@NonNull View itemView) {
        super(itemView);
        image_perf = itemView.findViewById(R.id.img_us);
        text_nome = itemView.findViewById(R.id.text_us_nom);
        text_status = itemView.findViewById(R.id.text_us_status);
    }
}
