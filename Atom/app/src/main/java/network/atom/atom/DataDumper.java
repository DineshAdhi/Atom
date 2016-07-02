package network.atom.atom;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by dinesh10c04 on 30/6/16.
 */
public interface DataDumper {



    dataStorage dumper=new dataStorage();
    cloud services=new cloud();

    class dataStorage
    {
        String userEmail;
        String userPassword;

        String signupEmail,signupPassword,signupMobile,signupUsername;
    }

    class cloud
    {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReferenceFromUrl("gs://atom-69580.appspot.com");
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=database.getReference();
    }


}
