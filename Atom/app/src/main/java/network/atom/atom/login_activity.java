package network.atom.atom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class login_activity extends AppCompatActivity implements DataDumper {


    TextInputLayout emailLayout;
    EditText inputField;
    Button backButton,nextButton;
    TextView signupLink;
    ImageView signinProfilePicImageView;
    int clickCount=0;
    ProgressDialog dialog;
    TextView signinEmailTextView,welcomeNoteTextView;

    public void initialize()
    {
        emailLayout=(TextInputLayout)findViewById(R.id.emailLayout);
        inputField=(EditText)findViewById(R.id.emailInputField);
        backButton=(Button)findViewById(R.id.backButton);
        nextButton=(Button)findViewById(R.id.nextButton);
        signupLink=(TextView)findViewById(R.id.signupLink);
        signinProfilePicImageView=(ImageView)findViewById(R.id.signinPicImageView);
        dialog=new ProgressDialog(login_activity.this);
        signinEmailTextView=(TextView)findViewById(R.id.signinEmailTextView);
        welcomeNoteTextView=(TextView)findViewById(R.id.welcomeNoteTextView);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();  // Initiallizing and declaring the UI elements throught a function
        buttonClickFunction(); // This function takes care of the clicking action of the buttons

    }

    private void buttonClickFunction() {

        nextButton.setText("Next");
        backButton.setText("Exit");
        emailLayout.setHint("E-mail");
        inputField.setText("");

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inputField.getText().toString().equals(""))
                {
                    if(clickCount==0)
                    {
                        setImage();
                        dialog.setMessage("Checking your mail.. Please Wait");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        dumper.userEmail=inputField.getText().toString();
                        clickCount++;
                        nextButton.setText("Login");
                        emailLayout.setHint("Password");
                        inputField.setText("");
                    }
                   else {
                        dumper.userPassword = inputField.getText().toString();
                        clickCount++;
                        FirebaseLogin();
                    }

                }
                else
                {
                    Toast.makeText(login_activity.this, "Please input the credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent 
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(login_activity.this,signup_activity.class);
                startActivity(intent);
            }
        });
    }

    private void setImage() {
        Log.e("Ser Image","Called");
        services.databaseReference.child("UserDetails").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("Dumper Mail",dumper.userEmail);
                final Map<String,String> map=(Map<String, String>) dataSnapshot.getValue();
                if(map.get("Email").equals(dumper.userEmail))
                {
                    signinEmailTextView.setText(map.get("Email"));
                    signupLink.setVisibility(View.GONE);
                    welcomeNoteTextView.setText("Welcome "+map.get("Username"));
                    new DownloadImage().execute(map.get("PhotoURL"));
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
                        Intent intent=new Intent(login_activity.this,userlist_activity.class);
                        startActivity(intent);
                    }
                });

    }

    class DownloadImage extends AsyncTask<String,Void,Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bitmap;

            try {
                URL url=new URL(strings[0]);
                URLConnection conn=url.openConnection();
                conn.connect();
                InputStream is=conn.getInputStream();
                BufferedInputStream bis=new BufferedInputStream(is);
                bitmap=BitmapFactory.decodeStream(bis);
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            signinProfilePicImageView.setImageBitmap(bitmap);
            findViewById(R.id.appTitleTextView).setVisibility(View.GONE);
            dialog.dismiss();
        }
    }
}
