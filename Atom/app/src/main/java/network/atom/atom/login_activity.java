package network.atom.atom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import butterknife.BindView;

public class login_activity extends AppCompatActivity implements DataDumper {


    TextInputLayout emailLayout;
    EditText inputField;
    Button exitButton,nextButton;
    TextView signupLink;
    ImageView signinProfilePicImageView;        // Required View for the activity
    int clickCount=0;
    ProgressDialog dialog;
    TextView signinEmailTextView,welcomeNoteTextView;
    long childerCount=0,dataBaseChilderCount;

    public void initialize() {
        emailLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        inputField = (EditText) findViewById(R.id.emailInputField);
        exitButton = (Button) findViewById(R.id.backButton);
        nextButton = (Button) findViewById(R.id.nextButton);
        signupLink = (TextView) findViewById(R.id.signupLink);
        signinProfilePicImageView = (ImageView) findViewById(R.id.signinPicImageView);  // Initialization of the required fields
        dialog = new ProgressDialog(login_activity.this);
        dialog.setCanceledOnTouchOutside(false);
        signinEmailTextView = (TextView) findViewById(R.id.signinEmailTextView);
        welcomeNoteTextView = (TextView) findViewById(R.id.welcomeNoteTextView);
        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();   // Initiallizing and declaring the UI elements throught a function
        buttonClickFunction();  // This function takes care of the clicking action of the buttons

    }

    private void buttonClickFunction() { // This function handles all the clicking function in a particular activity

        nextButton.setText("Next");
        exitButton.setText("Exit");
        emailLayout.setHint("E-mail");
        inputField.setText("");

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inputField.getText().toString().equals(""))
                {
                    if(clickCount==0)
                    {
                        dumper.userEmail=inputField.getText().toString();
                        verifyEmail();                                      // This ensures that the user entered an existing email
                        dialog.setMessage("Verifying your mail.. Please Wait");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        clickCount++;                                                   // This increments the variable clickCount based on the click
                        nextButton.setText("Login");                                    // directly changes the UI from E-mail input to Password.
                        emailLayout.setHint("Password");                                // A single editText field is used to obtain both e-mail and password
                        inputField.setText("");
                        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                    }
                   else {
                        dumper.userPassword = inputField.getText().toString();          // When the clickcount is incremented again, it starts the login() function
                        clickCount++;
                        FirebaseLogin();                // This directly starts the Tabs activity after authenticating the user
                    }

                }
                else
                {
                    Toast.makeText(login_activity.this, "Please input the credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);                   //This exits the app
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(login_activity.this,signup_activity.class); // This  starts signup_activity
                startActivity(intent);
            }
        });
    }

    private void verifyEmail() {  // This function uses a dummy password to check whether the email is registered
        String Password="Dinsh";  // A dummy password

        services.mAuth.signInWithEmailAndPassword(dumper.userEmail,Password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e.getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")) // This is an automated message that firebase throws when the password is wrong
                {
                    dialog.dismiss();
                    signupLink.setVisibility(View.VISIBLE);
                    inputField.setText("");
                    emailLayout.setHint("Email");
                    clickCount=0;
                    Toast.makeText(login_activity.this, "You seem to have entered an unregistered E-mail. Please Sign up first", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    setImage();     // If the email is authenticated, I am setting up the users image on the UI.
                }
            }
        });
    }

    private void setImage() {       // Sets up the image from the Firebase Storage API.
        Log.e("Ser Image","Called");
        services.databaseReference.child("UserDetails").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("Dumper Mail",dumper.userEmail);
                Log.e(Integer.toString((int)dataBaseChilderCount),Integer.toString((int)childerCount));
                final Map<String,String> map=(Map<String, String>) dataSnapshot.getValue();
                childerCount++;
                if(map.get("Email").equals(dumper.userEmail))   // Looks for the authenticated Email if it exists in our users database
                {
                    signinEmailTextView.setText(map.get("Email"));
                    dumper.currentUserId=map.get("UserID");
                    signinEmailTextView.setVisibility(View.INVISIBLE);
                    welcomeNoteTextView.setText("Welcome "+map.get("Username"));
                    welcomeNoteTextView.setVisibility(View.INVISIBLE);
                    signupLink.setVisibility(View.GONE);
                    Picasso.with(login_activity.this).load(map.get("PhotoURL")).fit()          // If exists, it returns the photoURL of the particular user
                            .into(signinProfilePicImageView, new Callback() {                  // and uses Piccaso library to set the image in the UI
                                @Override
                                public void onSuccess() {
                                    findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onError() {
                                    Toast.makeText(login_activity.this,"Check your internet connectivity",Toast.LENGTH_LONG).show();

                                }
                            });
                    findViewById(R.id.appTitleTextView).setVisibility(View.GONE);
                    signinEmailTextView.setVisibility(View.VISIBLE);
                    welcomeNoteTextView.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void FirebaseLogin() {
        dialog.setMessage("Loggin in ..");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        Log.e("mail",dumper.userEmail);
        Log.e("password",dumper.userPassword);

        services.mAuth.signInWithEmailAndPassword(dumper.userEmail,dumper.userPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        dialog.dismiss();
                        Intent intent=new Intent(login_activity.this,tabs_activity.class);
                        startActivity(intent);
                    }
                });

    }


}
