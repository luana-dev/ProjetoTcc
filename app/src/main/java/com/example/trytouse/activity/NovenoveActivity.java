package com.example.trytouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.trytouse.R;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class NovenoveActivity extends AppCompatActivity {
    FloatingActionButton btn_avanca;
    ImageView img_back, img_next;
    MediaPlayer mediaPlayer;
    EditText tel_nove;
    DatabaseReference dt, dtref;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novenove);

        btn_avanca = findViewById(R.id.fa_btn_avanca);
        img_back = findViewById(R.id.img_back);
        img_next = findViewById(R.id.img_home);
        tel_nove = findViewById(R.id.editTextPhoneUber);
        user = UsuarioFirebaseInfo.getIdUsuario();
        dt = ConfiguracaoFirebas.getFirebaseDatabase().child("usuarios");
        dtref = dt.child(user).child("telefone");

        AlertDialog.Builder builder = new AlertDialog.Builder(NovenoveActivity.this);
        builder.setView(getLayoutInflater().inflate(R.layout.dialog_novenove, null));
        builder.setCancelable(true);

        builder.create().show();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        btn_avanca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Passageiro99Activity.class));
            }
        });

        dtref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    tel_nove.setText(String.valueOf(snapshot.getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", error.getMessage());
            }
        });

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.uber3);
        if (mediaPlayer != null){
            mediaPlayer.start();
        }
    }

}