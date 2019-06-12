package com.bento.a;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bento.a.classes.class_User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class CadActivity extends AppCompatActivity {

    EditText inp_nom_usu, inp_tip_usu, inp_email;
    android.support.design.widget.TextInputEditText inp_senha, inp_conf_senha;
    TextView alert_camps_text;
    private String nom_usu, tip_usu, email, senha, conf_senha;
    Button but_cad_prox, but_cad_volt;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private class_User cadastrar = new class_User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_layout);

        //input dos textos
            inp_nom_usu = findViewById(R.id.cad_nom_usu_inp);
            inp_tip_usu = findViewById(R.id.cad_tip_usu_inp);
            inp_email = findViewById(R.id.cad_email_inp);
            inp_senha = findViewById(R.id.cad_senha_inp);
            inp_conf_senha = findViewById(R.id.cad_conf_senha_inp);
        //-

        //alertas
            alert_camps_text = findViewById(R.id.cad_att_pr_camps_text);
        //-

        //botao de voltar e proximo
        but_cad_prox = findViewById(R.id.cad_but_prox);
        but_cad_volt = findViewById(R.id.cad_but_vol);

        but_cad_prox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //edittext para string
                nom_usu = inp_nom_usu.getText().toString().trim();
                tip_usu = inp_tip_usu.getText().toString().trim();
                email = inp_email.getText().toString().trim();
                senha = Objects.requireNonNull(inp_senha.getText()).toString().trim();
                conf_senha = Objects.requireNonNull(inp_conf_senha.getText()).toString().trim();

                if(nom_usu.isEmpty() || tip_usu.isEmpty() || email.isEmpty() || senha.isEmpty() || conf_senha.isEmpty())
                    {
                        Toast.makeText(CadActivity.this,"Preencha os campos",Toast.LENGTH_SHORT).show();
                    }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    Toast.makeText(CadActivity.this,"Email não válido",Toast.LENGTH_SHORT).show();
                }
                else if(senha.length() < 6 || conf_senha.length() <= 6)
                    {
                        Toast.makeText(CadActivity.this,"Senhas precisam de mais de 6 letras",Toast.LENGTH_SHORT).show();
                    }
                else if(!senha.equals(conf_senha))
                    {
                        Toast.makeText(CadActivity.this,"Senhas estão diferentes",Toast.LENGTH_SHORT).show();
                    }
                else
                    {
                        //function cadastrar e ir para a próxima activity
                        cadastrar.setCadastro(nom_usu, tip_usu, email, senha, conf_senha);
                        //mandar para o db
                        //-

                        mAuth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(CadActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(CadActivity.this, "Cadastro efetuado", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(CadActivity.this, LoginActivity.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(CadActivity.this, "Cadastro não efetuado", Toast.LENGTH_LONG).show();
                                }

                                // ...
                            }
                        });
                    }
            }
        });


        but_cad_volt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CadActivity.this, CadSActivity.class));
            }
        });
        //-
    }
}
