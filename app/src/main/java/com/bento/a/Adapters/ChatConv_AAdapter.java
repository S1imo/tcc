package com.bento.a.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.Classes.Messages;
import com.bento.a.R;
import com.bento.a.ViewHolders.ViewHolderChatMessages;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChatConv_AAdapter extends RecyclerView.Adapter<ViewHolderChatMessages> {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String user_id;
    private Context mContext;
    private List<Messages> mMessages;
    private final static int MSG_DIRECTION_R = 0;
    private final static int MSG_DIRECTION_L = 1;

    public ChatConv_AAdapter(Context mContext, List<Messages> mMessages) {
        this.mContext = mContext;
        this.mMessages = mMessages;
    }

    @NonNull
    @Override
    public ViewHolderChatMessages onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_DIRECTION_R)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_messages_item_r, parent, false);
            return new ViewHolderChatMessages(view);
        }
        else
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_messages_item_l, parent, false);
            return new ViewHolderChatMessages(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderChatMessages holder, int position) {
        user_id = mAuth.getUid();
        Messages messages = mMessages.get(position);
        holder.text_messages.setText(messages.getMessage());
        holder.text_currentTime.setText(messages.getCurrent_time());

    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        user_id = mAuth.getUid();
        if(mMessages.get(position).getUs_sender().equals(user_id))
        {
            return MSG_DIRECTION_L;
        }
        else
        {
            return MSG_DIRECTION_R;
        }
    }
}