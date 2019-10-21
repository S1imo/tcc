package com.bento.a.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.ChatConversaActivity;
import com.bento.a.Classes.User;
import com.bento.a.PopUpPerfilFav;
import com.bento.a.R;
import com.bento.a.ViewHolders.ViewHolderChat;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Chat_AAdapter extends RecyclerView.Adapter<ViewHolderChat> {

    private Context mContext;
    private List<User> mUsers;

    public Chat_AAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item, parent, false);
        return new ViewHolderChat(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderChat holder, int position) {
        final User user = mUsers.get(position);
        holder.text_status.setText("Online");
        holder.text_nome.setText(user.getUs_nome());
        Picasso.get().load(user.getUs_img()).into(holder.image_perf);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ChatConversaActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("other_us_uid", user.getUs_uid()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
