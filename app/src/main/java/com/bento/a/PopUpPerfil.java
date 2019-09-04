package com.bento.a;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PopUpPerfil extends AppCompatActivity {
    private TextView exitText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_perfil);

        InpToVar();

        ButtonBack();

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

    private void InpToVar()
    {
        exitText = findViewById(R.id.exit_text);
    }

}
