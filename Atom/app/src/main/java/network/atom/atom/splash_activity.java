package network.atom.atom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

public class splash_activity extends AppCompatActivity implements DataDumper{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        services.databaseReference.keepSynced(true);
        Intent intent=new Intent(splash_activity.this,login_activity.class);
        startActivity(intent);


    }
}
