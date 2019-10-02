package com.bento.a.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.R;

public class ViewHolderChatMessages extends RecyclerView.ViewHolder {
    public TextView text_messages, text_currentTime;
    public ViewHolderChatMessages(@NonNull View itemView) {
        super(itemView);

        text_messages = itemView.findViewById(R.id.text_messages);
        text_currentTime = itemView.findViewById(R.id.text_current_time);
    }
}
