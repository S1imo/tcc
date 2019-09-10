package com.bento.a;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bento.a.Adapters.arrayAdapter;
import com.bento.a.animals.Animal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class desespero extends AppCompatActivity {

    private List<Animal> rowItems;
    private ArrayList<Animal> arrayList;
    private arrayAdapter arr_Adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desespero);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFire = FirebaseDatabase.getInstance();

        SwipeFlingAdapterView flingContainer = findViewById(R.id.frame_layout_des);

        rowItems = new ArrayList<>();

        final String user_id = mAuth.getUid();
        assert user_id != null;
        DatabaseReference mRef = mFire.getReference().child("Animais");
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
                                rowItems.add(animal);
                                arr_Adapter.notifyDataSetChanged();
                                System.out.println(pint);
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
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(desespero.this, "Deslike", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(desespero.this, "Like", Toast.LENGTH_SHORT).show();

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
}
