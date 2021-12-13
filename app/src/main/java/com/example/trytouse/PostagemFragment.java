package com.example.trytouse;

import android.Manifest;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.trytouse.activity.PubFotoActivity;
import com.example.trytouse.helper.Permissao;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.ByteArrayOutputStream;

public class PostagemFragment extends Fragment {

    private Button btn_abre_cam, btn__carrega_gal;
    private static final int SELECAO_CAMERA = 1;
    private static final int SELECAO_GALERIA = 200;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    MediaPlayer mediaPlayer;

    public PostagemFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_postagem, container, false);

        AlertDialog.Builder builder_post = new AlertDialog.Builder(getActivity());

        builder_post.setView(getLayoutInflater().inflate(R.layout.dialog_postagem, null));
        builder_post.setCancelable(true);

        AlertDialog dialog_post = builder_post.create();
        dialog_post.show();

        Permissao.validaPermissoes(permissoesNecessarias, getActivity(), 1 );

        btn_abre_cam = view.findViewById(R.id.btn_abre_cam);
        btn__carrega_gal = view.findViewById(R.id.btn_carrega_foto);

        btn_abre_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Fragment frag = PostagemFragment.this;
                getActivity().startActivityFromFragment(PostagemFragment.this, i, SELECAO_CAMERA);
            }
        });

        btn__carrega_gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Fragment frag = PostagemFragment.this;
                getActivity().startActivityFromFragment(PostagemFragment.this, i, SELECAO_GALERIA);
            }
        });

        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.insta5);
        if (mediaPlayer != null){
            mediaPlayer.start();
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == TelaInicialInsta.RESULT_OK && data != null) {
                if (requestCode == SELECAO_GALERIA){
                    // Do something with imagePath
                    Uri localImagemSelecionada = data.getData();
                    Bitmap photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), localImagemSelecionada);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] bytes = stream.toByteArray();

                    //Envia imagem escolhida para tela de publicacao
                    Intent i = new Intent(getActivity(), PubFotoActivity.class);
                    i.putExtra("fotoEscolhida", bytes );
                    startActivity( i );
                }
                if (requestCode == SELECAO_CAMERA){
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] bytes = stream.toByteArray();

                    //Envia imagem escolhida para tela de publicacao
                    Intent i = new Intent(getActivity(), PubFotoActivity.class);
                    i.putExtra("fotoEscolhida", bytes );
                    startActivity( i );
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}