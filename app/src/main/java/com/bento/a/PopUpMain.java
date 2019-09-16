package com.bento.a;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bento.a.Classes.Animal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopUpMain extends AppCompatActivity {

    private TextView exitText, text_tip,text_porte, text_idade, text_vac, text_raca, text_stat, text_desc;
    private RadioGroup sel_inp_porte, sel_inp_idade, sel_inp_vac, sel_inp_stat;
    private String an_porte, an_tip, an_idade, an_vac, an_raca, an_stat, an_desc, an_uid, user_id;
    private CircleImageView image1, image2, image3;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custompopup);
        mFire = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseDatabase.getInstance();
        mRef = mFire.getReference();
        user_id = mAuth.getUid();

        getIntentExtra();

        InpToVar();
        ButtonBack();
        InpToText();

    }

    private void getIntentExtra()
    {
        Bundle bundle = Objects.requireNonNull(getIntent().getExtras()).getBundle("an_uid");
        assert bundle != null;
        an_uid = bundle.toString();
        Toast.makeText(this, an_uid, Toast.LENGTH_SHORT).show();

    }

    private void InpToText() {
        image1 = findViewById(R.id.image_dog);
        image2 = findViewById(R.id.image_dog1);
        image3 = findViewById(R.id.image_dog2);

        text_tip = findViewById(R.id.tipo_animal);
        text_porte = findViewById(R.id.porte_animal);
        text_idade = findViewById(R.id.idade_ani);
        text_vac = findViewById(R.id.vacinado_animal);
        text_raca = findViewById(R.id.raca_animal);
        text_stat = findViewById(R.id.status_animal);
        text_desc = findViewById(R.id.desc_text);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void ButtonBack()
    {
        exitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void InpToVar()
    {
        exitText = findViewById(R.id.txtclose);
    }


}
