package com.bento.a;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class EsqueceuSenhaActivity extends AppCompatActivity {

    private Button but_env, but_vol;
    private EditText inp_email_es;
    private String userEmail;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esqueceu_senha_layout);
        inputToVar();
        buttonEnvFunc();
        buttonVolFunc();
    }

    private void inputToVar()
    {
        but_env = findViewById(R.id.but_env_es);
        but_vol = findViewById(R.id.but_vol_es);
        inp_email_es = findViewById(R.id.email_es_inp);
    }

    private void resetingPass()
    {
        userEmail = inp_email_es.getText().toString().trim();
        if(userEmail.isEmpty())
        {
            Toast.makeText(EsqueceuSenhaActivity.this, "Coloque um e-mail", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(EsqueceuSenhaActivity.this, "Cheque seu e-mail", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EsqueceuSenhaActivity.this, LoginActivity.class));
                    }
                    else
                    {
                        String message = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(EsqueceuSenhaActivity.this, "E-mail inválido", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    private void buttonVolFunc()
    {
        but_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EsqueceuSenhaActivity.this, LoginActivity.class));
            }
        });
    }

    private void buttonEnvFunc()
    {
        but_env.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetingPass();
            }
        });
    }
}
