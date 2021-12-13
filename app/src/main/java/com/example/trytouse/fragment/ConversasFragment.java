package com.example.trytouse.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.trytouse.R;
import com.example.trytouse.RecyclerItemClickListener;
import com.example.trytouse.activity.ChatActivity;
import com.example.trytouse.adapter.ConversasAdapter;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.example.trytouse.model.Conversa;
import com.example.trytouse.model.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ConversasFragment extends Fragment {
    private RecyclerView recConversas;
    private ConversasAdapter adapter_conv;
    private ArrayList<Conversa> listaConversas = new ArrayList<>();
    private DatabaseReference database;
    private DatabaseReference conversasRef;
    private ChildEventListener childEventListenerConversa;

    public ConversasFragment() {
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
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        recConversas = view.findViewById(R.id.recyclerConversas);

        adapter_conv = new ConversasAdapter(listaConversas, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recConversas.setLayoutManager(layoutManager);
        recConversas.setHasFixedSize(true);
        recConversas.setAdapter(adapter_conv);

        recConversas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recConversas,
                        new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Conversa usuarioSelected = listaConversas.get(position);

                Intent i = new Intent(getActivity(), ChatActivity.class);
                i.putExtra("chatConversa", usuarioSelected);
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

       String identificadorUsuario = UsuarioFirebaseInfo.getIdUsuario();
        database = ConfiguracaoFirebas.getFirebaseDatabase();
        conversasRef = database.child("conversas")
                .child( identificadorUsuario );

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperaConversa();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversasRef.removeEventListener(childEventListenerConversa);
    }

    public void recuperaConversa(){
        childEventListenerConversa = conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Conversa conversa = snapshot.getValue( Conversa.class );
                listaConversas.add( conversa );
                adapter_conv.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}