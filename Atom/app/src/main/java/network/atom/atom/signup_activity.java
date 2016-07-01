package network.atom.atom;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);

        initialize();
        buttonActions();

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
        if()
    }

    private void initialize() {
        profilepicImageView=(ImageView)findViewById(R.id.profilepicImageView);
        signupInputLayout=(TextInputLayout)findViewById(R.id.signupInputLayout);
        signupInputField=(EditText)findViewById(R.id.signupInputField);
        signupNextButton=(Button)findViewById(R.id.signupnextButton);
        signupBackButton=(Button)findViewById(R.id.signupbackButton);
    }
}