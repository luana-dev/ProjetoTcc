package com.example.trytouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.trytouse.R;

public class TelaUber extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_uber);
    }

    public void proximatela(View view){
        startActivity(new Intent(getApplicationContext(), TelaPermiUber.class));
    }
}