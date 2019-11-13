package com.bento.a;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bento.a.Classes.Animal;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

//https://www.youtube.com/watch?v=ifoVBdtXsv0

public class desespero extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Circle circle;
    private SupportMapFragment fragment;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocation;
    private float[] distance = new float[2];
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFire;
    private DatabaseReference mRef;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desespero);

        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseDatabase.getInstance();
        user_id = mAuth.getUid();

        PermissionEnabling();
    }

    private void PermissionEnabling() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mFusedLocation = LocationServices.getFusedLocationProviderClient(desespero.this);
                        fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPIdes);
                        fragment.getMapAsync(desespero.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(desespero.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(desespero.this);
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
                        .radius(700)
                        .strokeWidth(10)
                        .strokeColor(Color.RED)
                        .fillColor(Color.argb(128, 255, 0, 0)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 13));

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
                                        mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(Double.parseDouble(animal.getAn_lat()), Double.parseDouble(animal.getAn_long())))
                                                .title(animal.getAn_raca()));
                                        Toast.makeText(getBaseContext(), ""+distance[0], Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getBaseContext(), ""+distance[0], Toast.LENGTH_LONG).show();
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

        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                int strokeColor = circle.getStrokeColor() ^ 0x00ffffff;
                circle.setStrokeColor(strokeColor);
            }
        });
    }
}
