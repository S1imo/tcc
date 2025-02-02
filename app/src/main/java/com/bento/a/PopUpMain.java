package com.bento.a;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bento.a.Classes.Animal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopUpMain extends AppCompatActivity {

    private TextView exitText, text_tip_main, text_cast_main, text_porte_main, text_idade_main, text_vac_main, text_raca_main, text_stat_main, text_desc_main;
    private CircleImageView image1, image2, image3, image4;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_main);
        mFire = FirebaseDatabase.getInstance();
        InpToVar();
        SetTextViews();
        ButtonBack();

    }

    private void InpToVar() {
        image1 = findViewById(R.id.image_dog_main);
        image2 = findViewById(R.id.image_dog1_main);
        image3 = findViewById(R.id.image_dog2_main);
        image4 = findViewById(R.id.image_dog3_main);

        text_tip_main = findViewById(R.id.tipo_animal_main);
        text_cast_main = findViewById(R.id.castrado_animal_main);
        text_porte_main = findViewById(R.id.porte_animal_main);
        text_idade_main = findViewById(R.id.idade_ani_main);
        text_vac_main = findViewById(R.id.vacinado_animal_main);
        text_raca_main = findViewById(R.id.raca_animal_main);
        text_stat_main = findViewById(R.id.status_animal_main);
        text_desc_main = findViewById(R.id.desc_animal_main);


        exitText = findViewById(R.id.txtclose);
    }

    private void SetTextViews()
    {
        mRef = mFire.getReference();

        mRef.child("Animais").child(getIntent().getStringExtra("us_uid")).child(getIntent().getStringExtra("an_uid")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Animal animal = dataSnapshot.getValue(Animal.class);
                assert animal != null;
                text_desc_main.setText(animal.getAn_descricao());
                text_idade_main.setText(animal.getAn_idade());
                text_porte_main.setText(animal.getAn_porte());
                text_tip_main.setText(animal.getTip_an());
                text_raca_main.setText(animal.getAn_raca());
                text_stat_main.setText(animal.getAn_status());
                text_vac_main.setText(animal.getAn_vacinado());
                text_cast_main.setText(animal.getAn_cast());
                Picasso.get().load(animal.getAn_prof_img1()).into(image1);
                Picasso.get().load(animal.getAn_prof_img2()).into(image2);
                Picasso.get().load(animal.getAn_prof_img3()).into(image3);
                Picasso.get().load(animal.getAn_prof_img4()).into(image4);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ButtonBack() {
        exitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
