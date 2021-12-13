package com.example.trytouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.example.trytouse.R;

public class Telainicialuber1 extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telainicialuber1);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.uber);
        if (mediaPlayer != null){
            mediaPlayer.start();
        }
    }

    public void proximo(View view){
        startActivity(new Intent(getApplicationContext(), TelaInicialUber.class));
    }

    public void anterior(View view){
        startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
    }
}