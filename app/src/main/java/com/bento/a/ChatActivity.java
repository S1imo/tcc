package com.bento.a;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.Adapters.Chat_AAdapter;
import com.bento.a.Classes.Animal;
import com.bento.a.Classes.Connections;
import com.bento.a.Classes.Messages;
import com.bento.a.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    private Chat_AAdapter chat_adp;
    private ArrayList<User> mUsersList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef;
    private RecyclerView recyclerView;
    private ImageView but_profile, but_adot, but_perd, but_loja, but_chat;
    private String user_id;
    private Animation anim_fade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseDatabase.getInstance();
        user_id = mAuth.getUid();
        mRef = mFire.getReference();

        InpToVar();
        RecycleView();
        Buttons();
    }

    private void Buttons() {
        //botões superiores - menu
        ButtonPerfil();
        ButtonAdote();
        ButtonPerdidos();
        ButtonLoja();
        ButtonChat();
    }

    private void InpToVar() {
        but_profile = findViewById(R.id.profile_icon);
        but_adot = findViewById(R.id.adot_icon);
        but_perd = findViewById(R.id.perdido_icon);
        but_loja = findViewById(R.id.shop_icon);
        but_chat = findViewById(R.id.chat_icon);


        anim_fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
    }

    //menu
    private void ButtonPerfil() {
        but_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChatActivity.this, PerfilActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void ButtonAdote() {
        but_adot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChatActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void ButtonPerdidos() {
        but_perd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, PerdidosActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void ButtonLoja() {
        but_loja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChatActivity.this, LojaActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void ButtonChat() {
        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_chat.startAnimation(anim_fade);
            }
        });
    }

    private void RecycleView() {

        mUsersList = new ArrayList<>();
        recyclerView = findViewById(R.id.rvChatHeader);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chat_adp = new Chat_AAdapter(getApplicationContext(), mUsersList);

        mRef.child("ChatList").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mRef.child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot1: dataSnapshot.getChildren()){
                                User user = snapshot1.getValue(User.class);
                                assert user != null;
                                if(user.getUs_uid().equals(snapshot.getKey())){
                                    mUsersList.add(user);
                                    chat_adp.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(chat_adp);
    }
}
