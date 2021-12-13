package com.example.trytouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trytouse.R;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.fragment.ContatosFragment;
import com.example.trytouse.fragment.ConversasFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class WhatsActivity4 extends AppCompatActivity {

    //TextView textmuda4;
    //MediaPlayer mediaPlayer;
    private FirebaseAuth auth;
    private ImageView back3, home3;
    MediaPlayer mediaPlayer, mediaPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats4);

        back3 = findViewById(R.id.img_back);
        home3 = findViewById(R.id.img_home);

        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WhatsActivity3.class));
            }
        });

        home3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        /*AlertDialog.Builder builder = new AlertDialog.Builder(WhatsActivity4.this);

        builder.setView(getLayoutInflater().inflate(R.layout.dialog_conversas, null));
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();*/

        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle("WhatsApp");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        auth = ConfiguracaoFirebas.getFirebaseAutenticacao();

        //Configurar abas
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                .add("Conversas", ConversasFragment.class)
                        .add("Contatos", ContatosFragment.class)
                .create()
        );
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
       viewPagerTab.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
           @Override
           public void onTabClicked(int position) {
               if (position==0){
                   AlertDialog.Builder builder_whats_conv = new AlertDialog.Builder(WhatsActivity4.this);

                   builder_whats_conv.setView(getLayoutInflater().inflate(R.layout.dialog_conversas, null));
                   builder_whats_conv.setCancelable(true);

                   AlertDialog dialog_whats_conv = builder_whats_conv.create();
                   dialog_whats_conv.show();

                   mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.whats4);
                   if (mediaPlayer != null){
                       mediaPlayer.start();
                   }
               }
               if (position==1){
                   AlertDialog.Builder builder_whats_cont = new AlertDialog.Builder(WhatsActivity4.this);

                   builder_whats_cont.setView(getLayoutInflater().inflate(R.layout.dialog_contatos, null));
                   builder_whats_cont.setCancelable(true);

                   AlertDialog dialog_whats_cont = builder_whats_cont.create();
                   dialog_whats_cont.show();

                   mediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.whats3);
                   if (mediaPlayer2 != null){
                       mediaPlayer2.start();
                   }
               }
           }
       });
        viewPagerTab.setViewPager(viewPager);

        /*mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });

        mediaPlayer = MediaPlayer.create(this,R.raw.whats4);
        mediaPlayer.start();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair :
                deslogarUsuario();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;

            case R.id.menuConfiguracoes :
                abrirConfiguracoes();
                break;

            case R.id.menuSairTutorial:
                startActivity(new Intent(getApplicationContext(), FimWhats.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deslogarUsuario(){
        try {
            auth.signOut();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void abrirConfiguracoes(){
        startActivity(new Intent(getApplicationContext(), ConfiguracoesActivityWhats.class));
    }
}