package com.bento.a;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bento.a.Adapters.arrayAdapter;
import com.bento.a.Classes.Animal;
import com.bento.a.Classes.Connections;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class desespero extends AppCompatActivity {

    private arrayAdapter arr_Adapter;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef;
    private DatabaseReference mRefConnections;
    private String user_id, an_uid;
    private List<Animal> rowItems;
    private SwipeFlingAdapterView flingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desespero);

        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseDatabase.getInstance();
        mRef = mFire.getReference();
        user_id = mAuth.getUid();

        flingContainer = findViewById(R.id.swipe_des);

        rowItems = new ArrayList<>();
        DatabaseReference mRefAnimal = mFire.getReference().child("Animais");
        mRefConnections = mFire.getReference().child("Connections");
        mRefAnimal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("Teste");
                for (final DataSnapshot value : dataSnapshot.getChildren()) {
                    for (DataSnapshot value_in : value.getChildren()) {
                        final Animal animal = value_in.getValue(Animal.class);
                        assert animal != null;
                        //verificação - ver se o animal é do user que está
                        if (!animal.getUs_uid().equals(user_id)) {
                            rowItems.add(animal);
                            arr_Adapter.notifyDataSetChanged();
                            mRefConnections.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    for(DataSnapshot value_con: dataSnapshot.getChildren()) {
                                        if (Objects.equals(value_con.getKey(), animal.getAn_uid())) {
                                            for (DataSnapshot value_con1 : value_con.getChildren()) {
                                                Connections connections = value_con1.getValue(Connections.class);
                                                assert connections != null;
                                                if (connections.getUs_uid().equals(user_id)) {
                                                    rowItems.remove(animal);
                                                    arr_Adapter.notifyDataSetChanged();
                                                }
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });/*.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChildren())
                                    {
                                        for(DataSnapshot value_con: dataSnapshot.getChildren())
                                        {
                                            if(Objects.equals(value_con.getKey(), animal.getAn_uid()))
                                            {
                                                for(DataSnapshot value_con1: value_con.getChildren())
                                                {
                                                    Connections connections = value_con1.getValue(Connections.class);
                                                    assert connections != null;
                                                    if(!connections.getUs_uid().equals(user_id))
                                                    {
                                                        rowItems.add(animal);
                                                        arr_Adapter.notifyDataSetChanged();
                                                    }
                                                    else
                                                    {
                                                        rowItems.remove(animal);
                                                        arr_Adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        rowItems.add(animal);
                                        arr_Adapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });*/
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
                int num_chat = (int) System.currentTimeMillis();
                //o uid do usuario conectado
                refNew.child("Connections").child(an_uid).child("No" + num_chat).child("us_uid").setValue(user_id);
                //uid do animal
                refNew.child("Connections").child(an_uid).child("No" + num_chat).child("an_uid").setValue(((Animal) dataObject).getAn_uid());
                //uid da pessoa que colocou o cachorro para adoção
                refNew.child("Connections").child(an_uid).child("No" + num_chat).child("an_us_uid").setValue(((Animal) dataObject).getUs_uid());
            }

            @Override
            public void onRightCardExit(final Object dataObject) {
                an_uid = ((Animal) dataObject).getAn_uid();
                DatabaseReference refNew = mFire.getReference();
                int bbb = (int) System.currentTimeMillis();
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
