package com.bento.a;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bento.a.Adapters.Perdidos_AAdapter;
import com.bento.a.Classes.Animal;
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
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;
import java.util.List;

public class PerdidosActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef;
    private String user_id;
    private RecyclerView rv_Todos, rv_Region;
    private ImageView but_profile, but_adot, but_loja, but_chat;

    private GoogleMap mMap;
    private SupportMapFragment fragment;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocation;
    private Circle circle;
    private float[] distance = new float[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perdidos_layout);

        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseDatabase.getInstance();
        mRef = mFire.getReference();
        user_id = mAuth.getUid();

        InpToVar();
        PermissionEnabling();
        Buttons();
    }

    private void Buttons(){
        ButtonPerfil();
        ButtonAdote();
        ButtonLoja();
        ButtonChat();
    }

    private void InpToVar(){
        but_profile = findViewById(R.id.profile_icon);
        but_adot = findViewById(R.id.adot_icon);
        but_loja = findViewById(R.id.shop_icon);
        but_chat = findViewById(R.id.chat_icon);

    }

    private void PermissionEnabling() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mFusedLocation = LocationServices.getFusedLocationProviderClient(PerdidosActivity.this);
                        fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPIperd);
                        assert fragment != null;
                        fragment.getMapAsync(PerdidosActivity.this);
                        PerdidosRecyclerTodos();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PerdidosActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(PerdidosActivity.this);
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

    private void ButtonPerfil()
    {
        but_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PerdidosActivity.this, PerfilActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
    }

    private void ButtonAdote()
    {
        but_adot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerdidosActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonLoja()
    {
        but_loja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerdidosActivity.this, LojaActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void ButtonChat()
    {
        but_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerdidosActivity.this, ChatActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                if (!mRef.child(user_id).equals(user_id)) {
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                    Animal animal = dataSnapshot2.getValue(Animal.class);
                                    assert animal != null;
                                    Location.distanceBetween(Double.parseDouble(animal.getAn_lat()), Double.parseDouble(animal.getAn_long()), circle.getCenter().latitude, circle.getCenter().longitude, distance);
                                    if (distance[0] < circle.getRadius()) {
                                        PerdidosRecyclerRegion(animal.getAn_uid());
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

    private void PerdidosRecyclerRegion(final String an_id){
        final List<Animal> animalList1 = new ArrayList<>();
        final Perdidos_AAdapter perdidos_aAdapter1 = new Perdidos_AAdapter(PerdidosActivity.this, animalList1);

        rv_Region = findViewById(R.id.rvPerdidos);
        rv_Region.hasFixedSize();

        mRef.child("Animais");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Animal animal = snapshot1.getValue(Animal.class);
                        assert animal != null;
                        if (animal.getAn_status().equals("Perdido") && animal.getAn_uid().equals(an_id) && !animal.getUs_uid().equals(user_id)) {
                            animalList1.add(animal);
                            perdidos_aAdapter1.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        rv_Region.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        rv_Region.setAdapter(perdidos_aAdapter1);
    }

    private void PerdidosRecyclerTodos(){
        final List<Animal> animalList2 = new ArrayList<>();
        final Perdidos_AAdapter perdidos_aAdapter2 = new Perdidos_AAdapter(PerdidosActivity.this, animalList2);

        rv_Todos = findViewById(R.id.rvPerdidos2);
        rv_Todos.hasFixedSize();

        mRef.child("Animais").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        Animal animal = snapshot1.getValue(Animal.class);
                        assert animal != null;
                        if(animal.getAn_status().equals("Perdido") && !animal.getUs_uid().equals(user_id)){
                            animalList2.add(animal);
                            perdidos_aAdapter2.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        rv_Todos.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        rv_Todos.setAdapter(perdidos_aAdapter2);
    }
}
