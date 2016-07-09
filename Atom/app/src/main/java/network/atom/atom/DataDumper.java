package network.atom.atom;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

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

     class FriendsListDownloadImage extends AsyncTask<String,Void,Bitmap>
    {
        ImageView imageView;
        public FriendsListDownloadImage(ImageView imageView)
        {
            this.imageView=imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url=new URL(strings[0]);
                URLConnection conn=url.openConnection();
                conn.connect();
                InputStream is=conn.getInputStream();
                BufferedInputStream bs=new BufferedInputStream(is);
                Bitmap bitmap= BitmapFactory.decodeStream(bs);
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
            imageView.setImageBitmap(bitmap);
        }
    }


    class dataStorage
    {
        String userEmail;
        String userPassword;
        String currentUserName,currentUserId;

        String signupEmail,signupPassword,signupMobile,signupUsername;

        String clickedFriendId;
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
