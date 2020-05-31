package com.example.skymail.ui.Inbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.skymail.Adapter.InboxAdapter;
import com.example.skymail.Data.Messages;
import com.example.skymail.Interface.RecyclerItemClick;
import com.example.skymail.MainActivity5;
import com.example.skymail.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import static com.example.skymail.Data.io.access;

import java.util.ArrayList;
import java.util.Objects;

public class InboxFragment extends Fragment implements RecyclerItemClick {


    private RecyclerView rv;
    private String email;
    private Context InboxFragmentContext;
    private ArrayList<Messages> InboxMessagesListe;
    private InboxAdapter inboxadapter;
    private RecyclerView.LayoutManager manager;
    private String DeletedMessage;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inbox, container, false);
        email = access("email", InboxFragmentContext);
        //Declaring the ArrayList
        InboxMessagesListe = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //reference for the inboxMessages
         final DatabaseReference messagedatabase = database.getReference("InboxMessages");
        //query to get message information
        final Query query = messagedatabase.orderByChild( "to" ).equalTo(email);
        //REFERENCE
        rv=  root.findViewById(R.id.inboxRV);
        //MANAGER
        manager = new LinearLayoutManager(getContext());
        //ADAPTER
        inboxadapter = new InboxAdapter( getActivity(),InboxMessagesListe,this);
       // retrieve Message
       query.addValueEventListener( new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for(DataSnapshot m :dataSnapshot.getChildren()){
                   Messages message = m.getValue(Messages.class);
                   InboxMessagesListe.add( message );
                   //LAYOUT MANAGER
                   rv.setLayoutManager(manager);
                   //setAdapter
                   rv.setAdapter(inboxadapter);
                   //animator
                   rv.setItemAnimator( new DefaultItemAnimator() );

               }


           }
           @SuppressLint("ShowToast")
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
               Toast.makeText( getContext(),"error",Toast.LENGTH_LONG);

           }
       } );









        ((MainActivity5) Objects.requireNonNull( getActivity() )).enableDisableDrawer( DrawerLayout.LOCK_MODE_UNLOCKED,true);
        // attaching the touch helper to recycler view
        //query to get profile picture

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
                        DeletedMessage = InboxMessagesListe.get( position ).getMessagID();
                        Query query1 = messagedatabase.orderByChild( "messagID" ).equalTo( DeletedMessage );
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot InboxMessages: dataSnapshot.getChildren()) {
                                    InboxMessages.getRef().removeValue();


                                }
                            }

                            @Override
                            public void onCancelled(@NotNull DatabaseError databaseError) {
                            }
                        });
                        InboxMessagesListe.remove( position );
                        inboxadapter.notifyItemRemoved( position );


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
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        InboxFragmentContext=context;
    }

    @Override
    public void OnItemClick(View v, int position) {
        Intent intent = new Intent( getActivity(),InboxMessageContainer.class );
        intent.putExtra( "remail",InboxMessagesListe.get( position ).getTo() );
        intent.putExtra( "text",InboxMessagesListe.get(position).getMessageText() );
        intent.putExtra( "subject",InboxMessagesListe.get( position ).getSubject() );
        intent.putExtra( "picture",InboxMessagesListe.get( position ).getSenderProfilePicture() );
        intent.putExtra( "FULLNAME",InboxMessagesListe.get( position ).getSenderFullName() );
        startActivity( intent );
    }
}
