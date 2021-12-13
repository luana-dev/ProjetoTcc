package com.example.trytouse.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trytouse.R;
import com.example.trytouse.TelaInicialInsta;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.google.firebase.database.DatabaseReference;

public class CadastroInsta extends AppCompatActivity {
    private Button btn_gera_senhas, btn_cadastra;
    private TextView txt_senha;
    private DatabaseReference databaseReference;
    private String auth;
    private ClipboardManager myClipboard;
    private ClipData myClip;
    private ImageView back6, home6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_insta);

        back6 = findViewById(R.id.img_back);
        home6 = findViewById(R.id.img_home);

        back6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Insta_Activity3.class));
            }
        });

        home6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(CadastroInsta.this);

        builder.setView(getLayoutInflater().inflate(R.layout.dialog_tela_cad_insta, null));
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();

        btn_gera_senhas = findViewById(R.id.btn_gera_senha_insta);
        btn_cadastra = findViewById(R.id.btn_cad_insta);
        txt_senha = findViewById(R.id.txt_senhas2);

        databaseReference = ConfiguracaoFirebas.getFirebaseDatabase();
        auth = UsuarioFirebaseInfo.getIdUsuario();

        btn_gera_senhas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = gerarSenhaAleatoria();
                txt_senha.setText(txt);
                databaseReference.child("senhas").child("senhas_insta").child(auth).setValue(txt);
            }
        });

        btn_cadastra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TelaInicialInsta.class));
            }
        });

        txt_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String text;
                text = txt_senha.getText().toString();

                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getApplicationContext(), "Text Copied",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String gerarSenhaAleatoria() {
        int qtdeMaximaCaracteres = 8;
        String[] caracteres = { "a", "1", "b", "2", "4", "5", "6", "7", "8",
                "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
                "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
                "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
                "V", "W", "X", "Y", "Z" };

        StringBuilder senha = new StringBuilder();

        for (int i = 0; i < qtdeMaximaCaracteres; i++) {
            int posicao = (int) (Math.random() * caracteres.length);
            senha.append(caracteres[posicao]);
        }
        return senha.toString();
    }
}