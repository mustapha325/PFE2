package com.example.skymail;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skymail.Data.Messages;
import com.example.skymail.Data.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


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


    public void Adduser(){
        int radiobuttinid = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById( radiobuttinid );
        String Fullname = fullname.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String Gender = radioButton.getText().toString().trim();
        String Date = date.getText().toString().trim();
        String phonenumber = phone.getText().toString().trim();
        String ID = userdatabase.push().getKey();

        Users user = new Users(ID,Fullname,Email,Password,Date,Gender,phonenumber);

        assert ID != null;
        userdatabase.child(ID).setValue(user);

    }



}



