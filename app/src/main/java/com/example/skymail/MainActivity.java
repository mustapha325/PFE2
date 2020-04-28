package com.example.skymail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.skymail.Data.Users;
import org.jetbrains.annotations.NotNull;
import static com.example.skymail.Data.io.store;

public class MainActivity extends AppCompatActivity {



    Button signin;
    Button login;
    DatabaseReference userdatabase;
    EditText email,password;
    Query Query;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signin = findViewById( R.id.Signin);
        login = findViewById(R.id.Login);
        userdatabase = FirebaseDatabase.getInstance().getReference();
        email = findViewById( R.id.Email2);
        password = findViewById(R.id.Password2);

        signin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign = new Intent(MainActivity.this,MainActivity2.class);
                startActivity(sign);
            }
        } );


        login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query = userdatabase.child("users").orderByChild("email").equalTo(email.getText().toString());


                Query.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot users: dataSnapshot.getChildren()){
                                Users user = users.getValue( Users.class );

                                assert user != null;
                                if (user.getPassword().equals( password.getText().toString())){
                                    Intent login = new Intent(MainActivity.this,MainActivity5.class);
                                    store("email:"+user.getEmail()+";"+"nom:"+user.getFullname()+";"+"id:"+user.getUserID()+";"+"date:"+user.getBirthdate()+";"+"gender:"+user.getGender()+";"+"pass:"+user.getPassword()+";",MainActivity.this);
                                    login.putExtra( "ID",user.getUserID());
                                    startActivity(login);
                                }else{
                                    Toast.makeText( MainActivity.this,"mot pass incorrect",Toast.LENGTH_LONG).show();
                            }
                        }
                        }
                        else {
                            Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );




            }
        } );






    }

}
