package com.bento.a;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bento.a.users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PerfilActivity extends AppCompatActivity {

    private ImageView but_profile, but_adot, but_perd, but_loja, but_chat, but_edit_prof;
    private Animation anim_fade;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        ReadInformation();
        InpToVar();
        Buttons();
    }

    private void ReadInformation()
    {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        user_id = user.getUid();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    User user = new User();
                    user.setUs_tip_usu(Objects.requireNonNull(ds.child(user_id).getValue(User.class)).getUs_tip_usu());
                    if(user.getUs_tip_usu().equals("Empresa"))
                    {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void Buttons()
    {
        //botões superiores - menu
        ButtonPerfil();
        ButtonAdote();
        ButtonPerdidos();
        ButtonLoja();
        ButtonChat();
        ButtonEdit();
    }

    private void InpToVar()
    {
        but_profile = findViewById(R.id.profile_icon);
        but_adot = findViewById(R.id.adot_icon);
        but_perd = findViewById(R.id.perdido_icon);
        but_loja = findViewById(R.id.shop_icon);
        but_chat = findViewById(R.id.chat_icon);
        but_edit_prof = findViewById(R.id.but_edit_prof);

        anim_fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
    }

    //menu
    private void ButtonPerfil()
    {
        but_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_profile.startAnimation(anim_fade);
            }
        });
    }

    private void ButtonAdote()
    {
        but_adot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_adot.startAnimation(anim_fade);
                startActivity(new Intent(PerfilActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            }
        });
    }

    private void ButtonPerdidos()
    {
        but_perd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_perd.startAnimation(anim_fade);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            }
        });
    }

    private void ButtonLoja()
    {
        but_loja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_loja.startAnimation(anim_fade);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            }
        });
    }

    private void ButtonChat()
    {
        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_chat.startAnimation(anim_fade);
                startActivity(new Intent(PerfilActivity.this, ChatActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            }
        });
    }

    private void ButtonEdit()
    {
        but_edit_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUser();
            }
        });
    }

    private void editUser()
    {


    }
}
