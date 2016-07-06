package network.atom.atom;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class tabs_activity extends AppCompatActivity implements DataDumper{

    ViewPager viewPager;
    TabLayout tablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_activity);

        viewPager=(ViewPager)findViewById(R.id.viewPagerId);
        setViewPager(viewPager);

        tablayout=(TabLayout)findViewById(R.id.tabsId);
        tablayout.setupWithViewPager(viewPager);
        setIcons();
    }

    public void setIcons()
    {
        tablayout.getTabAt(0).setIcon(R.mipmap.posts);
        tablayout.getTabAt(1).setIcon(R.mipmap.messages);
        tablayout.getTabAt(2).setIcon(R.mipmap.friends);
        tablayout.getTabAt(3).setIcon(R.mipmap.ic_notifications_white_36dp);
        tablayout.getTabAt(4).setIcon(R.mipmap.profile);
    }
    public void setViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new posts_activity(),"");
        adapter.addFragment(new chats_activity(),"");
        adapter.addFragment(new friends_activity(),"");
        adapter.addFragment(new notifications_activity(),"");
        adapter.addFragment(new profile_activity(),"");
        viewPager.setAdapter(adapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter
    {
        final List<Fragment> fragmentList=new ArrayList<>();
        final List<String> fragmentStrings=new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentStrings.get(position);
        }

        public void addFragment(Fragment fragment,String title)
        {
            fragmentList.add(fragment);
            fragmentStrings.add(title);
        }
    }
}
