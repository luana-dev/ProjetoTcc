package com.example.trytouse.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trytouse.R;

public class WhatsActivity2 extends AppCompatActivity {
    MediaPlayer mediaPlayer2;
    private ImageView back, home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats2);

        back = findViewById(R.id.img_back);
        home = findViewById(R.id.img_home);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer2.stop();
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer2.stop();
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        AlertDialog.Builder builder_whats = new AlertDialog.Builder(this);

        builder_whats.setView(getLayoutInflater().inflate(R.layout.dialog_tela_inicial_whats, null));
        builder_whats.setCancelable(true);

        AlertDialog dialog_whats = builder_whats.create();
        dialog_whats.show();

        mediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.whats1);
        if (mediaPlayer2 != null){
            mediaPlayer2.start();
        }

    }

    public void mudarTexto(View view){
        startActivity(new Intent(getApplicationContext(), WhatsActivity3.class));
        mediaPlayer2.stop();
        //mediaPlayer2.stop();
    }
}