package com.example.trytouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.trytouse.R;

public class TelaPermiUber extends AppCompatActivity {
    ImageView img_uber;
    Button btn_prox_uber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_permi_uber);

        img_uber = findViewById(R.id.img_uber);
        btn_prox_uber = findViewById(R.id.btn_prox_uber);

        btn_prox_uber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        img_uber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}