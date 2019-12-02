package com.bento.a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bento.a.Adapters.ViewPagerAdapter;
import com.bento.a.Classes.Animal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopUpPerdidos extends AppCompatActivity {

    private FirebaseDatabase mFire;
    private DatabaseReference mRef;
    private TextView text_raca, text_status, text_idade, text_desc;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Dialog mDialog;
    private ImageView but_voltar;
    private FloatingActionButton but_chat;
    private String other_us_id, an_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_perdidos);

         InpToVar();
         Button();
         DisplayInfo();
         
    }

    private void InpToVar() {
        other_us_id = getIntent().getStringExtra("other_us_id");
        an_id = getIntent().getStringExtra("an_id");

        mFire = FirebaseDatabase.getInstance();
        mRef = mFire.getReference();

        //viewPager = mDialog.findViewById(R.id.myViewPager);
        text_idade = findViewById(R.id.perdido_idade);
        text_raca = findViewById(R.id.perdido_nome);
        text_status = findViewById(R.id.perdido_status);
        text_desc = findViewById(R.id.perdido_info);

        but_chat = findViewById(R.id.chat_perdidos);
        but_voltar = findViewById(R.id.voltar_perdidos);
    }

    private void Button(){
        ButtonChat();
        ButtonMap();
        ButtonVoltar();
    }

    private void DisplayInfo() {
        /*viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);*/

        mRef.child("Animais").child(other_us_id).child(an_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Animal animal = dataSnapshot.getValue(Animal.class);
                assert animal != null;
                text_status.setText(animal.getAn_status());
                text_idade.setText(animal.getAn_idade());
                text_raca.setText(animal.getAn_raca());
                text_desc.setText(animal.getAn_descricao());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ButtonMap(){
        
    }

    private void ButtonChat(){
        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopUpPerdidos.this, ChatConversaActivity.class)
                        .putExtra("other_us_uid", other_us_id));
            }
        });
    }
    private void ButtonVoltar(){
        but_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopUpPerdidos.this, PerdidosActivity.class));
            }
        });

    }

}
