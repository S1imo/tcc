package com.bento.a;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bento.a.Adapters.Main_AAdapter;
import com.bento.a.Classes.Animal;
import com.bento.a.Classes.Connections;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Main_AAdapter arr_Adapter;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFire;
    private DatabaseReference mRefAnimal, mRefConnections, mRef;
    private String user_id, an_uid;
    private ImageView but_profile, but_adot, but_perd, but_loja, but_chat, likee, deslikee;
    private FloatingActionButton buttonDes, buttonLike, buttonSuper;
    private List<Animal> rowItems;
    private SwipeFlingAdapterView flingContainer;
    private Animation likeanim, deslikeanim;
    private GoogleMap mMap;
    private SupportMapFragment fragment;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocation;
    private Circle circle;
    private float[] distance = new float[2];

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseDatabase.getInstance();
        user_id = mAuth.getUid();

        InpToVar(); //input para variaveis
        PermissionEnabling();//função permissões localização
        Buttons(); //função dos botoes
    }

    private void Buttons() {
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
    }

    private void InpToVar() {
        buttonDes = findViewById(R.id.deslike_btn);
        buttonLike = findViewById(R.id.like_btn);
        buttonSuper = findViewById(R.id.superlike_btn);
        but_profile = findViewById(R.id.profile_icon);
        but_adot = findViewById(R.id.adot_icon);
        but_perd = findViewById(R.id.perdido_icon);
        but_loja = findViewById(R.id.shop_icon);
        but_chat = findViewById(R.id.chat_icon);

        flingContainer = findViewById(R.id.frame);

        likeanim = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        deslikeanim = AnimationUtils.loadAnimation(this,R.anim.fade_in);

    }

    private void PermissionEnabling() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mFusedLocation = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                        fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPIdes);
                        fragment.getMapAsync(MainActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Permissão de localização");
                        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO - Se clicado em não, ver como fazer o bang n travar
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Permissão de localização");
                        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO - Se clicado em não, ver como fazer o bang n travar
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                });
    }

    private void SwipeCard(final String an_id) {
        rowItems = new ArrayList<>();
        mRefAnimal = mFire.getReference().child("Animais");
        mRefConnections = mFire.getReference().child("Connections");
        /*rowItems.remove(animal);
        arr_Adapter.notifyDataSetChanged();*/
        mRefAnimal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        final Animal animal = snapshot1.getValue(Animal.class);
                        if(animal.getAn_uid().equals(an_id) && !animal.getUs_uid().equals(user_id) && !animal.getAn_status().equals("Perdido")){
                            rowItems.add(animal);
                            arr_Adapter.notifyDataSetChanged();
                            mRefConnections.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChildren()){
                                        for(DataSnapshot snapshot2: dataSnapshot.getChildren()){
                                            if(snapshot2.getKey().equals(animal.getAn_uid())){
                                                for(DataSnapshot snapshot3: snapshot2.getChildren()){
                                                    Connections connections = snapshot3.getValue(Connections.class);
                                                    if(connections.getUs_uid().equals(user_id) && connections.getAn_uid().equals(animal.getAn_uid())|| connections.getAn_us_uid().equals(user_id) && connections.getAn_uid().equals(animal.getAn_uid())){
                                                        rowItems.remove(animal);
                                                        arr_Adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            mRefConnections.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    if(dataSnapshot.hasChildren()){
                                        for(DataSnapshot snapshot2: dataSnapshot.getChildren()){
                                            if(snapshot2.getKey().equals(animal.getAn_uid())){
                                                for(DataSnapshot snapshot3: snapshot2.getChildren()){
                                                    Connections connections = snapshot3.getValue(Connections.class);
                                                    if(connections.getUs_uid().equals(user_id) && connections.getAn_uid().equals(animal.getAn_uid()) || connections.getAn_us_uid().equals(user_id) && connections.getAn_uid().equals(animal.getAn_uid())){
                                                        rowItems.remove(animal);
                                                        arr_Adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    if(dataSnapshot.hasChildren()){
                                        for(DataSnapshot snapshot2: dataSnapshot.getChildren()){
                                            if(snapshot2.getKey().equals(animal.getAn_uid())){
                                                for(DataSnapshot snapshot3: snapshot2.getChildren()){
                                                    Connections connections = snapshot3.getValue(Connections.class);
                                                    if(connections.getUs_uid().equals(user_id) && connections.getAn_uid().equals(animal.getAn_uid()) || connections.getAn_us_uid().equals(user_id) && connections.getAn_uid().equals(animal.getAn_uid())){
                                                        rowItems.remove(animal);
                                                        arr_Adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChildren()){
                                        for(DataSnapshot snapshot2: dataSnapshot.getChildren()){
                                            if(snapshot2.getKey().equals(animal.getAn_uid())){
                                                for(DataSnapshot snapshot3: snapshot2.getChildren()){
                                                    Connections connections = snapshot3.getValue(Connections.class);
                                                    if(connections.getUs_uid().equals(user_id) && connections.getAn_uid().equals(animal.getAn_uid()) || connections.getAn_us_uid().equals(user_id) && connections.getAn_uid().equals(animal.getAn_uid())){
                                                        rowItems.remove(animal);
                                                        arr_Adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    if(dataSnapshot.hasChildren()){
                                        for(DataSnapshot snapshot2: dataSnapshot.getChildren()){
                                            if(snapshot2.getKey().equals(animal.getAn_uid())){
                                                for(DataSnapshot snapshot3: snapshot2.getChildren()){
                                                    Connections connections = snapshot3.getValue(Connections.class);
                                                    if(connections.getUs_uid().equals(user_id) && connections.getAn_uid().equals(animal.getAn_uid()) || connections.getAn_us_uid().equals(user_id) && connections.getAn_uid().equals(animal.getAn_uid())){
                                                        rowItems.remove(animal);
                                                        arr_Adapter.notifyDataSetChanged();
                                                    }
                                                }
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        arr_Adapter = new Main_AAdapter(this, R.layout.main_item, rowItems);
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
                deslikee = findViewById(R.id.deslike_anim);
                deslikee.setAnimation(deslikeanim);
                deslikeanim.start();
                if(dataObject != null){
                    an_uid = ((Animal) dataObject).getAn_uid();
                    DatabaseReference refNew = mFire.getReference();
                    int rand_num = (int) System.currentTimeMillis();
                    //o uid do usuario conectado
                    refNew.child("Connections").child(an_uid).child("No" + rand_num).child("us_uid").setValue(user_id);
                    //uid do animal
                    refNew.child("Connections").child(an_uid).child("No" + rand_num).child("an_uid").setValue(((Animal) dataObject).getAn_uid());
                    //uid da pessoa que colocou o cachorro para adoção
                    refNew.child("Connections").child(an_uid).child("No" + rand_num).child("an_us_uid").setValue(((Animal) dataObject).getUs_uid());
                }
            }

            @Override
            public void onRightCardExit(final Object dataObject) {
                likee = findViewById(R.id.like_anim);
                likee.setAnimation(likeanim);
                likeanim.start();
                if(dataObject != null){
                    an_uid = ((Animal) dataObject).getAn_uid();
                    DatabaseReference refNew = mFire.getReference();
                    int rand_num = (int) System.currentTimeMillis();
                    refNew.child("Connections").child(an_uid).child("Yes" + rand_num).child("us_uid").setValue(user_id);
                    refNew.child("Connections").child(an_uid).child("Yes" + rand_num).child("an_uid").setValue(((Animal) dataObject).getAn_uid());
                    refNew.child("Connections").child(an_uid).child("Yes" + rand_num).child("an_us_uid").setValue(((Animal) dataObject).getUs_uid());
                }

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

    private void ButtonPerfil() {
        but_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, PerfilActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
    }

    private void ButtonAdote() {
        but_adot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void ButtonPerdidos() {
        but_perd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PerdidosActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void ButtonLoja() {
        but_loja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, desespero.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonChat() {
        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    //inferior
    private void ButtonDes() {
        buttonDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animarFab(buttonDes);
                flingContainer.getTopCardListener().selectLeft();
            }
        });
    }

    private void ButtonSuperLike(){
        buttonSuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animarFab(buttonSuper);
                flingContainer.getTopCardListener().selectRight();
                likeanim.cancel();
            }
        });
    }

    private void ButtonLike() {

        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animarFab(buttonLike);
                flingContainer.getTopCardListener().selectRight();
            }
        });
    }

    private void animarFab(final FloatingActionButton fab) {
        fab.animate().scaleX(1.0f).scaleY(1.0f).setDuration(500).withEndAction(new Runnable() {
            @Override
            public void run() {

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                mLastLocation = locationResult.getLastLocation();

                circle = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                        .radius(700));
                mRef = mFire.getReference().child("Animais");
                if(!mRef.child(user_id).equals(user_id)){
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                for(DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()) {
                                    Animal animal = dataSnapshot2.getValue(Animal.class);
                                    assert animal != null;
                                    Location.distanceBetween(Double.parseDouble(animal.getAn_lat()), Double.parseDouble(animal.getAn_long()),circle.getCenter().latitude, circle.getCenter().longitude, distance);
                                    if(distance[0] < circle.getRadius()){
                                        SwipeCard(animal.getAn_uid());
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                mFusedLocation.removeLocationUpdates(mLocationCallback);
            }
        };
        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }
}