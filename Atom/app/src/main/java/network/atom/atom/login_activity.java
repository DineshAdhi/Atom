package network.atom.atom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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

    public void initialize()
    {
        emailLayout=(TextInputLayout)findViewById(R.id.emailLayout);
        inputField=(EditText)findViewById(R.id.emailInputField);
        backButton=(Button)findViewById(R.id.backButton);
        nextButton=(Button)findViewById(R.id.nextButton);
        signupLink=(TextView)findViewById(R.id.signupLink);
        signinProfilePicImageView=(ImageView)findViewById(R.id.signinPicImageView);
        dialog=new ProgressDialog(login_activity.this);
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
                        dumper.userEmail=inputField.getText().toString();
                        clickCount++;
                        nextButton.setText("Login");
                        backButton.setText("Back");
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
               if(backButton.getText().toString().equals("Back"))
               {
                   clickCount=0;
                   buttonClickFunction();
               }
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
                Map<String,Object> map=(Map<String, Object>) dataSnapshot.getValue();
                if(map.get("Email").equals(dumper.signupEmail))
                {
                    Log.e("Map",map.toString());
                    try {
                        findViewById(R.id.view).setVisibility(View.GONE);
                        URL newurl=new URL(map.get("PhotoURL").toString());
                        Bitmap bitmap= BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                        signinProfilePicImageView.setImageBitmap(bitmap);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (Exception e)
                    {
                        Log.e("Image Exception",e.getMessage());
                    }
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
}
