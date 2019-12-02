package com.bento.a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bento.a.Classes.Loja;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PopUpShopPerfil extends AppCompatActivity {

    private FirebaseDatabase mFire;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private String user_id, l_uid, l_img_1, l_img_2;
    private TextView exit, nome_prod, qtd_prod, valor_prod, desc_prod;
    private ImageView img1, img2;
    private EditText nome_edit, qtd_edit, valor_edit, desc_edit;
    private Button bt_edit, bt_exc, bt_cancel, bt_apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_shop_perfil_layout);

        InpToVar();
        DisplayInfo();
        Buttons();
    }

    private void InpToVar(){
        mFire = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mRef = mFire.getReference();
        user_id = mAuth.getUid();

        l_uid = getIntent().getStringExtra("l_id");

        exit = findViewById(R.id.exit_prod);
        bt_edit = findViewById(R.id.editar_prod);
        bt_exc = findViewById(R.id.prod_exc);
        bt_cancel = findViewById(R.id.but_cancel);
        bt_apply = findViewById(R.id.but_apli);

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

    private void Buttons(){
        ButtonEditar();
        ButtonExcluir();
        ButtonCancel();
        ButtonExit();
    }

    private void DisplayInfo(){
        mRef.child("Produtos").child(user_id).child(l_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Loja loja = dataSnapshot.getValue(Loja.class);
                assert loja != null;
                nome_prod.setText(loja.getL_nome());
                qtd_edit.setText(loja.getL_qtd());
                valor_edit.setText(loja.getL_val());
                desc_edit.setText(loja.getL_desc());
                Picasso.get().load(loja.getL_img_1()).into(img1);
                Picasso.get().load(loja.getL_img_1()).into(img2);

                l_img_1 = loja.getL_img_1();
                l_img_2 = loja.getL_img_2();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ButtonExcluir() {
        bt_exc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PopUpShopPerfil.this);
                builder.setTitle("Tem certeza de que quer excluir o produto");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRef.child("Produtos").child(user_id).child(l_uid).removeValue();
                        startActivity(new Intent(PopUpShopPerfil.this, PerfilActivity.class));
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
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
                bt_apply.setVisibility(View.VISIBLE);
                img1.setClickable(true);
                img2.setClickable(true);
            }
        });
    }

    //cadastrar

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
                bt_apply.setVisibility(View.GONE);
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
