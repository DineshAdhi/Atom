package network.atom.atom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class friends_activity extends Fragment implements DataDumper {

    List<Map<String,String>> userDetailsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_friends_activity,container,false);
        final ListView friendsListView=(ListView)view.findViewById(R.id.friendsListView);
        userDetailsList=new ArrayList<Map<String, String>>();
        services.databaseReference.child("UserDetails").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,String> userDetails=(Map<String,String>)dataSnapshot.getValue();
                userDetailsList.add(userDetails);
                FriendListAdapter adapter=new FriendListAdapter(getActivity(),userDetailsList);
                friendsListView.setAdapter(adapter);
                Log.e("USerDetailsFirebase",userDetailsList.toString());
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
        return view;
    }

    public class FriendListAdapter extends BaseAdapter
    {
        List<Map<String,String>> userDetailsList;
        LayoutInflater inflater;
        public FriendListAdapter(Context context, List<Map<String,String>> userDetailsList)
        {
            this.userDetailsList=userDetailsList;
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return userDetailsList.size();
        }

        @Override
        public Object getItem(int i) {
            return userDetailsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=inflater.inflate(R.layout.friendlistxml,);
            viewholder viewholder=new viewholder();
            Log.e("USerDetailsAdapter",userDetailsList.get(i).toString());
            viewholder.friendsImageView=(ImageView)view.findViewById(R.id.friendsImageView);
            new FriendsListDownloadImage(viewholder.friendsImageView).execute(userDetailsList.get(i).get("PhotoURL").toString());
            viewholder.addFriendButton=(Button)view.findViewById(R.id.addFriendButton);
            viewholder.friendsName=(TextView)view.findViewById(R.id.friendNameTextView);
            viewholder.friendsName.setText(userDetailsList.get(i).get("Username").toString());
            return view;
        }

        public class viewholder
        {
            ImageView friendsImageView;
            TextView friendsName;
            Button addFriendButton;
        }

        public class FriendsListDownloadImage extends AsyncTask<String,Void,Bitmap>
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
    }
}
