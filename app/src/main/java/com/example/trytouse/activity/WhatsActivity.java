package com.example.trytouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trytouse.R;

public class WhatsActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer, mediaPlayer2;
    TextView textTitulo;
    ImageView imagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.whats);
        mediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.escolhatutorial2);

        if (mediaPlayer != null){
            mediaPlayer.start();
        }
        if (!mediaPlayer.isPlaying()){
            mediaPlayer2.start();
        }

        textTitulo = findViewById(R.id.textboasvindas);
        //imagens = findViewById(R.id.imagens);

        //setData();
    }

    public void setData(){
        textTitulo.setText("Bem-vindo ao tutorial de como usar o WhatsApp!");
    }

    public void proximo(View view){
        startActivity(new Intent(getApplicationContext(), WhatsActivity2.class));
        mediaPlayer.stop();
    }

    public void anterior(View view){
        startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
        mediaPlayer.stop();

    }

}