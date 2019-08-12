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
    private EditText editTextAnimal, editTextCidade, editTextRaca, editTextDesc;
    private Button buttonAplicar;
    private Spinner inp_tip_status;
    private String tip_status;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_dog_layout);

        InpToVar();
        Buttons();
        setTipStatus();
    }

    private void Buttons()
    {
        ButtonVoltar();
    }

    private void InpToVar()
    {
        but_voltar = findViewById(R.id.voltarDButton);
        editTextAnimal = findViewById(R.id.cad_animal);
        editTextCidade = findViewById(R.id.cad_cidade);
        editTextDesc = findViewById(R.id.cad_descricao);
        editTextRaca = findViewById(R.id.cad_raca);
        buttonAplicar = findViewById(R.id.button_aplicar);
        inp_tip_status = findViewById(R.id.cad_status);
    }

    private void setTipStatus(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.tip_status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inp_tip_status.setAdapter(adapter);
        inp_tip_status.setOnItemSelectedListener(this);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.tip_status = parent.getItemAtPosition(position).toString();
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
