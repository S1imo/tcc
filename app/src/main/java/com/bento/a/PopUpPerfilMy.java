package com.bento.a;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class PopUpPerfilMy extends AppCompatActivity {
    private TextView exitText, text_tip, text_porte, text_idade, text_vac, text_stat, text_cast, text_raca, text_desc;
    private EditText editText_raca, editText_desc;
    private RadioGroup sel_inp_porte, sel_inp_idade, sel_inp_vac, sel_inp_stat, sel_inp_cast;
    private String an_porte, an_tip, an_idade, an_vac, an_raca, an_stat, an_desc, an_uid;
    private RadioButton but_sel_porte, but_sel_idade, but_sel_vac, but_sel_stat, porte_g, porte_p, porte_m, idade_f, idade_a, cas_s, cas_n, vac_s, vac_n, stat_adt, stat_perd;
    private CircleImageView image1, image2, image3, image4;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private String user_id;
    private Button but_editar, but_voltar, but_aplicar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_perfil_my);
        mFire = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getUid();
        mFire = FirebaseDatabase.getInstance();
        mRef = mFire.getReference();


        InpToVar();
        getIntentExtra();
        SetValues();
        ButtonEditar();
        ButtonBack();
        ButtonVoltar();

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
        text_raca = findViewById(R.id.text_raca_perf); //edit
        text_stat = findViewById(R.id.status_animal);
        text_desc = findViewById(R.id.text_desc_an); //edit
        text_cast = findViewById(R.id.castrado_animal);

        editText_desc = findViewById(R.id.desc_animal);
        editText_raca = findViewById(R.id.raca_animal);

        //RadioButtons
        porte_g = findViewById(R.id.porte_grande);
        porte_m = findViewById(R.id.porte_medio);
        porte_p = findViewById(R.id.porte_pequeno);
        idade_a = findViewById(R.id.adulto);
        idade_f = findViewById(R.id.filhote);
        cas_n = findViewById(R.id.castrado_nao);
        cas_s = findViewById(R.id.castrado_sim);
        vac_n = findViewById(R.id.vacinado_nao);
        vac_s = findViewById(R.id.vacinado_sim);
        stat_adt = findViewById(R.id.ani_adot);
        stat_perd = findViewById(R.id.ani_perdido);

        //Buttons
        but_aplicar = findViewById(R.id.but_aplicar);
        but_voltar = findViewById(R.id.but_back);


        //parte edição
        sel_inp_idade = findViewById(R.id.radbut_idade);
        sel_inp_porte = findViewById(R.id.radbut_porte);
        sel_inp_stat = findViewById(R.id.radbut_status);
        sel_inp_vac = findViewById(R.id.radbut_vacina);
        sel_inp_cast = findViewById(R.id.radbut_castrado);

    }

    private void SetValues() {
        System.out.println(user_id);
        System.out.println(an_uid);
        mRef.child("Animais").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Animal animal = snapshot.getValue(Animal.class);
                    assert animal != null;
                    if(animal.getAn_uid().equals(an_uid)){
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
                }
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
        View exit = findViewById(R.id.exit_m);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void ButtonEditar(){
        but_editar = findViewById(R.id.but_editar);

        but_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INVISIVEL
                text_porte.setVisibility(View.GONE);
                text_desc.setEnabled(true);
                text_raca.setEnabled(true);
                text_stat.setVisibility(View.GONE);
                text_cast.setVisibility(View.GONE);
                text_idade.setVisibility(View.GONE);
                text_tip.setVisibility(View.GONE);
                text_vac.setVisibility(View.GONE);
                but_editar.setVisibility(View.GONE);

                //VISIVEL
                porte_g.setVisibility(View.VISIBLE);
                porte_p.setVisibility(View.VISIBLE);
                porte_m.setVisibility(View.VISIBLE);
                idade_a.setVisibility(View.VISIBLE);
                idade_f.setVisibility(View.VISIBLE);
                cas_n.setVisibility(View.VISIBLE);
                cas_n.setVisibility(View.VISIBLE);
                vac_n.setVisibility(View.VISIBLE);
                vac_s.setVisibility(View.VISIBLE);
                stat_adt.setVisibility(View.VISIBLE);
                stat_perd.setVisibility(View.VISIBLE);
                but_voltar.setVisibility(View.VISIBLE);
                but_aplicar.setVisibility(View.VISIBLE);
                text_raca.setVisibility(View.VISIBLE);

            }
        });
    }

    private void ButtonVoltar(){
        but_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Visivel
                text_porte.setVisibility(View.VISIBLE);
                text_desc.setEnabled(false);
                text_raca.setEnabled(false);
                text_stat.setVisibility(View.VISIBLE);
                text_cast.setVisibility(View.VISIBLE);
                text_idade.setVisibility(View.VISIBLE);
                text_tip.setVisibility(View.VISIBLE);
                text_vac.setVisibility(View.VISIBLE);
                but_editar.setVisibility(View.VISIBLE);

                //Invisivel
                porte_g.setVisibility(View.GONE);
                porte_p.setVisibility(View.GONE);
                porte_m.setVisibility(View.GONE);
                idade_a.setVisibility(View.GONE);
                idade_f.setVisibility(View.GONE);
                cas_n.setVisibility(View.GONE);
                cas_n.setVisibility(View.GONE);
                vac_n.setVisibility(View.GONE);
                vac_s.setVisibility(View.GONE);
                stat_adt.setVisibility(View.GONE);
                stat_perd.setVisibility(View.GONE);
                but_voltar.setVisibility(View.GONE);
                but_aplicar.setVisibility(View.GONE);
                text_raca.setVisibility(View.GONE);
            }
        });
    }
}