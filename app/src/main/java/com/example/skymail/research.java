package com.example.skymail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import java.util.Objects;


public class research extends Fragment {
    Toolbar toolbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate( R.layout.fragment_research, container, false );
        toolbar = root.findViewById( R.id.toolbar );
        //this line using Drawer Interface method for disable navigation drawer (DrawerLayout.LOCK_MODE_LOCKED_CLOSED) and false for
        ((MainActivity5) Objects.requireNonNull( getActivity() )).enableDisableDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,false);
        return root;
    }




}
