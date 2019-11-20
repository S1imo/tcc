package com.bento.a.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.R;

public class ViewHolderChatMessages extends RecyclerView.ViewHolder {
    public TextView text_messages, text_currentTime;
    public ImageView image_message;
    public ViewHolderChatMessages(@NonNull View itemView) {
        super(itemView);
        text_messages = itemView.findViewById(R.id.text_messages);
        text_currentTime = itemView.findViewById(R.id.text_current_time);
        image_message = itemView.findViewById(R.id.image_chat_conv);
    }
}
