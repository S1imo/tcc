package com.bento.a;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bento.a.Adapters.arrayAdapter;
import com.bento.a.animals.Animal;
import com.bento.a.users.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private arrayAdapter arr_Adapter;
    private FirebaseAuth mAuth;
    private ImageButton buttonInfo;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef;
    private String user_id;
    private ImageView but_profile, but_adot, but_perd, but_loja, but_chat, imagelike;
    private FloatingActionButton buttonDes, buttonLike;
    private List<Animal> rowItems;
    private SwipeFlingAdapterView flingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseDatabase.getInstance();
        user_id = mAuth.getUid();

        InpToVar(); //input para variaveis
        SwipeCard(); //função do card
        Buttons(); //função dos botoes


    }

    private void Buttons() {
        //botões superiores - menu
        ButtonPerfil();
        ButtonAdote();
        ButtonPerdidos();
        ButtonLoja();
        ButtonChat();

        //botões inferiores
        ButtonDes();
        ButtonLike();
        ButtonInfo();
    }

    private void InpToVar() {
        buttonDes = findViewById(R.id.deslike_btn);
        buttonLike = findViewById(R.id.like_btn);
        buttonInfo = findViewById(R.id.info_btn);

        but_profile = findViewById(R.id.profile_icon);
        but_adot = findViewById(R.id.adot_icon);
        but_perd = findViewById(R.id.perdido_icon);
        but_loja = findViewById(R.id.shop_icon);
        but_chat = findViewById(R.id.chat_icon);

        flingContainer = findViewById(R.id.frame);
        imagelike = findViewById(R.id.imagelike);

    }


    private void SwipeCard() {
        rowItems = new ArrayList<>();
        mRef = mFire.getReference().child("Animais");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot value: dataSnapshot.getChildren())
                {
                    String pint = value.getKey();
                    assert pint != null;
                    if(!pint.equals(user_id))
                    {
                        for(DataSnapshot value_in: value.getChildren())
                        {
                            if(!value_in.child("connections").child("No").hasChild(user_id) && !value_in.child(user_id).child("connections").child("Yes").hasChild(user_id))
                            {
                                Animal animal = value_in.getValue(Animal.class);
                                rowItems.add(animal);
                                arr_Adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        arr_Adapter = new arrayAdapter(this, R.layout.main_item, rowItems);
        flingContainer.setAdapter(arr_Adapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arr_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(final Object dataObject) {
                final String other_uid = ((Animal) dataObject).getUs_uid();
                mRef = mFire.getReference();
                final DatabaseReference mRefA = mFire.getReference().child("Animais").child(other_uid);
                mRef.child("Users").child(other_uid).child("connections").child("No").child(user_id).setValue(true);
                mRefA.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot value: dataSnapshot.getChildren())
                        {
                            if(value.child("an_fprof_img").getValue().equals(((Animal) dataObject).getAn_fprof_img()))
                            {
                                mRefA.child("connections").child("No").child(user_id).setValue(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onRightCardExit(final Object dataObject) {
                String other_uid = ((Animal) dataObject).getUs_uid();
                mRef = mFire.getReference();
                mRef.child("Users").child(other_uid).child("connections").child("Yes").child(user_id).setValue(true);
                //TODO - colocar numero diferente para cada localchat - pegar do animal
                mRef.child("Chat_Connections").child("LocalChat").child(user_id).setValue(true);
                mRef.child("Chat_Connections").child("LocalChat").child(other_uid).setValue(true);
                mRef.child("Chat_Connections").child("LocalChat").child(((Animal) dataObject).getAn_uid()).setValue(true);
                //user_uid, other_uid e uid_cachorro

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                //arr_Adapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });
    }

    private void ButtonPerfil() {
        but_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, PerfilActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
    }

    private void ButtonAdote() {
        but_adot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void ButtonPerdidos() {
        but_perd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PerdidosActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void ButtonLoja() {
        but_loja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LojaActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonChat() {
        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, ChatActivity.class));
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    //inferior
    private void ButtonDes() {
        buttonDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animarFab(buttonDes);
                flingContainer.getTopCardListener().selectLeft();
                Toast.makeText(MainActivity.this, "Deslike", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ButtonLike() {

        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animarFab(buttonLike);
                flingContainer.getTopCardListener().selectRight();
                CharSequence contentDescription = flingContainer.getSelectedView().getContentDescription();
                String a = contentDescription.toString();
                Toast.makeText(MainActivity.this, a, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void ButtonInfo() {
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PopUpActivity.class));
            }
        });
    }

    private void animarFab(final FloatingActionButton fab) {
        fab.animate().scaleX(1.0f).scaleY(1.0f).setDuration(500).withEndAction(new Runnable() {
            @Override
            public void run() {

            }
        });
    }


}