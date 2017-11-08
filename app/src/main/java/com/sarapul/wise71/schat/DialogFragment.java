package com.sarapul.wise71.schat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sarapul.wise71.schat.R;


public class DialogFragment extends Fragment {

    public DialogFragment(){};

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_dialog, container, false);

        return view;
    }
}