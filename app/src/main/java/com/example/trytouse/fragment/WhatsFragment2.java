package com.example.trytouse.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.trytouse.R;
import com.example.trytouse.activity.WhatsActivity4;

public class WhatsFragment2 extends Fragment {
    Button btn_frag2, btn_frag_ant2;

    public WhatsFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_whats2, container, false);

        btn_frag2 = view.findViewById(R.id.btn_frag2);
        btn_frag_ant2 = view.findViewById(R.id.btn_frag_ant2);

        btn_frag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),WhatsActivity4.class);
                startActivity(intent);
            }
        });

        btn_frag_ant2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return view;
    }
}