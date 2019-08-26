package com.bento.a;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class PopUpActivity extends AppCompatActivity {

    private TextView exitText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custompopup);

        InpToVar();

        ButtonBack();

    }

    private void ButtonBack()
    {
        exitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void InpToVar()
    {
        exitText = findViewById(R.id.txtclose);
    }


}
