package com.example.trytouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.trytouse.R;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.example.trytouse.model.Usuario;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WhatsActivity3 extends AppCompatActivity {

    EditText txt_tel;
    ImageView img_tel;

    MediaPlayer mediaPlayer;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private DatabaseReference dtref;
    private String user;
    private ImageView back2, home2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats3);

        back2 = findViewById(R.id.img_back);
        home2 = findViewById(R.id.img_home);

        auth = ConfiguracaoFirebas.getFirebaseAutenticacao();

        AlertDialog.Builder builder_whats_tel = new AlertDialog.Builder(this);

        builder_whats_tel.setView(getLayoutInflater().inflate(R.layout.dialog_whats_telefone, null));
        builder_whats_tel.setCancelable(true);

        AlertDialog dialog_whats_tel = builder_whats_tel.create();
        dialog_whats_tel.show();

        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle("Verifique seu número");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_tel = findViewById(R.id.txt_tel);
        img_tel = findViewById(R.id.img_tel);
        user = UsuarioFirebaseInfo.getIdUsuario();
        mDatabase = ConfiguracaoFirebas.getFirebaseDatabase().child("usuarios");
        dtref = mDatabase.child(user).child("telefone");

        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                startActivity(new Intent(getApplicationContext(), WhatsActivity2.class));
            }
        });

        home2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        dtref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    txt_tel.setText(String.valueOf(snapshot.getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", error.getMessage());
            }
        });

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.whats2);
        if (mediaPlayer != null){
            mediaPlayer.start();
        }

        img_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                startActivity(new Intent(getApplicationContext(), WhatsActivity4.class));

            }
        });

    }
}