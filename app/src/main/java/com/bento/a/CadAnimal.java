package com.bento.a;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;


public class CadAnimal extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageButton but_voltar;
    private EditText editTextRaca, editTextDesc;
    private Button buttonAplicar;
    private Spinner inp_tip_animal;
    private String tip_status, tip_animal;
    private String[] spinnerValue = {
            "Cachorro",
            "Gato",
            "Outro"
    };

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
        ButtonVoltar();
    }

    private void InpToVar()
    {
        but_voltar = findViewById(R.id.voltarDButton);
        editTextDesc = findViewById(R.id.cad_descricao);
        editTextRaca = findViewById(R.id.cad_raca);
        buttonAplicar = findViewById(R.id.button_aplicar);
        inp_tip_animal = findViewById(R.id.cad_animal);
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

    private void ButtonVoltar(){
        but_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CadAnimal.this, PerfilActivity.class));
            }
        });
    }
}
