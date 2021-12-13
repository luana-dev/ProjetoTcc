package com.example.trytouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trytouse.R;
import com.example.trytouse.model.Usuario;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroAppsTutorial extends AppCompatActivity {
    private EditText txt_nome, txt_email, txt_senha, txt_phone;
    private Button cadastrar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_apps_tutorial);

        txt_nome = findViewById(R.id.editTextNome);
        txt_email = findViewById(R.id.editTextEmail);
        txt_senha = findViewById(R.id.editTextSenha);
        cadastrar = findViewById(R.id.btn_cadastrar);
        txt_phone = findViewById(R.id.editTextPhone);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarUsuario(v);
            }
        });

    }

    public void cadastrarUsuario(Usuario usuario){
        auth = ConfiguracaoFirebas.getFirebaseAutenticacao();
        auth.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Toast.makeText(CadastroAppsTutorial.this,
                            "Sucesso ao cadastrar o usuário!", Toast.LENGTH_SHORT).show();
                    UsuarioFirebaseInfo.atualizaNome(usuario.getNome());
                    finish();

                    try {
                        String idUser = task.getResult().getUser().getUid();
                        usuario.setId(idUser);
                        usuario.setCaminhoFoto("");
                        usuario.salvar();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                } else {
                        String excecao = "";
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e){
                            excecao = "Digite uma senha mais forte!";
                        } catch (FirebaseAuthInvalidCredentialsException e){
                            excecao = "Por favor, digite um e-mail válido!";
                        } catch (FirebaseAuthUserCollisionException e){
                            excecao = "Esta conta já foi cadastrada!";
                        } catch (Exception e) {
                            excecao = "Erro ao cadastrar o usuário!" + e.getMessage();
                            e.printStackTrace();
                        }

                        Toast.makeText(CadastroAppsTutorial.this,
                                excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarUsuario(View view){
        String textoNome = txt_nome.getText().toString();
        String textoEmail = txt_email.getText().toString();
        String textoSenha = txt_senha.getText().toString();
        String textoTel = txt_phone.getText().toString();

        if ( !textoNome.isEmpty() ){
            if (!textoTel.isEmpty()) {
                if (!textoEmail.isEmpty()) {
                    if (!textoSenha.isEmpty()) {
                        Usuario usuario = new Usuario();
                        usuario.setNome(textoNome);
                        usuario.setSenha(textoSenha);
                        usuario.setEmail(textoEmail);
                        usuario.setTelefone(textoTel);

                        cadastrarUsuario(usuario);
                    } else {
                        Toast.makeText(CadastroAppsTutorial.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroAppsTutorial.this, "Preencha o e-mail!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CadastroAppsTutorial.this, "Preencha o telefone!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(CadastroAppsTutorial.this, "Preencha o nome!", Toast.LENGTH_SHORT).show();
        }
    }
}