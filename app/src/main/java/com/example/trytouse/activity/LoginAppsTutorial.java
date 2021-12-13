package com.example.trytouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trytouse.R;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAppsTutorial extends AppCompatActivity {

    private EditText edit_email, edit_senha;
    private Button btn_entrar;
    private TextView cadastrar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_apps_tutorial);

        auth = ConfiguracaoFirebas.getFirebaseAutenticacao();

        edit_email = findViewById(R.id.edit_login_email);
        edit_senha = findViewById(R.id.edit_login_senha);
        btn_entrar = findViewById(R.id.btn_login);
        cadastrar = findViewById(R.id.txt_cadastro);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CadastroAppsTutorial.class));
            }
        });

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logarUsuario(v);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if (usuarioAtual != null){
            abrirTelaTutorial();
        }
    }

    public void abrirTelaTutorial(){
        startActivity(new Intent(getApplicationContext(), SegundaActivity.class));
    }

    public void logarUsuario(View view){
        String textoEmail = edit_email.getText().toString();
        String textoSenha = edit_senha.getText().toString();

        if (!textoEmail.isEmpty()){
            if (!textoSenha.isEmpty()){

                auth.signInWithEmailAndPassword(
                        textoEmail, textoSenha
                ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            abrirTelaTutorial();
                        } else {
                            Toast.makeText(LoginAppsTutorial.this, "Erro ao autenticar o usuário!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(LoginAppsTutorial.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginAppsTutorial.this, "Preencha o email!", Toast.LENGTH_SHORT).show();
        }
    }
}