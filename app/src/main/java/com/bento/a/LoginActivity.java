package com.bento.a;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        FirebaseDatabase database;
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user");

        myRef.setValue("Hello, World!");

    }
}
