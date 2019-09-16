package com.bento.a;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bento.a.Classes.Animal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class desespero extends AppCompatActivity {

    FirebaseAuth mAuth;
    String user_id;
    DatabaseReference mRef;
    FirebaseDatabase mFire;

    private List<Animal> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desespero);

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
                            Animal animal = value_in.getValue(Animal.class);
                            System.out.println(value_in.child("connections").getKey());
                            rowItems.add(animal);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
