package com.example.skymail;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skymail.Data.Messages;
import com.example.skymail.Data.UploadImages;
import com.example.skymail.Data.Users;
import com.example.skymail.Data.io;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity2 extends AppCompatActivity {
    EditText fullname;
    EditText email;
    EditText password;
    EditText date;
    DatePickerDialog datePickerDialog;
    EditText phone;
    Button confirm;
    ImageView back;
    RadioGroup radioGroup;
    RadioButton radioButton;
    FirebaseDatabase database;
    DatabaseReference userdatabase;
    Uri ProfilePicUri;
    StorageReference storageReference;
    CircleImageView profilepic;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );
        fullname = findViewById( R.id.Fullname );
        email = findViewById( R.id.Email );
        password = findViewById( R.id.Password );
        date = findViewById( R.id.Date );
        phone = findViewById( R.id.phonenumber);
        confirm = findViewById( R.id.Signin );
        profilepic = findViewById( R.id.profileIcon );
        radioGroup = findViewById( R.id.radioGroup );
        back = findViewById( R.id.GoBackIcon );
        database = FirebaseDatabase.getInstance();
        userdatabase = database.getReference("users");
        date.setOnClickListener( new View.OnClickListener() {

            Calendar calendar = Calendar.getInstance();
           final int year = calendar.get( Calendar.YEAR );
           final int month = calendar.get( Calendar.MONTH );
          final  int day = calendar.get( Calendar.DAY_OF_MONTH );
            public void onClick(View v) {
                datePickerDialog=new DatePickerDialog( MainActivity2.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int i, int ii, int iii) {
                        date.setText(day+"/"+(month+1)+"/"+year);

                    }
                },year,month,day );
                datePickerDialog.show();
            }
        } );



        confirm.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adduser();
                Intent confirm = new Intent( MainActivity2.this, MainActivity.class );
                startActivity( confirm );
            }
        } );


        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent( MainActivity2.this, MainActivity.class );
                startActivity( back );
            }
        } );

    }
    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mim = MimeTypeMap.getSingleton();
        return MimeTypeMap.getFileExtensionFromUrl(cr.getType( uri ));
    }


    public void Adduser(){
        int radiobuttinid = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById( radiobuttinid );
        final String Fullname = fullname.getText().toString().trim();
        final String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String Gender = radioButton.getText().toString().trim();
        String Date = date.getText().toString().trim();
        String phonenumber = phone.getText().toString().trim();
        final String ID = String.valueOf( System.currentTimeMillis());
        final Users user = new Users(ID,Fullname,Email,Password,Date,Gender,phonenumber);
        userdatabase.child( ID ).setValue(user);
        //add default profile picture with the new user
        //get image uri from drawable folder
        ProfilePicUri = (new Uri.Builder())
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(getResources().getResourcePackageName(R.drawable.user))
                .appendPath(getResources().getResourceTypeName(R.drawable.user))
                .appendPath(getResources().getResourceEntryName(R.drawable.user))
                .build();
        //Storage reference
        storageReference = FirebaseStorage.getInstance().getReference("DefaultProfilePicture");
        StorageReference ref = storageReference.child( System.currentTimeMillis()+ "." + getExtension( ProfilePicUri ));
        ref.putFile( ProfilePicUri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        } ).addOnCompleteListener( new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                StorageReference UploadedImageReference = Objects.requireNonNull( Objects.requireNonNull( task.getResult() ).getMetadata() ).getReference();
                assert UploadedImageReference != null;
                UploadedImageReference.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // add the DownloadUrl to a String variable
                        String URL = uri.toString();
                        //add the image information to the firebase database
                        UploadImages uploadImages = new UploadImages( ID, Fullname, Email, URL );
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference( "ProfilePicture" ).child( ID ).child(ID);
                        databaseReference1.setValue( uploadImages );
                    }
                } );


            }
        });
    }
}







