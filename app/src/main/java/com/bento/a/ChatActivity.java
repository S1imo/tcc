package com.bento.a;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.Classes.Animal;
import com.bento.a.Classes.Connections;
import com.bento.a.Classes.User;
import com.bento.a.ViewHolders.ViewHolderChat;
import com.bento.a.ViewHolders.ViewHolderSubChat;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef;
    private ImageView but_profile, but_adot, but_perd, but_loja, but_chat;
    private String user_id;
    private Animation anim_fade;
    private RecyclerView recyclerView;

    private FirebaseRecyclerOptions<Animal> options1;
    private FirebaseRecyclerOptions<User> options2;
    private FirebaseRecyclerAdapter<Animal, ViewHolderChat> adapter1;
    private FirebaseRecyclerAdapter<User, ViewHolderSubChat> adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseDatabase.getInstance();
        user_id = mAuth.getUid();
        mRef = mFire.getReference();

        InpToVar();
        RecycleViewA();
        Buttons();
    }


    @Override
    protected void onStop() {
        if (adapter2 != null && adapter1 != null) {
            adapter2.stopListening();
            adapter1.stopListening();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter2 == null && adapter1 == null) {
            adapter2.startListening();
            adapter1.startListening();
        }
        super.onStop();
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

                startActivity(new Intent(ChatActivity.this, PerfilActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void ButtonAdote() {
        but_adot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChatActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void ButtonPerdidos() {
        but_perd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, PerdidosActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void ButtonLoja() {
        but_loja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChatActivity.this, LojaActivity.class));
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

    private void RecycleViewA() {

        assert user_id != null;
        final DatabaseReference ref1 = mRef.child("Animais").child(user_id);
        final DatabaseReference ref2 = mRef.child("Users");
        final DatabaseReference ref3 = mRef.child("Connections");


        recyclerView = findViewById(R.id.rvChatHeader);
        recyclerView.setHasFixedSize(true);


        options1 = new FirebaseRecyclerOptions.Builder<Animal>()
                .setQuery(ref1, Animal.class).build();

        adapter1 = new FirebaseRecyclerAdapter<Animal, ViewHolderChat>(options1) {

            @NonNull
            @Override
            public ViewHolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_global_item, parent, false);
                return new ViewHolderChat(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderChat viewHolderChat, int i, @NonNull final Animal animal) {
                viewHolderChat.an_uid.setText(animal.getAn_uid());
                viewHolderChat.an_raca.setText(animal.getAn_raca());

                options2 = new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(ref2, User.class).build();

                adapter2 = new FirebaseRecyclerAdapter<User, ViewHolderSubChat>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderSubChat viewHolderSubChat, int i, @NonNull User user) {
                        viewHolderSubChat.us_nome.setText(user.getUs_nome());
                        viewHolderSubChat.us_status.setText("Online");

                        viewHolderSubChat.us_nome.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(ChatActivity.this, "PINTO", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Picasso.get().load(user.getUs_img()).into(viewHolderSubChat.us_img);
                    }

                    @NonNull
                    @Override
                    public ViewHolderSubChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View viewSub = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
                        return new ViewHolderSubChat(viewSub);
                    }
                };

                LinearLayoutManager mSubLinearLManager = new LinearLayoutManager(getApplicationContext());
                viewHolderChat.recyclerView_Sub.setLayoutManager(mSubLinearLManager);
                viewHolderChat.recyclerView_Sub.setNestedScrollingEnabled(false);
                adapter2.startListening();
                viewHolderChat.recyclerView_Sub.setAdapter(adapter2);
            }

        };
        LinearLayoutManager mLinearLManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLinearLManager);
        adapter1.startListening();
        recyclerView.setAdapter(adapter1);
    }
}
