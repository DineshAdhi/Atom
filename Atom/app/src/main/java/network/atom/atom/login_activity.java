package network.atom.atom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login_activity extends AppCompatActivity implements DataDumper {


    TextInputLayout emailLayout;
    EditText inputField;
    Button backButton,nextButton;
    TextView signupLink;
    int clickCount=0;
    ProgressDialog dialog;

    public void initialize()
    {
        emailLayout=(TextInputLayout)findViewById(R.id.emailLayout);
        inputField=(EditText)findViewById(R.id.emailInputField);
        backButton=(Button)findViewById(R.id.backButton);
        nextButton=(Button)findViewById(R.id.nextButton);
        signupLink=(TextView)findViewById(R.id.signupLink);
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

    private void FirebaseLogin() {
        dialog.setMessage("Loggin in ..");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        Log.e("mail",dumper.userEmail);
        Log.e("password",dumper.userPassword);

    }
}
