package com.bento.a;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bento.a.Adapters.ViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class PerdidosActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFire;
    private String user_id;
    private TextView testpop;
    private Dialog mDialog;
    private ImageView but_profile, but_adot, but_loja, but_chat, seta_voltar;
    private ImageButton chat_perdidos;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perdidos_layout);

        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseDatabase.getInstance();
        user_id = mAuth.getUid();

        InpToVar(); //input para variaveis
        Buttons(); //função dos botoes
        PerdidosAdapterTodos();
    }
    
    private void PerdidosAdapterRegion(){
        
    }

    private void PerdidosAdapterTodos() {

    }

    private void Buttons(){
        //MENU
        ButtonPerfil();
        ButtonAdote();
        ButtonLoja();
        ButtonChat();
        PopUp();
    }

    private void InpToVar(){

        but_profile = findViewById(R.id.profile_icon);
        but_adot = findViewById(R.id.adot_icon);
        but_loja = findViewById(R.id.shop_icon);
        but_chat = findViewById(R.id.chat_icon);


    }

    private void PopUp(){
        testpop = findViewById(R.id.textView5);
        mDialog = new Dialog(this);

        testpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog(){

        mDialog.setContentView(R.layout.popup_perdidos);

        //imagens em carrossel
        viewPager = mDialog.findViewById(R.id.myViewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);

        //botao chat para contato
        chat_perdidos = findViewById(R.id.chat_perdidos);
        final String other_us_uid = getIntent().getStringExtra("other_us_uid");

        chat_perdidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerdidosActivity.this, ChatConversaActivity.class)
                        .putExtra("other_us_uid", other_us_uid));
            }
        });


        //voltar
        seta_voltar = mDialog.findViewById(R.id.voltar_perdidos);
        seta_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }




    private void ButtonPerfil()
    {
        but_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PerdidosActivity.this, PerfilActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
    }

    private void ButtonAdote()
    {
        but_adot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerdidosActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonLoja()
    {
        but_loja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerdidosActivity.this, LojaActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonChat()
    {
        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerdidosActivity.this, ChatActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}
