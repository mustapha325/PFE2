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
    String email,picture,sub,messagetext,fullname;
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
        fullname = intent.getStringExtra( "FULLNAME" );


        subject = findViewById( R.id.SubjectHolder );
        text = findViewById( R.id.textholder );
        name = findViewById( R.id.senderfullname );
        remail = findViewById( R.id.recieveremail );
        circleImageView = findViewById( R.id.senderprofilepic );

        subject.setText( sub );
        text.setText( messagetext );
        Picasso.get().load( picture ).into( circleImageView );
        remail.setText( email );
        name.setText( fullname );






    }
}
