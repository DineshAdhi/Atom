package network.atom.atom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class signup_activity extends AppCompatActivity implements DataDumper {

    ImageView profilepicImageView;
    TextInputLayout signupInputLayout;
    EditText signupInputField;
    Button signupNextButton,signupBackButton;
    Uri photoURL;
    ProgressDialog dialog;
    int count=0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0)
        {
            Uri uri=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                profilepicImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void UploadImageAndData(ImageView imageView)  {

        StorageReference imageReference=services.storageReference.child("images/"+dumper.signupUsername);

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();

        Bitmap bitmap=imageView.getDrawingCache();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] data=baos.toByteArray();

        UploadTask uploadTask=imageReference.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("ProfileImage","ProfilePic uploaded");
                Log.e("SnapShot",taskSnapshot.toString());
                photoURL=taskSnapshot.getDownloadUrl();
                UploadData();
            }
        });

    }

    public void UploadData()
    {
        Map<String,String> map=new HashMap<String,String>();
        map.put("Username",dumper.signupUsername);
        map.put("Email",dumper.signupEmail);
        map.put("PhotoURL",photoURL.toString());
        map.put("Mobile",dumper.signupMobile);
        services.databaseReference.child("UserDetails").push().setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        Toast.makeText(signup_activity.this,"Accoung Created Successfully, Please sign in to continue",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);

        initialize();
        buttonActions();

        profilepicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,0);
            }
        });

    }

    private void buttonActions() {
        signupNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(signupInputField.getText().toString().equals(""))
                {
                    Toast.makeText(signup_activity.this,"Please input the Credentials",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    signupAction();
                }
            }
        });

        signupBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count=0;
                signupInputField.setText("");
                signupInputLayout.setHint("Email");
                signupInputField.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
            }
        });
    }

    private void signupAction() {
        if(count==0)
        {
            //e-mail
            signupInputLayout.setHint("Username");
            signupInputField.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            dumper.signupEmail=signupInputField.getText().toString();
            signupInputField.setText("");
            count++;
        }
        else if(count==1)
        {
            //username
            signupInputLayout.setHint("Password");
            signupInputField.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            dumper.signupUsername=signupInputField.getText().toString();
            signupInputField.setText("");
            count++;
        }
        else if(count==2)
        {
            //password
            signupInputLayout.setHint("Mobile");
            signupInputField.setInputType(InputType.TYPE_CLASS_NUMBER);
            dumper.signupPassword=signupInputField.getText().toString();
            signupNextButton.setText("Sign up");
            signupInputField.setText("");
            count++;
        }
        else if(count==3)
        {
            //mobile
            dumper.signupMobile=signupInputField.getText().toString();
            signupUser();
        }
    }

    private void signupUser() {
        dialog.setMessage("Signing up...");
        dialog.setTitle("Connecting to the backend");
        dialog.show();
        Log.e("Email",dumper.signupEmail.toString());
        Log.e("Pass",dumper.signupPassword.toString());
        Log.e("Mobile",dumper.signupMobile.toString());
        Log.e("Username",dumper.signupUsername.toString());

        services.mAuth.createUserWithEmailAndPassword(dumper.signupEmail,dumper.signupPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UploadImageAndData(profilepicImageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(signup_activity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                count=0;
                signupInputLayout.setHint("Email");
                signupInputField.setText("");
                signupInputField.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
            }
        });

    }

    private void initialize() {
        profilepicImageView=(ImageView)findViewById(R.id.profilepicImageView);
        signupInputLayout=(TextInputLayout)findViewById(R.id.signupInputLayout);
        signupInputField=(EditText)findViewById(R.id.signupInputField);
        signupNextButton=(Button)findViewById(R.id.signupnextButton);
        signupBackButton=(Button)findViewById(R.id.signupbackButton);
        dialog=new ProgressDialog(signup_activity.this);
    }
}