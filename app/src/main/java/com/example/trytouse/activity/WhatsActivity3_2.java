package com.example.trytouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.example.trytouse.R;

public class WhatsActivity3_2 extends AppCompatActivity {
MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_activity3_2);

    }

    public void mudarTexto(View view){
        startActivity(new Intent(getApplicationContext(), WhatsActivity4.class));
        mediaPlayer.stop();
    }

    public void telanterior(View view){
        startActivity(new Intent(getApplicationContext(), WhatsActivity3.class));
        mediaPlayer.stop();
    }
}