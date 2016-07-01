package network.atom.atom;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class signup_activity extends AppCompatActivity implements DataDumper {

    ImageView profilepicImageView;
    TextInputLayout signupInputLayout;
    EditText signupInputField;
    Button signupNextButton,signupBackButton;
    int count=0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0)
        {
            Uri uri=data.getData();
            profilepicImageView.setImageURI(uri);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);

        initialize();
        buttonActions();

        profilepicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("images/*");
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
    }

    private void signupAction() {
        if(count==0)
        {
            //e-mail
            signupInputLayout.setHint("Username");
            dumper.signupEmail=signupInputField.getText().toString();
            signupInputField.setText("");
            count++;
        }
        else if(count==1)
        {
            //username
            signupInputLayout.setHint("Password");
            dumper.signupUsername=signupInputField.getText().toString();
            signupInputField.setText("");
            count++;
        }
        else if(count==2)
        {
            //password
            signupInputLayout.setHint("Mobile");
            dumper.signupPassword=signupInputField.getText().toString();
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
        Log.e("Email",dumper.signupEmail.toString());
        Log.e("Pass",dumper.signupPassword.toString());
        Log.e("Mobile",dumper.signupMobile.toString());
        Log.e("Username",dumper.signupUsername.toString());
    }

    private void initialize() {
        profilepicImageView=(ImageView)findViewById(R.id.profilepicImageView);
        signupInputLayout=(TextInputLayout)findViewById(R.id.signupInputLayout);
        signupInputField=(EditText)findViewById(R.id.signupInputField);
        signupNextButton=(Button)findViewById(R.id.signupnextButton);
        signupBackButton=(Button)findViewById(R.id.signupbackButton);
    }
}