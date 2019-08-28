package com.bento.a;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.bento.a.animals.Animal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;


public class CadAnimal extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageButton but_voltar;
    private EditText editTextRaca, editTextDesc;
    private Button buttonAplicar;
    private RadioGroup inp_sel_port, inp_sel_vac, inp_sel_stat;
    private RadioButton but_rad_port, but_rad_vac, but_rad_stat;
    private Spinner inp_tip_animal;
    private String tip_animal, an_port, an_vac, an_stat, an_desc, an_raca;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_dog_layout);

        InpToVar();
        Buttons();
        setTipAnimal();

    }

    private void Buttons()
    {
        ButtonAplicar();
        ButtonVoltar();
    }

    private void InpToVar()
    {
        editTextDesc = findViewById(R.id.cad_descricao);
        editTextRaca = findViewById(R.id.cad_raca);

        inp_tip_animal = findViewById(R.id.cad_animal);

        inp_sel_port = findViewById(R.id.radio_port);
        inp_sel_vac = findViewById(R.id.radio_vac);
        inp_sel_stat = findViewById(R.id.radio_stat);

        buttonAplicar = findViewById(R.id.button_aplicar);
        but_voltar = findViewById(R.id.voltarDButton);
    }


    private void ButtonAplicar()
    {
        buttonAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetRadioText();
                RadioTxtToStg();
                //Colocar dados do animal
                CreateAn();
            }
        });
    }

    private void RadioTxtToStg()
    {
        an_port = but_rad_port.getText().toString().trim();
        an_vac = but_rad_vac.getText().toString().trim();
        an_stat = but_rad_stat.getText().toString();
        an_raca = editTextRaca.getText().toString().trim();
        an_desc = editTextDesc.getText().toString().trim();
    }

    private void SetRadioText()
    {
        int selectedid_port = inp_sel_port.getCheckedRadioButtonId();
        int selectedid_vac = inp_sel_vac.getCheckedRadioButtonId();
        int selectedid_stat = inp_sel_stat.getCheckedRadioButtonId();
        but_rad_port = findViewById(selectedid_port);
        but_rad_vac = findViewById(selectedid_vac);
        but_rad_stat = findViewById(selectedid_stat);
    }

    private void setTipAnimal(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.tip_animal, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inp_tip_animal.setAdapter(adapter);
        inp_tip_animal.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position > 0)
        {
            this.tip_animal = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //colocando dados an
    private void CreateAn()
    {
        String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        Animal an = new Animal(tip_animal, an_port, an_vac, an_raca, an_stat, an_desc, user_id);
        HashMap<String, Object> newPost = new HashMap<>();
        newPost.put("Alguma coisa", an.toMap());
        ref.updateChildren(newPost);
    }

    private void ButtonVoltar(){
        but_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CadAnimal.this, PerfilActivity.class));
            }
        });
    }
}
