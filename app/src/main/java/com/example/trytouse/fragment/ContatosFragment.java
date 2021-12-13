package com.example.trytouse.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.trytouse.R;
import com.example.trytouse.RecyclerItemClickListener;
import com.example.trytouse.model.Usuario;
import com.example.trytouse.activity.ChatActivity;
import com.example.trytouse.adapter.ContatosAdapter;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ContatosFragment extends Fragment {

    private RecyclerView recview_contatos;
    private ContatosAdapter adapter_con;
    private ArrayList<Usuario> listaContatos = new ArrayList<>();
    private DatabaseReference usuariosref;
    private DatabaseReference mDatabase;
    private ValueEventListener valueEventListenerContatos;
    private FirebaseUser usuarioAtual;
    private FirebaseAuth auth;

    public ContatosFragment() {
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
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        /*AlertDialog.Builder builder_whats_cont = new AlertDialog.Builder(getActivity());

        builder_whats_cont.setView(getLayoutInflater().inflate(R.layout.dialog_contatos, null));
        builder_whats_cont.setCancelable(true);

        AlertDialog dialog_whats_cont = builder_whats_cont.create();
        dialog_whats_cont.show();*/

        recview_contatos = view.findViewById(R.id.recview_contatos);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        usuariosref = mDatabase.child("usuarios");
        auth = FirebaseAuth.getInstance();
        usuarioAtual = auth.getCurrentUser();

        adapter_con = new ContatosAdapter(listaContatos, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recview_contatos.setLayoutManager(layoutManager);
        recview_contatos.setHasFixedSize(true);
        recview_contatos.setAdapter(adapter_con);

        recview_contatos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recview_contatos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Usuario usuarioSelected = listaContatos.get(position);

                                Intent i = new Intent(getActivity(), ChatActivity.class);
                                i.putExtra("chatContato", usuarioSelected);
                                startActivity(i);
                                //startActivity(new Intent(getActivity(), ChatActivity.class));
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        recuperarContatos();

        return view;
    }

    public void recuperarContatos(){
        valueEventListenerContatos = usuariosref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dados: snapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);

                    String emailUser = usuarioAtual.getEmail();
                    if (!emailUser.equals(usuario.getEmail())){
                        listaContatos.add(usuario);
                    }

                }

                adapter_con.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosref.removeEventListener(valueEventListenerContatos);
    }
}