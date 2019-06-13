package com.bento.a;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
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

import java.util.HashMap;
import java.util.Objects;


public class CadActivity extends AppCompatActivity {

    EditText inp_nom_usu, inp_tip_usu, inp_email;
    android.support.design.widget.TextInputEditText inp_senha, inp_conf_senha;
    Button but_cad_prox, but_cad_volt;

    private String nom_usu, tip_usu, email, senha, conf_senha, nome_comp, cpf, cep, telefone, rg;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_layout);

        inp_nom_usu = findViewById(R.id.cad_nom_usu_inp);
        inp_tip_usu = findViewById(R.id.cad_tip_usu_inp);
        inp_email = findViewById(R.id.cad_email_inp);
        inp_senha = findViewById(R.id.cad_senha_inp);
        inp_conf_senha = findViewById(R.id.cad_conf_senha_inp);

        but_cad_prox = findViewById(R.id.cad_but_prox);
        but_cad_volt = findViewById(R.id.cad_but_vol);

        buttonBack(but_cad_volt);
        buttonRegister(but_cad_prox);

    }

    private void buttonBack(Button butt_vol)
    {
        butt_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CadActivity.this, CadSActivity.class));
            }
        });
    }

    private void buttonRegister(Button butt_cad)
    {
        butt_cad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                inputToString(inp_nom_usu, inp_tip_usu, inp_email, inp_senha, inp_conf_senha);

                switch (verifyRegister(nom_usu, tip_usu, email, senha, conf_senha)){
                    case 1:
                        Toast.makeText(CadActivity.this,"Preencha os campos",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(CadActivity.this,"Email não válido",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(CadActivity.this,"Senhas precisam de mais de 6 letras",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(CadActivity.this,"Senhas não são iguais",Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        connectDB(email, senha);
                        break;
                    default:
                        Toast.makeText(CadActivity.this,"Um erro inesperado aconteceu",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void inputToString(EditText inp_nom_usu, EditText inp_tip_usu, EditText inp_email, android.support.design.widget.TextInputEditText inp_senha, android.support.design.widget.TextInputEditText inp_conf_senha)
    {
        this.nom_usu = inp_nom_usu.getText().toString().trim();
        this.tip_usu = inp_tip_usu.getText().toString().trim();
        this.email = inp_email.getText().toString().trim();
        this.senha = Objects.requireNonNull(inp_senha.getText()).toString().trim();
        this.conf_senha = Objects.requireNonNull(inp_conf_senha.getText()).toString().trim();
    }

    private int verifyRegister(String nom_usu, String tip_usu, String email, String senha, String conf_senha)
    {
        if(nom_usu.isEmpty() || tip_usu.isEmpty() || email.isEmpty() || senha.isEmpty() || conf_senha.isEmpty())
        {
            return 1;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            return 2;
        }
        else if(senha.length() < 6 || conf_senha.length() <= 6)
        {
            return 3;
        }
        else if(!senha.equals(conf_senha))
        {
            return 4;
        }
        else
        {
            return 0;
        }
    }

    private void connectDB(String email, String senha)
    {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(CadActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(CadActivity.this, "Cadastro efetuado", Toast.LENGTH_LONG).show();
                            dataDB();
                            startActivity(new Intent(CadActivity.this, LoginActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(CadActivity.this, "Cadastro não efetuado", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void intentGetVar()
    {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        this.nome_comp = bundle.getString("nome_comp");
        this.cpf = bundle.getString("cpf");
        this.cep = bundle.getString("cep");
        this.telefone = bundle.getString("telefone");
        this.rg = bundle.getString("rg");
    }

    private void dataDB()
    {
        String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        intentGetVar();
        HashMap<String, String> dataInfo = new HashMap<>();
            dataInfo.put("nome_user",nom_usu);
            dataInfo.put("tip_user",tip_usu);
            dataInfo.put("nome_comp_user", nome_comp);
            dataInfo.put("CPF", cpf);
            dataInfo.put("CEP", cep);
            dataInfo.put("Telefone", telefone);
            dataInfo.put("RG", rg);

        current_user_db.setValue(dataInfo);
    }
}
