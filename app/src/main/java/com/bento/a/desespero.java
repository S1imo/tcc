package com.bento.a;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bento.a.Adapters.arrayAdapter;
import com.bento.a.Classes.Animal;
import com.bento.a.Classes.Connections;
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

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef, mRefAnimal;
    private String user_id, an_uid;
    private List<Animal> rowItems;
    private SwipeFlingAdapterView flingContainer;
    private arrayAdapter arr_Adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desespero);

        mFire = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        flingContainer = findViewById(R.id.frame_layout_desespero);

        user_id = mAuth.getUid();

        SwipeCard();

    }

    private void SwipeCard() {
        rowItems = new ArrayList<>();
        mRefAnimal = mFire.getReference().child("Animais");

        mRefAnimal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for (final DataSnapshot value : dataSnapshot.getChildren()) {
                    for (DataSnapshot value_in : value.getChildren()) {
                        final Animal animal = value_in.getValue(Animal.class);
                        assert animal != null;
                        if (!animal.getUs_uid().equals(user_id)) {
                            System.out.println(animal.getAn_uid());
                            mRef = mFire.getReference().child("Connections").child(animal.getAn_uid());
                            mRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChildren()) {
                                        for (DataSnapshot value_con : dataSnapshot.getChildren()) {
                                            Connections connections = value_con.getValue(Connections.class);
                                            assert connections != null;
                                            if (!connections.getUs_uid().equals(user_id)) {
                                                rowItems.add(animal);
                                                arr_Adapter.notifyDataSetChanged();
                                            }
                                        }
                                    } else {
                                        rowItems.add(animal);
                                        arr_Adapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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
                an_uid = ((Animal) dataObject).getAn_uid();
                DatabaseReference refNew = mFire.getReference();
                int aaa = (int) System.currentTimeMillis();
                refNew.child("Connections").child(an_uid).child("No" + aaa).child("us_uid").setValue(user_id);
                refNew.child("Connections").child(an_uid).child("No" + aaa).child("an_uid").setValue(((Animal) dataObject).getAn_uid());
                refNew.child("Connections").child(an_uid).child("No" + aaa).child("an_us_uid").setValue(((Animal) dataObject).getUs_uid());
            }

            @Override
            public void onRightCardExit(final Object dataObject) {
                an_uid = ((Animal) dataObject).getAn_uid();
                int bbb = (int) System.currentTimeMillis();
                DatabaseReference refNew = mFire.getReference();
                refNew.child("Connections").child(an_uid).child("Yes" + bbb).child("us_uid").setValue(user_id);
                refNew.child("Connections").child(an_uid).child("Yes" + bbb).child("an_uid").setValue(((Animal) dataObject).getAn_uid());
                refNew.child("Connections").child(an_uid).child("Yes" + bbb).child("an_us_uid").setValue(((Animal) dataObject).getUs_uid());
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                Log.d("LIST", "Empty");
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });
    }
}
