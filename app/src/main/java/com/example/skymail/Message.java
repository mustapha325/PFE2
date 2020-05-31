package com.example.skymail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skymail.Data.Contacts;
import com.example.skymail.Data.Messages;
import com.example.skymail.Data.UploadImages;
import com.example.skymail.Data.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import static com.example.skymail.Data.io.access;
public class Message extends AppCompatActivity {


    EditText to, subject, object1, message;
    TextView from,id;
    String ID;
    String email;
    FirebaseDatabase messagedatabase;
    Toolbar toolbar;
    DatabaseReference DraftRreference;
    public static String userID;
    private static String userFULLNAME;
    private static String recieverphonenumber;
    private String EmailFromContactInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_message );
        Intent intent = getIntent();
        from = findViewById( R.id.From );
        userID = intent.getStringExtra("ID");
        userFULLNAME = intent.getStringExtra( "FULLNAME" );
        recieverphonenumber = intent.getStringExtra("number");
        EmailFromContactInformation = intent.getStringExtra( "email" );

        to = findViewById( R.id.To );
        object1 = findViewById( R.id.Object1 );
        subject = findViewById( R.id.Subject );
        message = findViewById( R.id.messageText );
        toolbar = findViewById( R.id.MessageActivityToolbar );
        messagedatabase = FirebaseDatabase.getInstance();
        email = access( "email", Message.this );
        ID = access( "id", Message.this );
        from.setText( email );


        to.setText( EmailFromContactInformation );


        setSupportActionBar( toolbar );
        getSupportActionBar().setTitle( "Message Composer" );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeAsUpIndicator( R.drawable.x );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.items, menu );
        return super.onCreateOptionsMenu( menu );
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sendAction:
                contact();
                send();
                finish();
                Toast.makeText( Message.this, ID + email, Toast.LENGTH_LONG ).show();
                return true;
            case android.R.id.home:
                DraftMessage();
                finish();
                return true;


        }

        return super.onOptionsItemSelected( item );
    }



    /*public void getRecieverInfo(){
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("users");
        Query query = user.orderByChild( "email" ).equalTo( to.getText().toString().trim() );
        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot u : dataSnapshot.getChildren()){
                    Users user = u.getValue(Users.class);
                    assert user != null;
                    store2("email:"+user.getEmail()+";"+"nom:"+user.getFullname()+";"+"id:"+user.getUserID()+";"+"date:"+user.getBirthdate()+";"+"gender:"+user.getGender()+";"+"pass:"+user.getPassword()+";",Message.this);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }*/


    public void DraftMessage() {

        DraftRreference = FirebaseDatabase.getInstance().getReference( "DraftMessages" );
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference profilepicture = database.getReference( "ProfilePicture" ).child( userID );
        profilepicture.addValueEventListener( new ValueEventListener() {
            String userID = ID;
            String messageID = DraftRreference.push().getKey();
            String From = from.getText().toString().trim();
            String To = to.getText().toString().trim();
            String Object = object1.getText().toString().trim();
            String Subject = subject.getText().toString().trim();
            String Message = message.getText().toString().trim();
            String userFullname = userFULLNAME;
            String ProfilePictureUri;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot image : dataSnapshot.getChildren()) {
                    UploadImages ProfilePic = image.getValue( UploadImages.class );
                    assert ProfilePic != null;
                    ProfilePictureUri = ProfilePic.getmImageUrl();
                }

                Messages message = new Messages( userID, messageID, From, To, Subject, Object, Message, ProfilePictureUri,userFullname );
                assert messageID != null;
                DraftRreference.child( messageID ).setValue( message );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }


    public void send(){
        final DatabaseReference root,root2;
        root = messagedatabase.getReference("InboxMessages");
        root2 =messagedatabase.getReference("SendedMessages");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference profilepicture = database.getReference("ProfilePicture");

        Query query1 = profilepicture.child( userID );

        query1.addValueEventListener( new ValueEventListener() {
            String userID = Message.userID;
            String messageID = root.push().getKey();
            String From = from.getText().toString().trim();
            String To = to.getText().toString().trim();
            String Object = object1.getText().toString().trim();
            String Subject = subject.getText().toString().trim();
            String Messagetext = message.getText().toString().trim();
            String ProfilePictureUri;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot image :dataSnapshot.getChildren()){
                    UploadImages ProfilePic = image.getValue(UploadImages.class);
                    assert ProfilePic != null;
                    ProfilePictureUri = ProfilePic.getmImageUrl();
                    Messages message = new Messages(userID,messageID,From,To,Subject,Object,Messagetext,ProfilePictureUri,userFULLNAME);
                    assert messageID != null;
                    root.child(messageID).setValue(message);
                    root2.child( messageID ).setValue( message );
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    public void contact(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query query = databaseReference.orderByChild( "email" ).equalTo( to.getText().toString().trim() );
        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    Users users = d.getValue(Users.class);

                    DatabaseReference profilepicture = FirebaseDatabase.getInstance().getReference("ProfilePicture");
                    assert users != null;
                    Query query1 = profilepicture.child( users.getUserID() );

                    query1.addValueEventListener( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot i : dataSnapshot.getChildren()){
                                final UploadImages image = i.getValue(UploadImages.class);;

                                final DatabaseReference user = FirebaseDatabase.getInstance().getReference("users");
                                assert image != null;
                                Query query = user.orderByChild( "email" ).equalTo( image.getUserEmail() );
                                query.addValueEventListener( new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot u : dataSnapshot.getChildren()) {
                                            Users user = u.getValue( Users.class );
                                            assert user != null;
                                            DatabaseReference Contacts = FirebaseDatabase.getInstance().getReference("Contacts").child(userID).child( "contacts" );
                                            Contacts contact = new Contacts(image.getUserID(),image.getUserFullname(),image.getUserEmail(),image.getmImageUrl(),userID,user.getPhonenumber());
                                            Contacts.child( image.getUserID() ).setValue( contact );
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );



    }

}
