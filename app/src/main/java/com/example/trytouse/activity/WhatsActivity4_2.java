package com.example.trytouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.example.trytouse.R;

public class WhatsActivity4_2 extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_activity4_2);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.whats4_2);
        if (mediaPlayer != null){
            mediaPlayer.start();
        }
    }

    public void proximo(View view){
        mediaPlayer.stop();

    }

    public void anterior(View view){
        startActivity(new Intent(getApplicationContext(), WhatsActivity4.class));
        mediaPlayer.stop();
    }
}