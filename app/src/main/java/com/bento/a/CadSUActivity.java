package com.bento.a;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bento.a.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.santalu.maskedittext.MaskEditText;

import java.util.HashMap;
import java.util.Objects;

public class CadSUActivity extends AppCompatActivity {

    private EditText cad_nom_inp;
    private MaskEditText cad_cpf_inp, cad_cep_inp, cad_rg_inp, cad_tel_inp, cad_nasc_inp;
    private String nome_comp, tip_usu, cpf, cep, telefone, nascimento, rg, nom_usu, email, senha;
    private Button but_cad, but_vol;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_su_layout);

        inputToVar();
        intentGetVar();

        buttonVoltar(but_vol);
        buttonCadIr(but_cad);
    }

    private void inputToVar() {

        cad_nom_inp = findViewById(R.id.cad_nome_inp);
        cad_cpf_inp = findViewById(R.id.cad_cpf_inp);
        cad_cep_inp = findViewById(R.id.cad_cep_inp);
        cad_tel_inp = findViewById(R.id.cad_tel_inp);
        cad_nasc_inp = findViewById(R.id.cad_nasc_inp);
        cad_rg_inp = findViewById(R.id.cad_rg_inp);

        but_cad = findViewById(R.id.cad_but_cads);
        but_vol = findViewById(R.id.cad_voltar_but);
    }

    private void buttonVoltar(Button butt_vol)
    {
        butt_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CadSUActivity.this, CadActivity.class));
            }
        });
    }

    private void buttonCadIr(Button butt_cad)
    {
        butt_cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textToString();

                switch(verfCamps(nome_comp, cpf, cep, nascimento, telefone, rg)) {

                    case 1:
                        Toast.makeText(CadSUActivity.this, "Preencha os campos", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(CadSUActivity.this, "Preencha inteiramente o CPF", Toast.LENGTH_SHORT).show();
                        cad_cpf_inp.requestFocus();
                        break;
                    case 3:
                        Toast.makeText(CadSUActivity.this, "Preencha inteiramente o CEP", Toast.LENGTH_SHORT).show();
                        cad_cep_inp.requestFocus();
                        break;
                    case 4:
                        Toast.makeText(CadSUActivity.this, "Preencha inteiramente o Telefone", Toast.LENGTH_SHORT).show();
                        cad_tel_inp.requestFocus();
                        break;
                    case 5:
                        Toast.makeText(CadSUActivity.this, "Preencha com a sua data de nascimento", Toast.LENGTH_SHORT).show();
                        cad_nasc_inp.requestFocus();
                        break;
                    case 6:
                        Toast.makeText(CadSUActivity.this, "Preencha inteiramente o RG", Toast.LENGTH_SHORT).show();
                        cad_rg_inp.requestFocus();
                    case 0:
                        connectDB(email, senha);
                        break;
                    default:
                        Toast.makeText(CadSUActivity.this, "Um erro inesperado aconteceu", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private static int verfCamps(String nome, String cpf, String cep, String nascimento, String telefone, String rg)
    {
        if(nome.isEmpty() || cpf.isEmpty() || cep.isEmpty() || telefone.isEmpty() || rg.isEmpty())
        {
            return 1;
        }
        if(!cpf.matches("^([0-9]{3}\\.?){3}-?[0-9]{2}$"))
        {
            return 2;
        }
        if(!cep.matches("^\\d{5}[-]\\d{3}$"))
        {
            return 3;
        }
        if(!telefone.matches("^\\(\\d{2}\\)\\d{5}-?\\d{4}$"))
        {
            return 4;
        }
        if(!nascimento.matches("^\\d{2}[/]\\d{2}[/]\\d{4}$"))
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
        this.nome_comp = cad_nom_inp.getText().toString().trim();
        this.cpf = Objects.requireNonNull(cad_cpf_inp.getText()).toString().trim();
        this.cep = Objects.requireNonNull(cad_cep_inp.getText()).toString().trim();
        this.telefone = Objects.requireNonNull(cad_tel_inp.getText()).toString().trim();
        this.nascimento = Objects.requireNonNull(cad_nasc_inp.getText()).toString().trim();
        this.rg = Objects.requireNonNull(cad_rg_inp.getText()).toString().trim();
    }

    private void intentGetVar()
    {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        this.nom_usu = bundle.getString("nom_usu");
        this.tip_usu = bundle.getString("tip_usu");
        this.senha = bundle.getString("senha");
        this.email = bundle.getString("email");
    }

    private void connectDB(String email, String senha)
    {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(CadSUActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            dataDB();
                            Toast.makeText(CadSUActivity.this, "Cadastro efetuado", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(CadSUActivity.this, LoginActivity.class));
                        }
                        else
                        {
                            exceptionsFire(task);
                        }
                    }
                });
    }

    //metodo para cadastrar informações no bd
    private void dataDB()
    {
        String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        User user = new User(nom_usu, tip_usu, nome_comp, cpf, cep, telefone, nascimento, rg);
        HashMap<String, Object> newPost = new HashMap<>();
        newPost.put(user_id, user.toMap());
        ref.setValue(newPost);
    }

    //metodo de exceção do Firebase
    private void exceptionsFire(Task<AuthResult> task)
    {
        try
            {
                throw Objects.requireNonNull(task.getException());
            }
        catch(FirebaseAuthWeakPasswordException e)
            {
                Toast.makeText(CadSUActivity.this, "Senha muito fraca.", Toast.LENGTH_LONG).show();
            }
        catch(FirebaseAuthInvalidCredentialsException e)
            {
                Toast.makeText(CadSUActivity.this, "O e-mail é inválido.", Toast.LENGTH_LONG).show();
            }
        catch(FirebaseAuthUserCollisionException e)
            {
                Toast.makeText(CadSUActivity.this, "O e-mail ou senha já estão cadastrados.", Toast.LENGTH_LONG).show();
            }
        catch(Exception e)
            {
                Log.e("ERRCad", e.getMessage());
            }
    }

}
