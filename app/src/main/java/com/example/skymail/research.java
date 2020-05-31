package com.example.skymail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skymail.Adapter.RecentContactAdapter;
import com.example.skymail.Data.Contacts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class research extends Fragment {
    private ArrayList<Contacts> ContactsListe;
    private String userID;
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private RecentContactAdapter recentContactAdapter;
    private ImageButton add;
    private LinearLayout ToContact;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate( R.layout.fragment_research, container, false );

        userID = MainActivity5.userID;
        rv = root.findViewById( R.id.contactRV );
        layoutManager = new LinearLayoutManager( getActivity(),LinearLayoutManager.HORIZONTAL,false);
        add = root.findViewById( R.id.addcontact );
        ToContact = root.findViewById( R.id.ToContactList );

        //this line using Drawer Interface method for disable navigation drawer (DrawerLayout.LOCK_MODE_LOCKED_CLOSED) and false for
        ((MainActivity5) Objects.requireNonNull( getActivity() )).enableDisableDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,false);
        ContactsListe = new ArrayList<>();

        recentContactAdapter = new RecentContactAdapter(getContext(),ContactsListe);

        DatabaseReference contacts = FirebaseDatabase.getInstance().getReference("Contacts").child( userID ).child( "contacts" );
        Query query = contacts.orderByChild( "userID" ).equalTo( userID );
        Toast.makeText( getContext(),userID,Toast.LENGTH_LONG ).show();
        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot c : dataSnapshot.getChildren()){
                    Contacts contacts1 = c.getValue(Contacts.class);
                    ContactsListe.add(contacts1);

                    rv.setLayoutManager( layoutManager );
                    rv.setAdapter( recentContactAdapter );
                    rv.setItemAnimator(new DefaultItemAnimator());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addcontact = new Intent( getActivity(), AddContact.class );
                startActivity( addcontact );
            }
        } );

        ToContact.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contact = new Intent( getActivity(), MyContactLIst.class );
                startActivity( contact );
            }
        } );





        return root;
    }




}
