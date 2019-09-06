package com.bento.a.animals;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.R;

class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView mImage;

    MyViewHolder(@NonNull View itemView) {
        super(itemView);

        mImage = itemView.findViewById(R.id.image_View_upload);

    }
}
