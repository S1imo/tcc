package com.bento.a;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

public class desespero extends AppCompatActivity {

    FirebaseAuth mAuth;
    String user_id;
    int pinto = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desespero);

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getUid();
        Pint();
        System.out.println();

    }

    private void Pint() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Animais").child(user_id);
        myRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot value : dataSnapshot.getChildren()) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
