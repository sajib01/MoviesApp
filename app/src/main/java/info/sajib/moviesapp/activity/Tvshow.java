package info.sajib.moviesapp.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import info.sajib.moviesapp.R;
import info.sajib.moviesapp.fragments.TvairingToday;
import info.sajib.moviesapp.fragments.Tvon_The_Air;
import info.sajib.moviesapp.fragments.Tvpopular;
import info.sajib.moviesapp.fragments.Tvtop_Rated;

/**
 * Created by sajib on 09-04-2016.
 */
public class Tvshow extends AppCompatActivity {
    private ViewPager2 viewpager;
    private TabLayout slidingTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tvshow);

        viewpager=  findViewById(R.id.tv_show_viewpager);
        slidingTabLayout= findViewById(R.id.tv_show_slidingtab);

        viewpager.setAdapter(new TVFragmentAdapter(this));

        new TabLayoutMediator(slidingTabLayout, viewpager,
                (tab, position) -> tab.setText("OBJECT " + (position + 1))
        ).attach();
    }

    class TVFragmentAdapter extends FragmentStateAdapter {
        String tabs[];

        public TVFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
            tabs=getResources().getStringArray(R.array.Tvtab);
        }


        @NonNull
        @Override
        public androidx.fragment.app.Fragment createFragment(int position) {
            Fragment fragment=null;
            if(position==0)
            {
                fragment=new TvairingToday();
            }
            if(position==1)
            {
                fragment=new Tvon_The_Air();
            }
            if(position==2)
            {
                fragment=new Tvtop_Rated();
            }
            if(position==3)
            {
                fragment=new Tvpopular();
            }

            return fragment;
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
