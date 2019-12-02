package com.bento.a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadLoja extends AppCompatActivity {

    private ImageButton but_voltar;
    private Button but_cad;
    private CircleImageView img1, img2;
    private EditText cad_nome, cad_qtd, cad_valor, cad_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_loja);

        InpToVar();
        Buttons();

    }

    private void Buttons(){
        ButtonVoltar();
        ButtonCad();

    }

    private void InpToVar(){
        img1 = findViewById(R.id.image_View1);
        img2 = findViewById(R.id.image_View2);

        but_voltar = findViewById(R.id.voltarLButton);
        but_cad = findViewById(R.id.but_cad_prod);

        cad_nome = findViewById(R.id.cad_nome_prod);
        cad_qtd = findViewById(R.id.cad_qtd_prod);
        cad_valor = findViewById(R.id.cad_valor_prod);
        cad_desc = findViewById(R.id.cad_desc_prod);
    }

        private void ButtonVoltar(){
            but_voltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CadLoja.this, PerfilActivity.class));
                }
            });
        }

        private void ButtonCad(){

        }

}
