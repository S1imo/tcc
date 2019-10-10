package com.bento.a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PopUpPerfilFav extends AppCompatActivity {

    ImageView but_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_perfil_fav);

        InpToVar();
        ButtonChat();

    }

    private void InpToVar() {

        but_chat = findViewById(R.id.fav_chat);

    }

    private void ButtonChat() {

        final String other_us_uid = getIntent().getStringExtra("other_us_uid");

        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopUpPerfilFav.this, ChatConversaActivity.class)
                        .putExtra("other_us_uid", other_us_uid));
            }
        });
    }
}
