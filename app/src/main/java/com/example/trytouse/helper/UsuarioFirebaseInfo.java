package com.example.trytouse.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.trytouse.model.Usuario;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebaseInfo {

    public static String getIdUsuario(){
        return getUsuarioAtual().getUid();
    }

    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebas.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

    public static boolean atualizaFoto(Uri url){
        try{
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao utilizar foto de perfil");
                    }
                }
            });
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public static boolean atualizaNome(String nome){
        try{
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar nome de perfil");
                    }
                }
            });
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public static Usuario getUsuarioLogadoDados(){
        FirebaseUser user = getUsuarioAtual();

        Usuario usuario = new Usuario();
        usuario.setEmail(user.getEmail());
        usuario.setNome(user.getDisplayName());
        usuario.setId(user.getUid());

        if (user.getPhotoUrl() == null){
            usuario.setCaminhoFoto("");
        } else {
            usuario.setCaminhoFoto(user.getPhotoUrl().toString());
        }
        return usuario;
    }
}
