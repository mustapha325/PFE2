package com.example.skymail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.example.skymail.Data.Messages;
import com.example.skymail.Data.UploadImages;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import static com.example.skymail.Data.io.access;
public class Message extends AppCompatActivity {



    EditText to,subject,object1,message;
    TextView from;
    String ID;
    String email;
    FirebaseDatabase messagedatabase;





    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_message );

        from = findViewById( R.id.From );
        to = findViewById(R.id.To);
        object1 = findViewById( R.id.Object1);
        subject = findViewById( R.id.Subject );
        message = findViewById(R.id.messageText);

        messagedatabase = FirebaseDatabase.getInstance();
        email = access("email",Message.this);
        from.setText(email);
        ID = access( "id",Message.this );
        androidx.appcompat.widget.Toolbar toolbar = findViewById( R.id.toolbar2 );
        setSupportActionBar( toolbar );
        Objects.requireNonNull( getSupportActionBar() ).setTitle( "Message Composer" );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.items,menu);
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sendAction:
                sendMessage();
                finish();
                Toast.makeText( Message.this,ID+email,Toast.LENGTH_LONG ).show();
                return true;

        }

        return super.onOptionsItemSelected( item );
    }




  public void sendMessage(){
      final DatabaseReference root,root2;
      root = messagedatabase.getReference("InboxMessages");
      root2 =messagedatabase.getReference("SendedMessages");

      FirebaseDatabase database = FirebaseDatabase.getInstance();
      final DatabaseReference profilepicture = database.getReference("ProfilePicture");

          Query query1 = profilepicture.child( access( "nom",this ) );

          query1.addValueEventListener( new ValueEventListener() {
              String userID = ID;
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
                  }
                  Messages message = new Messages(userID,messageID,From,To,Subject,Object,Messagetext,ProfilePictureUri);
                  assert messageID != null;
                  root.child(messageID).setValue(message);
                  root2.child( messageID ).setValue( message );
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          } );
    }
}
