package com.example.skymail.ui.Inbox;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.skymail.Data.Messages;
import com.example.skymail.Data.Users;
import com.example.skymail.Message;
import com.example.skymail.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class InboxMessageContainer extends AppCompatActivity {

    private TextView subject,text,remail,name;
    private CircleImageView circleImageView;
    String email,picture,sub,messagetext;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.message_container );
        toolbar = findViewById(R.id.InboxMessageContainetToolBar);
        setSupportActionBar( toolbar );



        Intent intent = getIntent();
        email = intent.getStringExtra( "remail" );
        picture = intent.getStringExtra( "picture" );
        sub = intent.getStringExtra( "subject" );
        messagetext = intent.getStringExtra( "text" );


        subject = findViewById( R.id.SubjectHolder );
        text = findViewById( R.id.textholder );
        name = findViewById( R.id.senderfullname );
        remail = findViewById( R.id.recieveremail );
        circleImageView = findViewById( R.id.senderprofilepic );

        subject.setText( sub );
        text.setText( messagetext );
        Picasso.get().load( picture ).into( circleImageView );
        remail.setText( email );
        getSenderFullName();





    }


    public void getSenderFullName(){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("SendedMessages");
        final Query query = databaseReference.orderByChild( "from" ).equalTo( email );

        databaseReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot m : dataSnapshot.getChildren()){
                    Messages messages = m.getValue( Messages.class);
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                    assert messages != null;
                    Query query1 = databaseReference.orderByChild( "email" ).equalTo( messages.getTo() );
                    query1.addValueEventListener( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot u : dataSnapshot.getChildren()){
                                Users users = u.getValue(Users.class);
                                assert users != null;
                                name.setText( users.getFullname() );
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }


}
