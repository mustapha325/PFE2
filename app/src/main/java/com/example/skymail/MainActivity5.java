package com.example.skymail;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.example.skymail.Data.UploadImages;
import com.example.skymail.Data.Users;
import com.example.skymail.Data.io;
import com.example.skymail.Interface.DrawerLocker;
import com.example.skymail.Interface.RecyclerItemClick;
import com.example.skymail.ui.Inbox.InboxMessageContainer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;
import static com.example.skymail.Data.io.access;


public class MainActivity5 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLocker {

    private AppBarConfiguration mAppBarConfiguration;
    static String email;
    private final int nav_menu_id = 16908332;
    private DrawerLayout mDrawerLayout;
    private NavController navController;
    private CircleImageView profileimage;
    private Uri UriImage;
    private Button addImage;
    private static final int PICK_IMAGE_REQUEST=1;
    private static final int TAKE_PICTURE = 101;
    private StorageReference storageReference;
    private String id;
    private TextView  useremail, userfullname;
    private String fullname;
    private String userID;



    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.fragment_main_reception );
        Intent intent = getIntent();
        profileimage = findViewById( R.id.profileimage);
        id = access( "id", this );
        fullname = access( "nom", this );
        email = access( "email", this );
        userID = intent.getStringExtra( "ID" );


        BottomNavigationView bottomNavigationView = findViewById( R.id.bottom_navigation );
        ImageButton settings = findViewById( R.id.settings );
        mDrawerLayout = findViewById( R.id.drawer_layout );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Message = new Intent( MainActivity5.this, com.example.skymail.Message.class );
                Message.putExtra( "ID",userID );
                startActivity( Message );
            }
        } );
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        NavigationView navigationView = findViewById( R.id.nav_view );
        View headerView = navigationView.getHeaderView( 0 );
         userfullname = headerView.findViewById( R.id.n );
         useremail= headerView.findViewById( R.id.e );
         userfullname.setText( fullname );
         useremail.setText( email );











        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_draft, R.id.nav_tools, R.id.nav_inbox, R.id.nav_send, R.id.nav_share, R.id.nav_Trash )
                .setDrawerLayout( drawer )
                .build();

        navController = Navigation.findNavController( this, R.id.nav_host_fragment );
        NavigationUI.setupActionBarWithNavController( this, navController, mAppBarConfiguration );
        NavigationUI.setupWithNavController( bottomNavigationView,navController );
        NavigationUI.setupWithNavController( navigationView, navController );

        storageReference = FirebaseStorage.getInstance().getReference("ProfilePicture");
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("users");
        Query query = user.orderByChild( "email" ).equalTo( useremail.getText().toString().trim() );
        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot u : dataSnapshot.getChildren()){
                     Users user = u.getValue(Users.class);
                    assert user != null;
                    DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference("ProfilePicture").child( user.getUserID() );
                    databaseReference.addValueEventListener( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot pic :dataSnapshot.getChildren()){
                               UploadImages picture = pic.getValue(UploadImages.class);
                                assert picture != null;
                                Picasso.get().load( picture.getmImageUrl() ).into( profileimage );
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




        profileimage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create an alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity5.this);
                builder.setTitle("Picture Picker");
                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.image_choser, null);
                builder.setView(customLayout);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();

                addImage=customLayout.findViewById( R.id.addimage );
                addImage.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenFileChoser();
                    }
                } );
            }
        } );













    }


    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mim = MimeTypeMap.getSingleton();
        return MimeTypeMap.getFileExtensionFromUrl(cr.getType( uri ));
    }


   private void OpenFileChoser(){
       Intent intent = new Intent();
       intent.setType( "image/*" );
       intent.setAction( Intent.ACTION_GET_CONTENT );
       startActivityForResult( intent,PICK_IMAGE_REQUEST);

    }
    private void OpenDeviceCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult( intent,TAKE_PICTURE );

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if(resultCode == RESULT_OK && (requestCode == PICK_IMAGE_REQUEST ||requestCode == TAKE_PICTURE )){
            DatabaseReference user = FirebaseDatabase.getInstance().getReference("users");
            Query query = user.orderByChild( "email" ).equalTo( useremail.getText().toString().trim() );
            query.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot u : dataSnapshot.getChildren()){
                        final Users user = u.getValue(Users.class);
                        assert user != null;

                        UriImage = data.getData();
                        //Storage reference
                        StorageReference ref = storageReference.child( System.currentTimeMillis()+ "." + getExtension( UriImage ) );
                        // Upload the Profile picture that we picked up to ref
                        ref.putFile(UriImage).addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Toast.makeText( MainActivity5.this,"Image uploaded",Toast.LENGTH_LONG ).show();


                            }
                        }).addOnCompleteListener( new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                //get the uploaded image reference to retrieve her DownloadUrl
                                StorageReference UploadedImageReference =  Objects.requireNonNull( Objects.requireNonNull( task.getResult() ).getMetadata() ).getReference();
                                assert UploadedImageReference != null;
                                UploadedImageReference.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // add the DownloadUrl to a String variable
                                        String URL = uri.toString();
                                        //add the image information to the firebase database
                                        UploadImages uploadImages = new UploadImages(user.getUserID(),user.getFullname(),useremail.getText().toString().trim(),URL);
                                        DatabaseReference databaseReference1 =FirebaseDatabase.getInstance().getReference("ProfilePicture").child(user.getUserID() ).child( user.getUserID() );
                                        databaseReference1.setValue(uploadImages);
                                    }
                                } );


                            }
                        } )
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText( MainActivity5.this,"Error",Toast.LENGTH_LONG ).show();
                                    }
                                });}
                    profileimage.setImageURI(UriImage);

                    }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main_activity5, menu );
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController( this, R.id.nav_host_fragment );
        return NavigationUI.navigateUp( navController, mAppBarConfiguration )
                || super.onSupportNavigateUp();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent i = new Intent( MainActivity5.this, SettingsActivity.class );
                startActivity( i );
                break;

            case R.id.action_about:
                Toast.makeText( MainActivity5.this, "About ???", Toast.LENGTH_LONG ).show();
                break;

            case nav_menu_id:
                mDrawerLayout.openDrawer( Gravity.LEFT );
                break;
        }


        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        return false;
    }

    public void settings(View view) {
        Intent settings = new Intent( MainActivity5.this, SettingsActivity.class );
        startActivity( settings );
    }


    @Override
    //DrawerLocker method
    public void enableDisableDrawer(int mode,boolean mode2) {
        //disable
        mDrawerLayout.setDrawerLockMode(mode);
        //disable action button in toolbar
        Objects.requireNonNull( getSupportActionBar() ).setDisplayHomeAsUpEnabled(mode2);
    }


}
