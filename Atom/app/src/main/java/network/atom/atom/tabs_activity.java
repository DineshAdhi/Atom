package network.atom.atom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class tabs_activity extends AppCompatActivity implements DataDumper{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_activity);
    }
}
