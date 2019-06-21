package com.bento.a;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;
    private SwipeFlingAdapterView flingContainer;
    private ImageButton buttonDes, buttonLike, buttonSuperL, buttonInfo;

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
        /*ButtonPerfil();
        ButtonAdote();
        ButtonPerdidos();
        ButtonLoja();
        ButtonChat();*/

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

        flingContainer = findViewById(R.id.frame);
    }

    /*private void ButtonPerfil()
    {

    }

    private void ButtonAdote()
    {

    }

    private void ButtonPerdidos()
    {

    }

    private void ButtonLoja()
    {

    }

    private void ButtonChat()
    {

    }

    */

    private void ButtonDes()
    {
        buttonDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                flingContainer.getTopCardListener().selectLeft();
            }
        });
    }

    private void ButtonLike()
    {
        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               flingContainer.getTopCardListener().selectRight();
            }
        });
    }

    private void ButtonSuperLike()
    {
        buttonSuperL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        ArrayListCards();

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

    private void ArrayListCards()
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

}


