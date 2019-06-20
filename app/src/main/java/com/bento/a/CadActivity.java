package com.bento.a;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Objects;


public class CadActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText inp_nom_usu, inp_email;
    private android.support.design.widget.TextInputEditText inp_senha, inp_conf_senha;
    private Button but_cad_prox, but_cad_volt;
    private Spinner inp_tip_usu;

    private String nom_usu, tip_usu, email, senha, conf_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_layout);

        findViewIDs();

        setTipUsu();

        buttonBack(but_cad_volt);
        buttonRegister(but_cad_prox);

    }

    private void buttonBack(Button butt_vol)
    {
        butt_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CadActivity.this, LoginActivity.class));
            }
        });
    }

    private void findViewIDs()
    {
        inp_nom_usu = findViewById(R.id.cad_nom_usu_inp);
        inp_tip_usu = findViewById(R.id.cad_tip_usu_inp);
        inp_email = findViewById(R.id.cad_email_inp);
        inp_senha = findViewById(R.id.cad_senha_inp);
        inp_conf_senha = findViewById(R.id.cad_conf_senha_inp);

        but_cad_prox = findViewById(R.id.cad_but_prox);
        but_cad_volt = findViewById(R.id.cad_but_vol);
    }

    private void buttonRegister(Button butt_cad)
    {
        butt_cad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                inputToString(inp_nom_usu, inp_email, inp_senha, inp_conf_senha);

                switch (verifyRegister(nom_usu, tip_usu, email, senha, conf_senha)){
                    case 1:
                        Toast.makeText(CadActivity.this,"Preencha o campo nome",Toast.LENGTH_SHORT).show();
                        inp_nom_usu.requestFocus();
                        break;
                    case 2:
                        Toast.makeText(CadActivity.this,"Preencha o campo do tipo de usuário",Toast.LENGTH_SHORT).show();
                        inp_tip_usu.requestFocus();
                        break;
                    case 3:
                        Toast.makeText(CadActivity.this,"Preencha o campo do e-mail",Toast.LENGTH_SHORT).show();
                        inp_email.requestFocus();
                        break;
                    case 4:
                        Toast.makeText(CadActivity.this,"Preencha o campo da senha",Toast.LENGTH_SHORT).show();
                        inp_senha.requestFocus();
                        break;
                    case 5:
                        Toast.makeText(CadActivity.this,"Preencha o campo da confirmação da senha",Toast.LENGTH_SHORT).show();
                        inp_conf_senha.requestFocus();
                        break;
                    case 6:
                        Toast.makeText(CadActivity.this,"E-mail não válido",Toast.LENGTH_SHORT).show();
                        inp_email.requestFocus();
                        break;
                    case 7:
                        Toast.makeText(CadActivity.this,"Senhas precisam de mais de 6 letras",Toast.LENGTH_SHORT).show();
                        inp_senha.requestFocus();
                        break;
                    case 8:
                        Toast.makeText(CadActivity.this,"Senhas não são iguais",Toast.LENGTH_SHORT).show();
                        inp_senha.requestFocus();
                        break;
                    case 0:
                        if(tip_usu.equals("Usuário"))
                        {
                            startActivity(new Intent(CadActivity.this, CadSUActivity.class)
                                    .putExtra("nom_usu",nom_usu)
                                    .putExtra("tip_usu",tip_usu)
                                    .putExtra("senha", senha)
                                    .putExtra("email", email));
                        }
                        else if(tip_usu.equals("Empresa"))
                        {
                            startActivity(new Intent(CadActivity.this, CadSEActivity.class)
                                    .putExtra("nom_usu",nom_usu)
                                    .putExtra("tip_usu",tip_usu)
                                    .putExtra("senha", senha)
                                    .putExtra("email", email));
                        }
                        break;
                    default:
                        Toast.makeText(CadActivity.this,"Um erro inesperado aconteceu",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void inputToString(EditText inp_nom_usu, EditText inp_email, android.support.design.widget.TextInputEditText inp_senha, android.support.design.widget.TextInputEditText inp_conf_senha)
    {
        this.nom_usu = inp_nom_usu.getText().toString().trim();
        this.email = inp_email.getText().toString().trim();
        this.senha = Objects.requireNonNull(inp_senha.getText()).toString().trim();
        this.conf_senha = Objects.requireNonNull(inp_conf_senha.getText()).toString().trim();

    }

    private int verifyRegister(String nom_usu, String tip_usu, String email, String senha, String conf_senha)
    {
        if(nom_usu.isEmpty())
        {
            return 1;
        }
        else if(tip_usu.isEmpty())
        {
            return 2;
        }
        else if(email.isEmpty())
        {
            return 3;
        }
        else if(senha.isEmpty())
        {
            return 4;
        }
        else if(conf_senha.isEmpty())
        {
            return 5;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            return 6;
        }
        else if(senha.length() < 6 || conf_senha.length() <= 6)
        {
            return 7;
        }
        else if(!senha.equals(conf_senha))
        {
            return 8;
        }
        else
        {
            return 0;
        }
    }


    //metodo para retornar array do tip_usuario
    private void setTipUsu()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.tip_usu, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inp_tip_usu.setAdapter(adapter);
        inp_tip_usu.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.tip_usu = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}