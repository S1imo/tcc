package com.bento.a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bento.a.Adapters.Loja_AAdapter;
import com.bento.a.Classes.Loja;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LojaActivity extends AppCompatActivity {

    private ImageView but_profile, but_adot, but_perdidos, but_chat;
    private RecyclerView recyclerView1, recyclerView2;
    private FirebaseDatabase mFire;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loja_layout);

        InpToVar();
        Buttons();
        RecyclerProdAn();
        RecyclerProdB();
    }

    private void InpToVar() {
        mFire = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mRef = mFire.getReference();
        user_id = mAuth.getUid();

        but_profile = findViewById(R.id.profile_icon);
        but_adot = findViewById(R.id.adot_icon);
        but_perdidos = findViewById(R.id.perdido_icon);
        but_chat = findViewById(R.id.chat_icon);
    }

    private void Buttons() {
        ButtonPerfil();
        ButtonAdote();
        ButtonPerdido();
        ButtonChat();
    }

    private void RecyclerProdAn() {
        final ArrayList<Loja> mLojas1 = new ArrayList<>();
        final Loja_AAdapter loja_AAdapter1 = new Loja_AAdapter(LojaActivity.this, mLojas1);

        recyclerView1 = findViewById(R.id.rvProds1);
        recyclerView1.hasFixedSize();

        mRef.child("Produto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getKey().equals(user_id)) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Loja loja = snapshot1.getValue(Loja.class);
                            assert loja != null;
                            if (!loja.getL_us_uid().equals(user_id)) {
                                mLojas1.add(loja);
                                loja_AAdapter1.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        recyclerView1.setAdapter(loja_AAdapter1);
    }

    private void RecyclerProdB() {
        final ArrayList<Loja> mLojas2 = new ArrayList<>();
        final Loja_AAdapter loja_AAdapter2 = new Loja_AAdapter(LojaActivity.this, mLojas2);

        recyclerView2 = findViewById(R.id.rvProds2);
        recyclerView2.hasFixedSize();

        mRef.child("Produto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getKey().equals(user_id)) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Loja loja = snapshot1.getValue(Loja.class);
                            assert loja != null;
                            if (!loja.getL_us_uid().equals(user_id)) {
                                mLojas2.add(loja);
                                loja_AAdapter2.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        recyclerView2.setAdapter(loja_AAdapter2);

    }


    private void ButtonPerfil() {
        but_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LojaActivity.this, PerfilActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
    }

    private void ButtonAdote() {
        but_adot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LojaActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void ButtonPerdido() {
        but_perdidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LojaActivity.this, PerdidosActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void ButtonChat() {
        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LojaActivity.this, ChatActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}
