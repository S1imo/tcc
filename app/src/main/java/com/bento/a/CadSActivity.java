package com.bento.a;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.santalu.maskedittext.MaskEditText;
import java.util.Objects;

public class CadSActivity extends AppCompatActivity {

    private EditText cad_nom_inp;
    private MaskEditText cad_cpf_inp, cad_cep_inp, cad_rg_inp, cad_tel_inp;
    private String nome, cpf, cep, telefone, rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_s_layout);

        cad_nom_inp = findViewById(R.id.cad_nome_inp);
        cad_cpf_inp = findViewById(R.id.cad_cpf_inp);
        cad_cep_inp = findViewById(R.id.cad_cep_inp);
        cad_tel_inp = findViewById(R.id.cad_tel_inp);
        cad_rg_inp = findViewById(R.id.cad_rg_inp);

        Button but_cad = findViewById(R.id.cad_but_cads);
        Button but_vol = findViewById(R.id.cad_voltar_but);

        buttonVoltar(but_vol);
        buttonCadIr(but_cad);
    }

    private void buttonVoltar(Button butt_vol)
    {
        butt_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CadSActivity.this, LoginActivity.class));
            }
        });
    }

    private void buttonCadIr(Button butt_cad)
    {
        butt_cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textToString();

                switch(verfCamps(nome, cpf, cep, telefone, rg)) {

                    case 1:
                        Toast.makeText(CadSActivity.this, "Preencha os campos", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(CadSActivity.this, "O campo nome não pode conter numeros", Toast.LENGTH_SHORT).show();
                        cad_nom_inp.requestFocus();
                        break;
                    case 3:
                        Toast.makeText(CadSActivity.this, "Preencha inteiramente o CPF", Toast.LENGTH_SHORT).show();
                        cad_cpf_inp.requestFocus();
                        break;
                    case 4:
                        Toast.makeText(CadSActivity.this, "Preencha inteiramente o CEP", Toast.LENGTH_SHORT).show();
                        cad_cep_inp.requestFocus();
                        break;
                    case 5:
                        Toast.makeText(CadSActivity.this, "Preencha inteiramente o Telefone", Toast.LENGTH_SHORT).show();
                        cad_tel_inp.requestFocus();
                        break;
                    case 6:
                        Toast.makeText(CadSActivity.this, "Preencha inteiramente o RG", Toast.LENGTH_SHORT).show();
                        cad_rg_inp.requestFocus();
                        break;
                    case 0:
                        startActivity(new Intent(CadSActivity.this, CadActivity.class)
                                .putExtra("nome_comp",nome)
                                .putExtra("cpf",cpf)
                                .putExtra("cep",cep)
                                .putExtra("telefone",telefone)
                                .putExtra("rg",rg));
                        break;
                    default:
                        Toast.makeText(CadSActivity.this, "Um erro inesperado aconteceu", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private static int verfCamps(String nome, String cpf, String cep, String telefone, String rg)
    {
        if(nome.isEmpty() || cpf.isEmpty() || cep.isEmpty() || telefone.isEmpty() || rg.isEmpty())
        {
            return 1;
        }
        if(!nome.matches("^[a-zA-Z]*$"))
        {
            return 2;
        }
        if(!cpf.matches("^([0-9]{3}\\.?){3}-?[0-9]{2}$"))
        {
            return 3;
        }
        if(!cep.matches("^\\d{5}[-]\\d{3}$"))
        {
            return 4;
        }
        if(!telefone.matches("^\\(\\d{2}\\)\\d{5}-?\\d{4}$"))
        {
            return 5;
        }
        if(!rg.matches("^\\d{2}\\.?\\d{3}\\.?\\d{3}\\.?-?\\d{1}$"))
        {
            return 6;
        }
        else
        {
            return 0;
        }
    }

    private void textToString()
    {
        this.nome = cad_nom_inp.getText().toString().trim();
        this.cpf = Objects.requireNonNull(cad_cpf_inp.getText()).toString().trim();
        this.cep = Objects.requireNonNull(cad_cep_inp.getText()).toString().trim();
        this.telefone = Objects.requireNonNull(cad_tel_inp.getText()).toString().trim();
        this.rg = Objects.requireNonNull(cad_rg_inp.getText()).toString().trim();
    }


}
