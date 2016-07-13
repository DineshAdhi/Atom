package network.atom.atom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

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

    List<Map<String, Object>> userDetailsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_friends_activity, container, false);
        userDetailsList = new ArrayList<Map<String, Object>>();
        final ListViewCompat listViewCompat=(ListViewCompat)view.findViewById(R.id.friendsListView);
        services.databaseReference.child("UserDetails").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> userDetails = (Map<String, Object>) dataSnapshot.getValue();
                if(!dumper.currentUserId.equals(userDetails.get("UserID")))
                {
                    userDetailsList.add(userDetails);
                    FriendsListAdapter adapter=new FriendsListAdapter(userDetailsList);
                    listViewCompat.setAdapter(adapter);
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
        return view;
    }

    public class FriendsListAdapter extends BaseAdapter
    {
        List<Map<String,Object>> userDetails;
        LayoutInflater inflater;

        public FriendsListAdapter(List<Map<String,Object>> userDetails)
        {
            this.userDetails=userDetails;
            inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return userDetails.size();
        }

        @Override
        public Object getItem(int i) {
            return userDetails.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view=inflater.inflate(R.layout.friendlistxml,null);
            final viewHolder v=new viewHolder();
            v.friendsProfileImageView=(ImageView)view.findViewById(R.id.friendsProfileImageView);
            v.friendsNameTextView=(TextView)view.findViewById(R.id.friendsNameTextView);
            v.addFriendButton=(Button)view.findViewById(R.id.addFriendButton);
            Picasso.with(getContext()).load(userDetails.get(i).get("PhotoURL").toString()).fit().into(v.friendsProfileImageView);
            v.friendsNameTextView.setText(userDetails.get(i).get("Username").toString());
            try
            {
                if(userDetails.get(i).get("FriendRequests").toString().contains(dumper.currentUserId))
                {
                    Log.e("Request Found",userDetails.get(i).get("Username").toString());
                    v.addFriendButton.setBackground(getResources().getDrawable(R.color.colorPrimary));
                    v.addFriendButton.setTextColor(getResources().getColor(R.color.textWhileColor));
                    v.addFriendButton.setText("Requested");
                }
            }
            catch (NullPointerException e)
            {

            }

            v.addFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    v.addFriendButton.setBackground(getResources().getDrawable(R.color.colorPrimary));
                    v.addFriendButton.setTextColor(getResources().getColor(R.color.textWhileColor));
                    v.addFriendButton.setText("Requested");

                    services.databaseReference.child("UserDetails/"+userDetails.get(i).get("UserID").toString()).child("FriendRequests")
                            .child(dumper.currentUserId).setValue(dumper.currentUserId);

                    services.databaseReference.child("UserDetails/"+dumper.currentUserId).child("SentRequests")
                            .child(userDetails.get(i).get("UserID").toString()).setValue(userDetails.get(i).get("UserID").toString());
                }
            });

            return view;
        }

        public class viewHolder
        {
            ImageView friendsProfileImageView;
            TextView friendsNameTextView;
            Button addFriendButton;
        }
    }


    }

