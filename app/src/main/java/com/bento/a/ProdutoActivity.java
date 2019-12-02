package com.bento.a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProdutoActivity extends AppCompatActivity {

    private ImageView bt_voltar, prod_image_view;
    private TextView text_name, text_valor, text_desc;
    private Button bt_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);

        ButtonBuy();
        ButtonVoltar();
        InpToVar();
    }

    private void InpToVar(){

        prod_image_view = findViewById(R.id.image_prod);

        text_name = findViewById(R.id.prod_view_name);
        text_desc = findViewById(R.id.prod_view_desc);
        text_valor = findViewById(R.id.prod_view_valor);
    }

    private void ButtonBuy(){
        bt_buy = findViewById(R.id.but_comprar);
        bt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProdutoActivity.this, PopUpComprar.class));
            }
        });

    }

    private void ButtonVoltar(){
        bt_voltar = findViewById(R.id.voltar_loja);
        bt_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProdutoActivity.this, LojaActivity.class));
            }
        });
    }

}
