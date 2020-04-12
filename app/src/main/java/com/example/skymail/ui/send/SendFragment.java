package com.example.skymail.ui.send;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.skymail.Adapter.InboxAdapter;
import com.example.skymail.Adapter.SendAdapter;
import com.example.skymail.Data.Messages;
import com.example.skymail.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.skymail.Data.io.access;

public class SendFragment extends Fragment {

    public static Context SendFragmentContext;
    private String email;
    private ArrayList<Messages> SendedMessagesListe;
    private RecyclerView.LayoutManager manager;
    private SendAdapter sendadapter;
    private RecyclerView rv;
    private String DeletedMessage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_send, container, false);
        rv = root.findViewById( R.id.sendRV );
        email = access( "email",SendFragmentContext );
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference messagedatabase = firebaseDatabase.getReference( "SendedMessages" );
        Query query = messagedatabase.orderByChild( "from" ).equalTo( email );
        //Declaring the ArrayList
        SendedMessagesListe = new ArrayList<>();
        //Layout Manager
        manager = new LinearLayoutManager( getActivity() );

        //Adapter
        sendadapter = new SendAdapter(getActivity(), SendedMessagesListe);


         query.addValueEventListener( new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for(DataSnapshot SendedMessages :dataSnapshot.getChildren()){
                     Messages message = SendedMessages.getValue(Messages.class);
                     SendedMessagesListe.add( message );
                     //LAYOUT MANAGER
                     rv.setLayoutManager(manager);
                     //setAdapter
                     rv.setAdapter(sendadapter);


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
                        DeletedMessage = SendedMessagesListe.get( position ).getMessagID();
                        Query query1 = messagedatabase.orderByChild( "messagID" ).equalTo( DeletedMessage );
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
                        SendedMessagesListe.remove( position );
                        sendadapter.notifyItemRemoved( position );


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
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SendFragmentContext=context;
    }
}