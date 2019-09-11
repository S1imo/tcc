package com.bento.a;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bento.a.ViewHolders.ViewHolderAnimal;
import com.bento.a.ViewHolders.ViewHolderChat;
import com.bento.a.animals.Animal;
import com.bento.a.users.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFire;
    private ImageView but_profile, but_adot, but_perd, but_loja, but_chat;
    private String user_id;
    private Animation anim_fade;
    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<User> options;
    private FirebaseRecyclerAdapter<User, ViewHolderAnimal> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseDatabase.getInstance();
        user_id = mAuth.getUid();

        InpToVar();
        VerifyRecycle();
        Buttons();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        if(adapter != null)
            adapter.stopListening();
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }

    private void Buttons()
    {
        //botões superiores - menu
        ButtonPerfil();
        ButtonAdote();
        ButtonPerdidos();
        ButtonLoja();
        ButtonChat();
    }

    private void InpToVar()
    {
        but_profile = findViewById(R.id.profile_icon);
        but_adot = findViewById(R.id.adot_icon);
        but_perd = findViewById(R.id.perdido_icon);
        but_loja = findViewById(R.id.shop_icon);
        but_chat = findViewById(R.id.chat_icon);

        recyclerView = findViewById(R.id.rvChat);
        anim_fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
    }

    //menu
    private void ButtonPerfil()
    {
        but_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_profile.startAnimation(anim_fade);
                startActivity(new Intent(ChatActivity.this, PerfilActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private void ButtonAdote()
    {
        but_adot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_adot.startAnimation(anim_fade);
                startActivity(new Intent(ChatActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private void ButtonPerdidos()
    {
        but_perd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_perd.startAnimation(anim_fade);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private void ButtonLoja()
    {
        but_loja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_loja.startAnimation(anim_fade);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private void ButtonChat()
    {
        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_chat.startAnimation(anim_fade);
            }
        });
    }


    private void VerifyRecycle()
    {
        assert user_id != null;
        DatabaseReference ref = mFire.getReference().child("Animais").child(user_id);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot value: dataSnapshot.getChildren())
                {
                    if(value.child("connection").hasChild("Yes"))
                    {
                        RecyclerModel();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void RecyclerModel()
    {
        DatabaseReference ref = mFire.getReference().child("Users").child(user_id);
        recyclerView = findViewById(R.id.rvAnimal);
        recyclerView.setHasFixedSize(true);

        options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(ref, User.class).build();

        FirebaseRecyclerAdapter<User, ViewHolderChat> adapter = new FirebaseRecyclerAdapter<User, ViewHolderChat>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderChat viewHolderChat, int i, @NonNull final User user) {
                Toast.makeText(ChatActivity.this, "PINTO", Toast.LENGTH_SHORT).show();
            }

            @NonNull
            @Override
            public ViewHolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);

                return new ViewHolderChat(view);
            }
        };

        LinearLayoutManager mLinearLManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
