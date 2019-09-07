package com.bento.a;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PopUpPerfil extends AppCompatActivity {
    private TextView exitText;
    private Button but_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_perfil);

        InpToVar();

        ButtonBack();
        ButtonEdit();

    }

    private void InpToVar()
    {
        exitText = findViewById(R.id.exit_text);
        but_edit = findViewById(R.id.but_editar);
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
                startActivity(new Intent(PopUpPerfil.this, CadAnimal.class));
            }
        });
    }


}
