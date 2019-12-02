package com.bento.a;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PopUpShopPerfil extends AppCompatActivity {

    private TextView exit, nome_prod, qtd_prod, valor_prod, desc_prod;
    private ImageView img1, img2;
    private EditText nome_edit, qtd_edit, valor_edit, desc_edit;
    private Button bt_edit, bt_exc, bt_cancel, bt_aplly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_shop_perfil);

        InpToVar();
        Buttons();
    }

    private void Buttons(){
        ButtonEditar();
        ButtonCancel();
        ButtonExit();
    }

    private void InpToVar(){
        exit = findViewById(R.id.exit_prod);
        bt_edit = findViewById(R.id.editar_prod);
        bt_exc = findViewById(R.id.prod_exc);
        bt_cancel = findViewById(R.id.but_cancel);
        bt_aplly = findViewById(R.id.but_apli);

        nome_prod = findViewById(R.id.nome_prod);
        qtd_prod = findViewById(R.id.qtd_prod);
        valor_prod = findViewById(R.id.valor_prod);
        desc_prod = findViewById(R.id.desc_prod);

        img1 = findViewById(R.id.image_prod1);
        img2 = findViewById(R.id.image_prod2);

        nome_edit = findViewById(R.id.edit_prod_nome);
        qtd_edit = findViewById(R.id.edit_prod_qtd);
        valor_edit = findViewById(R.id.edit_prod_valor);
        desc_edit = findViewById(R.id.edit_prod_desc);

    }

    private void ButtonEditar(){
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INVISIVEL
                nome_prod.setVisibility(View.GONE);
                qtd_prod.setVisibility(View.GONE);
                valor_prod.setVisibility(View.GONE);
                desc_prod.setVisibility(View.GONE);
                bt_edit.setVisibility(View.GONE);
                bt_exc.setVisibility(View.GONE);

                //VISIVEL
                nome_edit.setVisibility(View.VISIBLE);
                qtd_edit.setVisibility(View.VISIBLE);
                valor_edit.setVisibility(View.VISIBLE);
                desc_edit.setVisibility(View.VISIBLE);
                bt_cancel.setVisibility(View.VISIBLE);
                bt_aplly.setVisibility(View.VISIBLE);
                img1.setClickable(true);
                img2.setClickable(true);
            }
        });
    }

    private void ButtonCancel(){
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VISICEL
                nome_prod.setVisibility(View.VISIBLE);
                qtd_prod.setVisibility(View.VISIBLE);
                valor_prod.setVisibility(View.VISIBLE);
                desc_prod.setVisibility(View.VISIBLE);
                bt_edit.setVisibility(View.VISIBLE);
                bt_exc.setVisibility(View.VISIBLE);

                //INVISIVEL
                nome_edit.setVisibility(View.GONE);
                qtd_edit.setVisibility(View.GONE);
                valor_edit.setVisibility(View.GONE);
                desc_edit.setVisibility(View.GONE);
                bt_cancel.setVisibility(View.GONE);
                bt_aplly.setVisibility(View.GONE);
                img1.setClickable(false);
                img2.setClickable(false);
            }
        });
    }

    private void ButtonExit(){
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
