package com.bento.a.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.R;


public class ViewHolderAnimal extends RecyclerView.ViewHolder {

    public TextView t1;
    public ImageView i1;

    public ViewHolderAnimal(@NonNull View itemView) {
        super(itemView);

        t1 = itemView.findViewById(R.id.text_View_upload);
        i1 = itemView.findViewById(R.id.image_View_upload);

    }
}
