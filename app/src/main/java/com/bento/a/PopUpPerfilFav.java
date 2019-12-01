package com.bento.a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bento.a.Classes.Animal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PopUpPerfilFav extends AppCompatActivity {

    private ImageView but_chat, image1, image2, image3, image4;
    private TextView but_exit, text_tip, textext_port, text_id, text_cast, text_vac, text_raca, text_status, text_desc;
    private FloatingActionButton but_exclude;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef;
    private String an_uid, other_us_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_perfil_fav);
        other_us_uid = getIntent().getStringExtra("other_us_uid");
        an_uid = getIntent().getStringExtra("an_uid");

        InpToVar();
        DisplayData();
        ButtonExit();
        ButtonChat();
        ButtonExclude();

    }

    private void DisplayData() {
        mRef.child("Animais").child(other_us_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Animal animal = snapshot.getValue(Animal.class);
                    assert animal != null;
                    System.out.println(an_uid);
                    System.out.println(animal.getAn_uid());
                    if (animal.getAn_uid().equals(an_uid)) {
                        Picasso.get().load(animal.getAn_prof_img1()).into(image1);
                        Picasso.get().load(animal.getAn_prof_img2()).into(image2);
                        Picasso.get().load(animal.getAn_prof_img3()).into(image3);
                        Picasso.get().load(animal.getAn_prof_img4()).into(image4);

                        text_tip.setText(animal.getTip_an());
                        textext_port.setText(animal.getAn_porte());
                        text_id.setText(animal.getAn_idade());
                        text_cast.setText(animal.getAn_cast());
                        text_vac.setText(animal.getAn_vacinado());
                        text_raca.setText(animal.getAn_raca());
                        text_status.setText(animal.getAn_status());
                        text_desc.setText(animal.getAn_descricao());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void InpToVar() {
        mFire = FirebaseDatabase.getInstance();
        mRef = mFire.getReference();

        image1 = findViewById(R.id.image_dog_fav);
        image2 = findViewById(R.id.image_dog1_fav);
        image3 = findViewById(R.id.image_dog2_fav);
        image4 = findViewById(R.id.image_dog3_fav);

        text_tip = findViewById(R.id.tipo_animal_fav);
        textext_port = findViewById(R.id.porte_animal_fav);
        text_id = findViewById(R.id.idade_ani_fav);
        text_cast = findViewById(R.id.castrado_animal_fav);
        text_vac = findViewById(R.id.vacinado_animal_fav);
        text_raca = findViewById(R.id.raca_animal_fav);
        text_status = findViewById(R.id.status_animal_fav);
        text_desc = findViewById(R.id.desc_animal_fav);

        but_chat = findViewById(R.id.fav_chat);
        but_exit = findViewById(R.id.txtclose);
        but_exclude = findViewById(R.id.ex_chat_fav);

    }

    private void ButtonExit() {
        but_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopUpPerfilFav.this, PerfilActivity.class));
            }
        });
    }

    private void ButtonChat() {
        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopUpPerfilFav.this, ChatConversaActivity.class)
                        .putExtra("other_us_uid", other_us_uid));
            }
        });
    }

    private void ButtonExclude() {
        but_exclude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child("Connections").child(an_uid).removeValue();
                startActivity(new Intent(PopUpPerfilFav.this, PerfilActivity.class));
            }
        });
    }
}
