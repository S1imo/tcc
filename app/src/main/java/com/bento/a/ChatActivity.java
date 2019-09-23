package com.bento.a;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bento.a.ViewHolders.ViewHolderChat;
import com.bento.a.ViewHolders.ViewHolderSubChat;
import com.bento.a.Classes.Animal;
import com.bento.a.Classes.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

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
        if(adapter2 != null && adapter1 != null)
        {
            adapter2.stopListening();
            adapter1.stopListening();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter2 == null && adapter1 == null) {
            assert adapter2 != null;
            adapter2.startListening();
            adapter1.startListening();
        }
        super.onStop();
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

        anim_fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
    }

    //menu
    private void ButtonPerfil()
    {
        but_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChatActivity.this, PerfilActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void ButtonAdote()
    {
        but_adot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChatActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void ButtonPerdidos()
    {
        but_perd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, PerdidosActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void ButtonLoja()
    {
        but_loja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChatActivity.this, LojaActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

    private void RecycleViewA()
    {

        assert user_id != null;
        DatabaseReference ref1 = mRef.child("Animais").child(user_id);
        DatabaseReference ref2 = mRef.child("Users").child(user_id);

        recyclerView = findViewById(R.id.rvChatHeader);
        recyclerView.setHasFixedSize(true);

        options1 = new FirebaseRecyclerOptions.Builder<Animal>()
                .setQuery(ref1, Animal.class).build();

        options2 = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(ref2, User.class).build();

        /*adapter2 = new FirebaseRecyclerAdapter<User, ViewHolderSubChat>(options2) {

            private Context context;
            private List<User> users;

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderSubChat viewHolderSubChat, int i, @NonNull User user) {
                viewHolderSubChat.us_nome.setText(users.get(i).getUs_nome());
                viewHolderSubChat.us_status.setText("Online");
                Picasso.get().load(users.get(i).getUs_img()).into(viewHolderSubChat.us_img);
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot value : dataSnapshot.getChildren())
                        {
                            String key = null;
                            for(DataSnapshot value_in: value.child("Animais").child(user_id).getChildren())
                            {
                                for(DataSnapshot value_Key : value_in.child("connections").child("Yes").getChildren())
                                {
                                    key = value_Key.getKey();
                                }
                            }
                            for(DataSnapshot value_in_us: value.child("Users").getChildren())
                            {
                                System.out.println("Primeiro");
                                if(Objects.equals(value_in_us.getKey(), key))
                                {
                                    assert key != null;
                                    mRef.child("Users").child(key).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            User oUser = dataSnapshot.getValue(User.class);
                                            assert oUser != null;
                                            viewHolderSubChat.us_nome.setText(oUser.getUs_nome());
                                            viewHolderSubChat.us_status.setText(oUser.getUs_status());
                                            Picasso.get().load(oUser.getUs_img()).into(viewHolderSubChat.us_img);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ViewHolderSubChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View listView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
                return new ViewHolderSubChat(listView);
            }
        };*/

        adapter1 = new FirebaseRecyclerAdapter<Animal, ViewHolderChat>(options1) {

            private Context context;

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderChat viewHolderChat, int i, @NonNull Animal animal) {
                viewHolderChat.an_uid.setText(animal.getAn_uid());
                viewHolderChat.an_raca.setText(animal.getAn_raca());

                viewHolderChat.recyclerView_Sub.setHasFixedSize(true);
                viewHolderChat.recyclerView_Sub.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                viewHolderChat.recyclerView_Sub.setNestedScrollingEnabled(false);
                adapter2.startListening();
                viewHolderChat.recyclerView_Sub.setAdapter(adapter2);

            }

            @NonNull
            @Override
            public ViewHolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_global_item, parent, false);
                return new ViewHolderChat(view);
            }
        };
        LinearLayoutManager mLinearLManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLinearLManager);
        adapter1.startListening();
        recyclerView.setAdapter(adapter1);

        /*DatabaseReference ref = mFire.getReference().child("Animais");

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (final DataSnapshot value: dataSnapshot.getChildren())
                        {
                            boolean b = Objects.equals(value.getKey(), user_id);
                            if(b)
                            {
                                for(DataSnapshot value_in: value.getChildren())
                                {
                                    if(value_in.child("connections").hasChild("Yes")) //verificando se o usuario esta conectado
                                    {

                                        Toast.makeText(ChatActivity.this, "FASE - 1", Toast.LENGTH_SHORT).show();*/
    }
}
