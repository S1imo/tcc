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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Chat_AAdapter extends RecyclerView.Adapter<ViewHolderChat> {

    private Context mContext;
    private List<User> mUsers;
    private DatabaseReference mRef;
    private FirebaseDatabase mFire = FirebaseDatabase.getInstance();

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
    public void onBindViewHolder(@NonNull final ViewHolderChat holder, int position) {
        final User user = mUsers.get(position);

        mRef = mFire.getReference();
        mRef.child("@linkers").child(user.getUs_uid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equals("true")){
                    holder.text_status.setText("Online");
                } else if(dataSnapshot.getKey().equals("false")){
                    holder.text_status.setText("Offline");
                } else if(!dataSnapshot.hasChildren()){
                    holder.text_status.setText("Offline");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
