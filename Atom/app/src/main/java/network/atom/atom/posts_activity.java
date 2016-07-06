package network.atom.atom;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class posts_activity extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.activity_posts_activity, container, false);
       FloatingActionButton floatingActionButton=(FloatingActionButton)view.findViewById(R.id.postsFabID);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"You clikced",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


}
