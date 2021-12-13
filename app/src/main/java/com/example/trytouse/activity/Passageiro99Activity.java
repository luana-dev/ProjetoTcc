package com.example.trytouse.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.trytouse.R;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.fragment.NoveFragment;
import com.example.trytouse.fragment.UberFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Passageiro99Activity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private EditText editMeuLocal, editDestino;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private ImageView img_back, img_home;
    private Button chamar;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private LatLng local;
    FrameLayout frameLayout;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passageiro99);

        /*List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);*/

        AlertDialog.Builder builder_passnove = new AlertDialog.Builder(this);

        builder_passnove.setView(getLayoutInflater().inflate(R.layout.dialog_nove, null));
        builder_passnove.setCancelable(true);

        AlertDialog dialog_passnove = builder_passnove.create();
        dialog_passnove.show();

        editMeuLocal = findViewById(R.id.editMeuLocal);
        editDestino = findViewById(R.id.editLocalnove);
        img_back = findViewById(R.id.img_back);
        img_home = findViewById(R.id.img_home);
        chamar = findViewById(R.id.btn_chamarnove);
        frameLayout = findViewById(R.id.viewPagerpasnove);

        autenticacao = ConfiguracaoFirebas.getFirebaseAutenticacao();

        frameLayout.setVisibility(View.INVISIBLE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar99);
        toolbar.setTitle("99");
        setSupportActionBar(toolbar);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NovenoveActivity.class));
            }
        });

        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.viewPagerpasnove, new NoveFragment()).commit();

    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("Place", "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("Place", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        recuperarLocalizacaoUsuario();

        chamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desenhaLinha();
            }
        });


    }

    private void recuperarLocalizacaoUsuario() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng meuLocal = new LatLng(latitude, longitude);
                local = meuLocal;

                mMap.clear();
                mMap.addMarker(
                        new MarkerOptions()
                                .position(meuLocal)
                                .title("Meu Local")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                );

                Geocoder gCoder = new Geocoder(getApplicationContext());
                ArrayList<Address> addresses = null;
                try {
                    addresses = (ArrayList<Address>) gCoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    editMeuLocal.setText(addresses.get(0).getThoroughfare() + " " + addresses.get(0).getFeatureName());

                }

                mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(meuLocal, 15)
                );

            }


        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000,
                    10,
                    locationListener
            );

        }
    }

    private void desenhaLinha() {
        String endDestino = editDestino.getText().toString();
        frameLayout.setVisibility(View.VISIBLE);
        chamar.setVisibility(View.INVISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(Passageiro99Activity.this);
        builder.setView(getLayoutInflater().inflate(R.layout.dialog_pagamento, null));
        builder.setCancelable(true);
        builder.create().show();

        if (!endDestino.equals("")|| endDestino != null){
            Address address = recuperaEnd(endDestino);
            if (address != null){
                PolylineOptions polyline = new PolylineOptions();
                polyline.add(new LatLng(local.latitude, local.longitude));
                polyline.add(new LatLng(address.getLatitude(), address.getLongitude()));
                polyline.width(20);
                mMap.addPolyline(polyline);

                LatLng meuDestino = new LatLng(address.getLatitude(), address.getLongitude());

                mMap.addMarker(
                        new MarkerOptions()
                                .position(meuDestino)
                                .title("Meu Destino")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_uber_99))
                );

                mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(meuDestino, 15)
                );
            }
        }

       /* locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            return;
        }*/
        /*Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        PolylineOptions polyline = new PolylineOptions();
        polyline.add(new LatLng(latitude, longitude));
        polyline.add(new LatLng(-22.879460, -43.549857));
        polyline.add(new LatLng(-22.879510, -43.549880));
        polyline.add(new LatLng(-22.879577, -43.549922));
        polyline.add(new LatLng(-22.879639, -43.549956));
        polyline.add(new LatLng(-22.879710, -43.549996));*/


    }

    private Address recuperaEnd(String endDestino) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> listaEnds = geocoder.getFromLocationName(endDestino,1);
            if (listaEnds != null && listaEnds.size() > 0){
                Address address = listaEnds.get(0);

                return address;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_insta, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void deslogarUsuario(){
        try{
            autenticacao.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSairInsta:
                deslogarUsuario();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.menuSairTutorialInsta:
                startActivity(new Intent(getApplicationContext(), FimNove.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}