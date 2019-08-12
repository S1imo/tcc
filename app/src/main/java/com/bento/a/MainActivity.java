package com.bento.a;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Placeholder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;
    private Animation frombottom;
    private SwipeFlingAdapterView flingContainer;
    private ImageButton buttonInfo;
    private ImageView but_profile, but_adot, but_perd, but_loja, but_chat, imagelike;
    private FloatingActionButton buttonDes, buttonSuperL, buttonLike;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);


        InpToVar(); //input para variaveis
        SetArrays(); //seta os arrays
        Buttons(); //função dos botoes
        SwipeCard(); //função do card

    }


    private void Buttons()
    {
        //botões superiores - menu
        ButtonPerfil();
        ButtonAdote();
        ButtonPerdidos();
        ButtonLoja();
        ButtonChat();

        //botões inferiores
        ButtonDes();
        ButtonLike();
        ButtonSuperLike();
        ButtonInfo();
    }

    private void InpToVar()
    {
        buttonSuperL = findViewById(R.id.superlike_btn);
        buttonDes = findViewById(R.id.deslike_btn);
        buttonLike = findViewById(R.id.like_btn);
        buttonInfo = findViewById(R.id.info_btn);

        but_profile = findViewById(R.id.profile_icon);
        but_adot = findViewById(R.id.adot_icon);
        but_perd = findViewById(R.id.perdido_icon);
        but_loja = findViewById(R.id.shop_icon);
        but_chat = findViewById(R.id.chat_icon);

        flingContainer = findViewById(R.id.frame);

    }

    private void ButtonPerfil()
    {
        but_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, PerfilActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
    }

    private void ButtonAdote()
    {
        but_adot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void ButtonPerdidos()
    {
        but_perd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(PerfilActivity.this, PerdidosActivity.class));
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonLoja()
    {
        but_loja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(PerfilActivity.this, LojaActivity.class));
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonChat()
    {
        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(PerfilActivity.this, ChatActivity.class));
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    //inferior
    private void ButtonDes()
    {
        buttonDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animarFab(buttonDes);
                flingContainer.getTopCardListener().selectLeft();
                Toast.makeText(MainActivity.this, "Like",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ButtonLike()
    {

        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animarFab(buttonLike);
                flingContainer.getTopCardListener().selectRight();
                Toast.makeText(MainActivity.this, "Like",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ButtonSuperLike()
    {

        buttonSuperL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagelike.startAnimation(frombottom);
                animarFab(buttonSuperL);
                flingContainer.getTopCardListener().selectRight();
                Toast.makeText(MainActivity.this, "Superlike", Toast.LENGTH_SHORT).show();

            }
        });
        imagelike = (ImageView) findViewById(R.id.imagelike);
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
    }


    private void ButtonInfo()
    {
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, PopUpActivity.class));
            }
        });
    }

    private void SetArrays()
    {
        al = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.name_idade, al );
    }

    private void SwipeCard()
    {
        ArrayTextListCards();

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(MainActivity.this, "Deslike",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(MainActivity.this, "Like",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                al.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });
    }

    private void ArrayTextListCards()
    {
        al.add("Cachorro, 8 meses");
        al.add("c");
        al.add("python");
        al.add("java");
        al.add("html");
        al.add("c++");
        al.add("css");
        al.add("javascript");
    }

    private void animarFab(final FloatingActionButton fab)
    {
        fab.animate().scaleX(0.7f).scaleY(0.7f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                fab.animate().scaleX(1f).scaleY(1f);
            }
        });
    }
}


