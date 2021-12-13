package com.example.trytouse;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.SearchView;

import com.example.trytouse.activity.PerfilAmigoActivity;
import com.example.trytouse.adapter.AdapterPesquisa;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.example.trytouse.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PesquisaFragment extends Fragment {

   private SearchView searchPesquisa;
   private RecyclerView recyclerPesquisa;
   private List<Usuario> listaUsuarios;
   private DatabaseReference usuarioRef;
   private AdapterPesquisa adapterPesquisa;
   private String idUsuarioLogado;
   MediaPlayer mediaPlayer;

    public PesquisaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        AlertDialog.Builder builder_pesquisa = new AlertDialog.Builder(getActivity());

        builder_pesquisa.setView(getLayoutInflater().inflate(R.layout.dialog_pesquisa, null));
        builder_pesquisa.setCancelable(true);

        AlertDialog dialog_pesquisa = builder_pesquisa.create();
        dialog_pesquisa.show();

        searchPesquisa = view.findViewById(R.id.searchPesquisa);
        recyclerPesquisa = view.findViewById(R.id.recyclerPesquisa);
        listaUsuarios = new ArrayList<>();
        usuarioRef = ConfiguracaoFirebas.getFirebaseDatabase().child("usuarios");
        idUsuarioLogado = UsuarioFirebaseInfo.getIdUsuario();

        recyclerPesquisa.setHasFixedSize(true);
        recyclerPesquisa.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterPesquisa = new AdapterPesquisa(listaUsuarios, getActivity());

        recyclerPesquisa.setAdapter(adapterPesquisa);

        recyclerPesquisa.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerPesquisa, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Usuario usuarioSelec = listaUsuarios.get(position);
                Intent i = new Intent(getActivity(), PerfilAmigoActivity.class);
                i.putExtra("usuarioSelecionado", usuarioSelec);
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        searchPesquisa.setQueryHint("Buscar usuários");
        searchPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String texto = newText.toUpperCase();
                pesquisar(texto);
                return true;
            }
        });

        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.insta4);
        if (mediaPlayer != null){
            mediaPlayer.start();
        }

        return view;
    }

    private void pesquisar(String texto){
        listaUsuarios.clear();

        if (texto.length() >= 2){
            Query query = usuarioRef.orderByChild("nome")
                    .startAt(texto)
                    .endAt(texto + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listaUsuarios.clear();

                    for (DataSnapshot ds : snapshot.getChildren()){
                        Usuario usuario = ds.getValue(Usuario.class);

                        if ( idUsuarioLogado.equals( usuario.getId() ) )
                            continue;

                        //adiciona usuário na lista
                        listaUsuarios.add( usuario );

                      //  listaUsuarios.add(ds.getValue(Usuario.class));
                    }
                    adapterPesquisa.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}