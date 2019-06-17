package com.bento.a;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class PopUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custompopup);
    }
    public void clickexit (View v){
        finish();

    }

}
