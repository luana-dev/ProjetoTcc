package com.example.trytouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.trytouse.R;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SenhasActivity extends AppCompatActivity {
    Button btn_gera_senhas;
    TextView txt_senhas;
    DatabaseReference referenciafire = FirebaseDatabase.getInstance().getReference();
     private String auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senhas);

        btn_gera_senhas = findViewById(R.id.btn_gera_senha);
        txt_senhas = findViewById(R.id.txt_senhas);

        auth = UsuarioFirebaseInfo.getIdUsuario();

        btn_gera_senhas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = gerarSenhaAleatoria();
                txt_senhas.setText(txt);
                referenciafire.child("senhas").child("senhas_insta").child(auth).setValue(txt);
            }
        });

        /**ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);**/
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
