package com.bento.a.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.Classes.Messages;
import com.bento.a.R;
import com.bento.a.ViewHolders.ViewHolderChatMessages;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatConv_AAdapter extends RecyclerView.Adapter<ViewHolderChatMessages> {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String mens_co, latitude, longitude, user_id;
    private String[] parts;
    private Context mContext;
    private List<Messages> mMessages;
    private boolean i = false;
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
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_messages_item_l, parent, false);
            return new ViewHolderChatMessages(view);
        }
        else
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_messages_item_r, parent, false);
            return new ViewHolderChatMessages(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderChatMessages holder, int position) {
        user_id = mAuth.getUid();
        final Messages messages = mMessages.get(position);
        if(messages.getMessage().contains(".jpg")){
            Picasso.get().load(messages.getMessage()).into(holder.image_message);
            holder.text_messages.setVisibility(View.GONE);
            holder.image_message.setVisibility(View.VISIBLE);
            holder.image_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!i){
                        i = true;
                        holder.image_message.setMinimumWidth(1000);
                        holder.image_message.setMinimumHeight(1000);
                        holder.image_message.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        holder.text_currentTime.setText(messages.getCurrent_time());
                    }
                    else if(i){
                        i = false;
                        holder.image_message.setMinimumWidth(50);
                        holder.image_message.setMinimumHeight(50);
                        holder.image_message.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        holder.text_currentTime.setText(messages.getCurrent_time());
                    }

                }
            });
        }
        else if(messages.getMessage().contains("lat") && messages.getMessage().contains("long")){
            mens_co = messages.getMessage();
            parts = mens_co.split("-");
            latitude = parts[0];
            longitude = parts[1];
            holder.text_messages.setText("Veja a minha localização");
            holder.text_messages.setHighlightColor(Color.BLUE);
            holder.text_messages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent());
                }
            });
        }
        else {
            holder.text_messages.setText(messages.getMessage());
            holder.image_message.setVisibility(View.GONE);
            holder.text_messages.setVisibility(View.VISIBLE);
            holder.text_currentTime.setText(messages.getCurrent_time());
        }
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