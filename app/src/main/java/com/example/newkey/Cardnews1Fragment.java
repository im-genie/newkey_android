package com.example.newkey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


public class Cardnews1Fragment extends Fragment {

    public Cardnews1Fragment() {
        // Required empty public constructor
    }

    public static Cardnews1Fragment newInstance(String param1, String param2) {
        Cardnews1Fragment fragment = new Cardnews1Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cardnews1, container, false);
    }
}