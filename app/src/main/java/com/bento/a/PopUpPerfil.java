package com.bento.a;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class PopUpPerfil extends AppCompatActivity {
    private TextView exitText, text_tip, text_porte, text_idade, text_vac, text_stat, text_cast;
    private EditText text_raca, text_desc;
    private RadioGroup sel_inp_porte, sel_inp_idade, sel_inp_vac, sel_inp_stat, sel_inp_cast;
    private String an_porte, an_tip, an_idade, an_vac, an_raca, an_stat, an_desc, an_uid;
    private RadioButton but_sel_porte, but_sel_idade, but_sel_vac, but_sel_stat;
    private CircleImageView image1, image2, image3, image4;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_perfil);
        mFire = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getUid();
        mFire = FirebaseDatabase.getInstance();
        mRef = mFire.getReference();


        InpToVar();

        View exit = findViewById(R.id.exit_m);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getIntentExtra();
        SetValues();
        ButtonBack();

    }

    private void getIntentExtra() {
        an_uid = Objects.requireNonNull(getIntent().getStringExtra("an_uid"));

    }

    private void InpToVar() {
        image1 = findViewById(R.id.image_dog1);
        image2 = findViewById(R.id.image_dog2);
        image3 = findViewById(R.id.image_dog3);
        image4 = findViewById(R.id.image_dog4);

        text_tip = findViewById(R.id.tipo_animal);
        text_porte = findViewById(R.id.porte_animal);
        text_idade = findViewById(R.id.idade_ani);
        text_vac = findViewById(R.id.vacinado_animal);
        text_raca = findViewById(R.id.raca_animal); //edit
        text_stat = findViewById(R.id.status_animal);
        text_desc = findViewById(R.id.desc_animal); //edit
        text_cast = findViewById(R.id.castrado_animal);

        //parte edição
        sel_inp_idade = findViewById(R.id.radbut_idade);
        sel_inp_porte = findViewById(R.id.radbut_porte);
        sel_inp_stat = findViewById(R.id.radbut_status);
        sel_inp_vac = findViewById(R.id.radbut_vacina);
        sel_inp_cast = findViewById(R.id.radbut_castrado);

    }

    private void SetValues() {
        mRef.child("Animais").child(user_id).child(an_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Animal animal = dataSnapshot.getValue(Animal.class);
                assert animal != null;
                text_tip.setText(animal.getTip_an());
                text_desc.setText(animal.getAn_descricao());
                text_idade.setText(animal.getAn_idade());
                text_porte.setText(animal.getAn_porte());
                text_raca.setText(animal.getAn_raca());
                text_stat.setText(animal.getAn_status());
                text_vac.setText(animal.getAn_vacinado());

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

    private void SetRadioText() {
        int selectId_idade = sel_inp_idade.getCheckedRadioButtonId();
        int selectId_porte = sel_inp_porte.getCheckedRadioButtonId();
        int selectId_stat = sel_inp_stat.getCheckedRadioButtonId();
        int selectId_vac = sel_inp_vac.getCheckedRadioButtonId();
        but_sel_idade = findViewById(selectId_idade);
        but_sel_porte = findViewById(selectId_porte);
        but_sel_stat = findViewById(selectId_stat);
        but_sel_idade = findViewById(selectId_vac);
    }

    private void ButtonBack() {

    }
}