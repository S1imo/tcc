package com.bento.a;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bento.a.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskedittext.MaskEditText;

import java.util.HashMap;
import java.util.Objects;

public class EditPerfEActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Button but_aplicar, but_voltar;
    private MaskEditText novo_cep_inp, novo_cnpj_inp, novo_tel_inp;
    private EditText novo_nom_usu_inp, novo_nom_emp_inp;
    private String novo_nom_usu, tip_usu, novo_nom_emp, novo_cep, novo_cnpj, novo_tel;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_perfe_layout);

        InputToVar();
        SetValues();
        ButtonAplicar();
        ButtonVoltar();
    }

    private void SetValues() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user_id = user.getUid();
        myRef = mFirebaseDatabase.getReference("Users/"+user_id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                novo_nom_emp_inp.setText(user.getUs_nome());
                novo_nom_usu_inp.setText(user.getUs_nome_comp());
                tip_usu = user.getUs_tip_usu();
                novo_cnpj_inp.setText(user.getUs_cnpj());
                novo_cep_inp.setText(user.getUs_cep());
                novo_tel_inp.setText(user.getUs_tel());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void InputToVar() {
        novo_nom_emp_inp = findViewById(R.id.edit_nomEU);
        novo_nom_usu_inp = findViewById(R.id.edit_nomE);
        novo_cep_inp = findViewById(R.id.edit_cepE);
        novo_cnpj_inp = findViewById(R.id.edit_cnpjE);
        novo_tel_inp = findViewById(R.id.edit_telE);

        but_aplicar = findViewById(R.id.buttonAplicar);
        but_voltar = findViewById(R.id.buttonVoltar);
    }

    private void InpToString() {
        novo_nom_usu = novo_nom_usu_inp.getText().toString().trim();
        novo_nom_emp = novo_nom_emp_inp.getText().toString().trim();
        novo_cep = Objects.requireNonNull(novo_cep_inp.getText()).toString().trim();
        novo_cnpj = Objects.requireNonNull(novo_cnpj_inp.getText()).toString().trim();
        novo_tel = Objects.requireNonNull(novo_tel_inp.getText()).toString().trim();
    }

    private void ButtonVoltar() {
        but_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditPerfEActivity.this, PerfilActivity.class));
            }
        });
    }

    private void ButtonAplicar() {
        but_aplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InpToString();
                switch (verCampos()) {
                    case 1:
                        Toast.makeText(EditPerfEActivity.this, "Preencha os campos", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(EditPerfEActivity.this, "Preencha o CEP corretamente", Toast.LENGTH_SHORT).show();
                        novo_cep_inp.requestFocus();
                        break;
                    case 3:
                        Toast.makeText(EditPerfEActivity.this, "Preencha o CNPJ corretamente", Toast.LENGTH_SHORT).show();
                        novo_cnpj_inp.requestFocus();
                        break;
                    case 4:
                        Toast.makeText(EditPerfEActivity.this, "Preencha o número de telefone corretamente", Toast.LENGTH_SHORT).show();
                        novo_tel_inp.requestFocus();
                        break;
                    case 0:
                        ChangeData();
                        break;
                    default:
                        Toast.makeText(EditPerfEActivity.this, "Um erro inesperado ocorreu", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private int verCampos() {
        if (novo_nom_usu.isEmpty() || novo_cep.isEmpty() || novo_cnpj.isEmpty() || novo_tel.isEmpty()) {
            return 1;
        }
        if (!novo_cep.matches("^\\d{5}[-]\\d{3}$")) {
            return 2;
        }
        if (!novo_cnpj.matches("^\\d{2}\\.\\d{3}\\.\\d{3}[/]\\d{4}[-]\\d{2}$")) {
            return 3;
        }
        if (!novo_tel.matches("^\\(\\d{2}\\)\\d{5}-?\\d{4}$")) {
            return 4;
        }
        else
        {
            return 0;
        }
    }

    private void ChangeData() {
        String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        User user = new User(novo_nom_usu,tip_usu, novo_nom_emp, novo_cep, novo_cnpj, novo_tel);
        HashMap<String, Object> newPost = new HashMap<>();
        newPost.put(user_id, user.toMap());
        ref.updateChildren(newPost).addOnCompleteListener(EditPerfEActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(EditPerfEActivity.this, "Mudanças aplicadas", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditPerfEActivity.this, PerfilActivity.class));
                }
                else
                {
                    String error = Objects.requireNonNull(task.getException()).toString();
                    Toast.makeText(EditPerfEActivity.this, "Não foi possível salvar: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}