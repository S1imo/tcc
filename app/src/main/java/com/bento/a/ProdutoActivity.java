package com.bento.a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bento.a.Classes.Loja;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProdutoActivity extends AppCompatActivity {

    private ImageView bt_voltar, prod_image_view;
    private TextView text_name, text_valor, text_desc;
    private String l_id, l_us_uid;
    private Button bt_buy;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produto_layout);

        InpToVar();
        DisplayInfo();
        ButtonBuy();
        ButtonVoltar();
    }

    private void InpToVar(){
        mFire = FirebaseDatabase.getInstance();
        mRef = mFire.getReference();

        l_id = getIntent().getStringExtra("l_id");
        l_us_uid = getIntent().getStringExtra("l_us_uid");

        prod_image_view = findViewById(R.id.image_prod);
        text_name = findViewById(R.id.prod_view_name);
        text_desc = findViewById(R.id.prod_view_desc);
        text_valor = findViewById(R.id.prod_view_valor);

        bt_buy = findViewById(R.id.but_comprar);
        bt_voltar = findViewById(R.id.voltar_loja);
    }

    private void DisplayInfo(){
        mRef.child("Produto").child(l_us_uid).child(l_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Loja loja = dataSnapshot.getValue(Loja.class);
                assert loja != null;
                Picasso.get().load(loja.getL_img_1()).into(prod_image_view);
                text_name.setText(loja.getL_nome());
                text_valor.setText(loja.getL_val());
                text_desc.setText(loja.getL_desc());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ButtonBuy(){
        bt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProdutoActivity.this, PopUpComprar.class));
            }
        });

    }

    private void ButtonVoltar(){
        bt_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProdutoActivity.this, LojaActivity.class));
            }
        });
    }

}
