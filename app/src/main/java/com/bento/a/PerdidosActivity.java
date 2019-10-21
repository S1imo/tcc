package com.bento.a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bento.a.Classes.Animal;
import com.bento.a.ViewHolders.ViewHolderAnimal;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PerdidosActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFire;
    private String user_id;
    private ImageView but_profile, but_adot, but_loja, but_chat;
    private RecyclerView regiaoRView, todosRView;
    private FirebaseRecyclerOptions<Animal> options1;
    private FirebaseRecyclerAdapter<Animal, ViewHolderAnimal> adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perdidos_layout);

        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseDatabase.getInstance();
        user_id = mAuth.getUid();

        InpToVar(); //input para variaveis
        Buttons(); //função dos botoes

        //PerdidoAn();
    }

    private void Buttons(){
        //MENU
        ButtonPerfil();
        ButtonAdote();
        ButtonLoja();
        ButtonChat();
    }

    private void InpToVar(){

        but_profile = findViewById(R.id.profile_icon);
        but_adot = findViewById(R.id.adot_icon);
        but_loja = findViewById(R.id.shop_icon);
        but_chat = findViewById(R.id.chat_icon);
    }

    private void ButtonPerfil()
    {
        but_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PerdidosActivity.this, PerfilActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
    }

    private void ButtonAdote()
    {
        but_adot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerdidosActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonLoja()
    {
        but_loja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerdidosActivity.this, LojaActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonChat()
    {
        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerdidosActivity.this, ChatActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    /*private void PerdidoAn(){
        regiaoRView = findViewById(R.id.rvPerdidos);
        todosRView = findViewById(R.id.rvPerdidos2);

        regiaoRView.setHasFixedSize(true);
        todosRView.setHasFixedSize(true);

        DatabaseReference mRef = mFire.getReference().child("Animais");

        options1 = new FirebaseRecyclerOptions.Builder<Animal>()
                .setQuery(mRef, Animal.class)
                .build();

        //animais da regiao -- fazer com google
        FirebaseRecyclerAdapter<Animal, ViewHolderAnimal> adapter1 = new FirebaseRecyclerAdapter<Animal, ViewHolderAnimal>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderAnimal viewHolderAnimal, int i, @NonNull Animal animal) {

            }

            @NonNull
            @Override
            public ViewHolderAnimal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.chat_item, parent, false);
                return null;
            }
        };

        //todos os animais
        FirebaseRecyclerAdapter<Animal, ViewHolderAnimal> adapter2 = new FirebaseRecyclerAdapter<Animal, ViewHolderAnimal>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderAnimal viewHolderAnimal, int i, @NonNull Animal animal) {

            }

            @NonNull
            @Override
            public ViewHolderAnimal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                return null;
            }
        };

        LinearLayoutManager mLinearManager1 = new LinearLayoutManager(getApplicationContext());
        LinearLayoutManager mLinearManager2 = new LinearLayoutManager(getApplicationContext());
        adapter1.startListening();
        adapter2.startListening();
        regiaoRView.setLayoutManager(mLinearManager1);
        todosRView.setLayoutManager(mLinearManager2);
        regiaoRView.setAdapter(adapter1);
        regiaoRView.setAdapter(adapter2);
    }*/
}
