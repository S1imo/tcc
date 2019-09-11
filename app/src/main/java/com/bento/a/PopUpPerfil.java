package com.bento.a;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PopUpPerfil extends AppCompatActivity {
    private TextView exitText,porteani, idade_animal, vacinado, anistatus;
    private EditText raca_ani, desc_ani;
    private Button but_edit, but_voltar, but_aply;
    private RadioButton grande, medio, pequeno, ani_filhote, ani_adulto, vac_sim, vac_nao, status_adot, status_perdido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_perfil);

        InpToVar();

        ButtonBack();
        ButtonEdit();
        ButtonVoltar();
        ButtonAply();

    }

    private void InpToVar()
    {
        exitText = findViewById(R.id.exit_text);
        but_edit = findViewById(R.id.but_editar);
        but_voltar = findViewById(R.id.but_back);
        but_aply = findViewById(R.id.but_aplicar);

        grande = findViewById(R.id.porte_grande);
        medio = findViewById(R.id.porte_medio);
        pequeno = findViewById(R.id.porte_pequeno);
        porteani = findViewById(R.id.porte_animal);

        ani_adulto = findViewById(R.id.adulto);
        ani_filhote = findViewById(R.id.filhote);
        idade_animal = findViewById(R.id.idade_ani);

        vacinado = findViewById(R.id.vacinado_animal);
        vac_sim = findViewById(R.id.vacinado_sim);
        vac_nao = findViewById(R.id.vacinado_nao);

        anistatus = findViewById(R.id.status_animal);
        status_adot = findViewById(R.id.ani_adot);
        status_perdido = findViewById(R.id.ani_perdido);

        raca_ani = findViewById(R.id.raca_animal);
        desc_ani = findViewById(R.id.desc_animal);

    }

    private void ButtonBack()
    {
        exitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void ButtonEdit(){
        but_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                porteani.setVisibility(View.GONE);
                grande.setVisibility(View.VISIBLE);
                medio.setVisibility(View.VISIBLE);
                pequeno.setVisibility(View.VISIBLE);

                idade_animal.setVisibility(View.GONE);
                ani_filhote.setVisibility(View.VISIBLE);
                ani_adulto.setVisibility(View.VISIBLE);

                vacinado.setVisibility(View.GONE);
                vac_sim.setVisibility(View.VISIBLE);
                vac_nao.setVisibility(View.VISIBLE);

                anistatus.setVisibility(View.GONE);
                status_adot.setVisibility(View.VISIBLE);
                status_perdido.setVisibility(View.VISIBLE);

                but_edit.setVisibility(View.GONE);
                but_voltar.setVisibility(View.VISIBLE);
                but_aply.setVisibility(View.VISIBLE);

                raca_ani.setEnabled(true);
                desc_ani.setEnabled(true);

            }
        });
    }

    private void ButtonVoltar(){
        but_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                porteani.setVisibility(View.VISIBLE);
                grande.setVisibility(View.GONE);
                medio.setVisibility(View.GONE);
                pequeno.setVisibility(View.GONE);

                idade_animal.setVisibility(View.VISIBLE); //
                ani_filhote.setVisibility(View.GONE);
                ani_adulto.setVisibility(View.GONE);

                vacinado.setVisibility(View.VISIBLE);
                vac_sim.setVisibility(View.GONE);
                vac_nao.setVisibility(View.GONE);

                anistatus.setVisibility(View.VISIBLE);
                status_adot.setVisibility(View.GONE);
                status_perdido.setVisibility(View.GONE);

                but_edit.setVisibility(View.VISIBLE);
                but_voltar.setVisibility(View.GONE);
                but_aply.setVisibility(View.GONE);

                raca_ani.setEnabled(false);
                desc_ani.setEnabled(false);
            }
        });
    }

    private void ButtonAply(){

    }


}
