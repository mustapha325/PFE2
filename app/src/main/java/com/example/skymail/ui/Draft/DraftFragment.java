package com.example.skymail.ui.Draft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skymail.Adapter.DraftAdapter;
import com.example.skymail.Data.Messages;
import com.example.skymail.Data.Users;
import com.example.skymail.Data.io;
import com.example.skymail.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DraftFragment extends Fragment {

DatabaseReference databaseReference;
RecyclerView rv;
RecyclerView.LayoutManager layoutManager;
DraftAdapter draftAdapter;
ArrayList<Messages> DraftedMessagesListe;
String DeletedMessage;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_draft, container, false);
        rv = root.findViewById( R.id.Draftrv );
        DraftedMessagesListe = new ArrayList<>();
        layoutManager = new LinearLayoutManager( getActivity() );
        databaseReference = FirebaseDatabase.getInstance().getReference("DraftMessages");
        draftAdapter = new DraftAdapter( getActivity(),DraftedMessagesListe);
        String email = io.access("email", Objects.requireNonNull( getActivity() ) );
        Query query = databaseReference.orderByChild( "from" ).equalTo( email );
        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot draft :dataSnapshot.getChildren()){
                    Messages DraftedMessages = draft.getValue(Messages.class);
                    DraftedMessagesListe.add( DraftedMessages );
                    //LAYOUT MANAGER
                    rv.setLayoutManager(layoutManager);
                    //setAdapter
                    rv.setAdapter(draftAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch (direction){
                    case ItemTouchHelper.LEFT:
                        DeletedMessage = DraftedMessagesListe.get( position ).getMessagID();
                        Query query1 = databaseReference.orderByChild( "messagID" ).equalTo( DeletedMessage );
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot mSnapshot: dataSnapshot.getChildren()) {
                                    mSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        DraftedMessagesListe.remove( position );
                        draftAdapter.notifyItemRemoved( position );


                        break;

                    case ItemTouchHelper.RIGHT:


                        break;
                }


            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper( simpleCallback );
        itemTouchHelper.attachToRecyclerView( rv);







        return root;
    }
}