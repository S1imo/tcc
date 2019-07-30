package com.bento.a;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.santalu.maskedittext.MaskEditText;

import java.util.HashMap;
import java.util.Objects;

public class EditPerfUActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Button but_aplicar, but_voltar;
    private MaskEditText  novo_cep_inp, novo_cpf_inp, novo_dat_inp, novo_rg_inp, novo_tel_inp;
    private EditText novo_nom_usu_inp;
    private String novo_nom_usu, novo_cep, novo_cpf, novo_dat, novo_rg, novo_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_perfu_layout);

        InputToVar();

        ButtonAplicar();
        ButtonVoltar();
    }

    private void InputToVar()
    {
        novo_nom_usu_inp = findViewById(R.id.edit_nom);
        novo_cep_inp = findViewById(R.id.edit_cep);
        novo_cpf_inp = findViewById(R.id.edit_cpf);
        novo_dat_inp = findViewById(R.id.edit_data);
        novo_rg_inp = findViewById(R.id.edit_rg);
        novo_tel_inp = findViewById(R.id.edit_tel);

        but_aplicar = findViewById(R.id.buttonAplicar);
        but_voltar = findViewById(R.id.buttonVoltar);
    }
    private void InpToString()
    {
        novo_nom_usu = novo_nom_usu_inp.getText().toString().trim();
        novo_cep = Objects.requireNonNull(novo_cep_inp.getText()).toString().trim();
        novo_cpf = Objects.requireNonNull(novo_cpf_inp.getText()).toString().trim();
        novo_dat = Objects.requireNonNull(novo_dat_inp.getText()).toString().trim();
        novo_rg = Objects.requireNonNull(novo_rg_inp.getText()).toString().trim();
        novo_tel = Objects.requireNonNull(novo_tel_inp.getText()).toString().trim();
    }

    private void ButtonVoltar()
    {
        but_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditPerfUActivity.this, PerfilActivity.class));
            }
        });
    }

    private void ButtonAplicar()
    {
        but_aplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InpToString();
                switch(verCampos())
                {
                    case 1:
                        Toast.makeText(EditPerfUActivity.this, "Preencha os campos", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(EditPerfUActivity.this, "Preencha o CEP corretamente", Toast.LENGTH_SHORT).show();
                        novo_cep_inp.requestFocus();
                        break;
                    case 3:
                        Toast.makeText(EditPerfUActivity.this, "Preencha o CPF corretamente", Toast.LENGTH_SHORT).show();
                        novo_cpf_inp.requestFocus();
                        break;
                    case 4:
                        Toast.makeText(EditPerfUActivity.this, "Preencha o número de telefone corretamente", Toast.LENGTH_SHORT).show();
                        novo_tel_inp.requestFocus();
                        break;
                    case 5:
                        Toast.makeText(EditPerfUActivity.this, "Preencha a data de nascimento corretamente", Toast.LENGTH_SHORT).show();
                        novo_dat_inp.requestFocus();
                        break;
                    case 6:
                        Toast.makeText(EditPerfUActivity.this, "Preencha o RG corretamente", Toast.LENGTH_SHORT).show();
                        novo_rg_inp.requestFocus();
                        break;
                    case 0:
                        ChangeData();
                        break;
                    default:
                        Toast.makeText(EditPerfUActivity.this, "Um erro inesperado ocorreu", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private int verCampos()
    {
        if(novo_nom_usu.isEmpty() || novo_cep.isEmpty() || novo_cpf.isEmpty() || novo_tel.isEmpty() || novo_dat.isEmpty() || novo_rg.isEmpty())
        {
            return 1;
        }
        if(novo_cep.matches("^\\d{5}[-]\\d{3}$"))
        {
            return 2;
        }
        if(novo_cpf.matches("^([0-9]{3}\\.?){3}-?[0-9]{2}$"))
        {
            return 3;
        }
        if(novo_tel.matches("^\\(\\d{2}\\)\\d{5}-?\\d{4}$"))
        {
            return 4;
        }
        if(novo_dat.matches("^\\d{2}[/]\\d{2}[/]\\d{4}$"))
        {
            return 5;
        }
        if(novo_rg.matches("^\\d{2}\\.?\\d{3}\\.?\\d{3}\\.?-?\\d{1}$"))
        {
            return 6;
        }
        else
        {
            return 0;
        }
    }

    private void ChangeData() {
        final String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Comum").child(user_id);
        HashMap<String, String> newPost = new HashMap<>();
        newPost.put("Nome_Usuario",novo_nom_usu);
        newPost.put("CEP", novo_cep);
        newPost.put("CPF", novo_cpf);
        newPost.put("Telefone", novo_tel);
        newPost.put("Data_de_Nascimento", novo_dat);
        newPost.put("RG", novo_rg);
        ref.setValue(newPost).addOnCompleteListener(EditPerfUActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(EditPerfUActivity.this, "Mudanças aplicadas", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditPerfUActivity.this, PerfilActivity.class));
                }
                else
                {
                    String error = Objects.requireNonNull(task.getException()).toString();
                    Toast.makeText(EditPerfUActivity.this, "Não foi possível salvar: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
