package com.example.trytouse;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trytouse.adapter.AdapterFeed;
import com.example.trytouse.adapter.ContatosAdapter;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.example.trytouse.model.Feed;
import com.example.trytouse.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FeedFragment extends Fragment {
    private RecyclerView rcFeed;
    private AdapterFeed ad_feed;
    private ArrayList<Feed> listaFeed = new ArrayList<>();
    private ValueEventListener valueEventListenerFeed;
    private DatabaseReference feedRef;
    private String idUsuarioLogado;
    MediaPlayer mediaPlayer;

    public FeedFragment() {
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
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        rcFeed = view.findViewById(R.id.recyclerFeed);

        idUsuarioLogado = UsuarioFirebaseInfo.getIdUsuario();
        feedRef = ConfiguracaoFirebas.getFirebaseDatabase().child("feed");
        ad_feed = new AdapterFeed(listaFeed, getActivity());

        AlertDialog.Builder builder_feed = new AlertDialog.Builder(getActivity());

        builder_feed.setView(getLayoutInflater().inflate(R.layout.dialog_feed, null));
        builder_feed.setCancelable(true);

        AlertDialog dialog_feed = builder_feed.create();
        dialog_feed.show();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcFeed.setLayoutManager(layoutManager);
        rcFeed.setHasFixedSize(true);
        rcFeed.setAdapter(ad_feed);

        listarFeed();

        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.insta3);
        if (mediaPlayer != null){
            mediaPlayer.start();
        }

        return view;
    }

    private void listarFeed(){
        valueEventListenerFeed = feedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dados: snapshot.getChildren()){
                    Feed feed = dados.getValue(Feed.class);

                        listaFeed.add(feed);
                }
                ad_feed.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.stop();
        feedRef.removeEventListener(valueEventListenerFeed);
    }

}