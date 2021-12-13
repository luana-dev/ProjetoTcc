package com.example.trytouse.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.trytouse.FeedFragment;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.fragment.UberFragment;
import com.example.trytouse.model.Destino;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

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
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PassageiroActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private EditText editDestino;
    private Button btn_chamar;
    MediaPlayer mediaPlayer, mediaPlayer2;
    private ImageView img_back_uber2, img_home_uber2;
    private LatLng local;
    FrameLayout viewPager;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passageiro);

        img_back_uber2 = findViewById(R.id.img_back);
        img_home_uber2 = findViewById(R.id.img_home);
        viewPager = findViewById(R.id.viewPagerpas);
        btn_chamar = findViewById(R.id.btn_chamar);

        viewPager.setVisibility(View.INVISIBLE);

        img_back_uber2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying() | mediaPlayer2.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer2.stop();
                }
                startActivity(new Intent(getApplicationContext(), LoginUber.class));
            }
        });

        img_home_uber2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying() | mediaPlayer2.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer2.stop();
                }
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        AlertDialog.Builder builder_pass = new AlertDialog.Builder(this);
        AlertDialog.Builder builder_pass2 = new AlertDialog.Builder(this);

        builder_pass.setView(getLayoutInflater().inflate(R.layout.dialog_pass, null));
        builder_pass2.setView(getLayoutInflater().inflate(R.layout.dialog_pass2, null));

        builder_pass.setCancelable(true);
        builder_pass2.setCancelable(true);

        builder_pass.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
              builder_pass2.create().show();
            }
        });

        AlertDialog dialog_pass = builder_pass.create();
        dialog_pass.show();

        Toolbar toolbar = findViewById(R.id.toolbar_uber);
        toolbar.setTitle("Uber");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        autenticacao = ConfiguracaoFirebas.getFirebaseAutenticacao();

        inicializarComponentes();

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.uber4);
        mediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.uber5);
        if (mediaPlayer != null){
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mediaPlayer2 != null){
                        mediaPlayer2.start();
                    }
                }
            });
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.viewPagerpas, new UberFragment()).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        recuperarLocalizacaoUsuario();


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
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.usuario))
                );

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

    private void inicializarComponentes(){
        editDestino = findViewById(R.id.editDestino);
        btn_chamar = findViewById(R.id.btn_chamar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void chamar_uber(View v){
        String endDestino = editDestino.getText().toString();

        if (!endDestino.equals("")|| endDestino != null){
            viewPager.setVisibility(View.VISIBLE);
            btn_chamar.setVisibility(View.INVISIBLE);

            AlertDialog.Builder builder = new AlertDialog.Builder(PassageiroActivity.this);
            builder.setView(getLayoutInflater().inflate(R.layout.dialog_pagamento, null));
            builder.setCancelable(true);
            builder.create().show();

            Address address = recuperaEnd(endDestino);

            if (address!= null){
                Destino destino = new Destino();
                destino.setCidade(address.getAdminArea());
                destino.setCep(address.getPostalCode());
                destino.setBairro(address.getSubLocality());
                destino.setRua(address.getThoroughfare());
                destino.setNumero(address.getFeatureName());
                destino.setLatitude(String.valueOf(address.getLatitude()));
                destino.setLongitude(String.valueOf(address.getLongitude()));

               /* StringBuilder msg = new StringBuilder();
                msg.append("Cidade"+ destino.getCidade());
                msg.append("Rua"+ destino.getRua());
                msg.append("Bairro"+ destino.getBairro());
                msg.append("Número"+ destino.getNumero());
                msg.append("Cep"+ destino.getCep());*/

                PolylineOptions polyline = new PolylineOptions();
                polyline.add(new LatLng(local.latitude, local.longitude));
                polyline.add(new LatLng(address.getLatitude(), address.getLongitude()));
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

                /*AlertDialog.Builder builder = new AlertDialog.Builder(PassageiroActivity.this)
                        .setTitle("Confirme seu endereco!")
                        .setMessage(msg)
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();*/
            }
        } else {
            Toast.makeText(PassageiroActivity.this, "Informe o endereço de destino!", Toast.LENGTH_SHORT).show();
        }
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
                startActivity(new Intent(getApplicationContext(), FimUber.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}