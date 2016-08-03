package info.sajib.moviesapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import info.sajib.moviesapp.R;
import info.sajib.moviesapp.fragments.TvairingToday;
import info.sajib.moviesapp.fragments.Tvon_The_Air;
import info.sajib.moviesapp.fragments.Tvpopular;
import info.sajib.moviesapp.fragments.Tvtop_Rated;
import info.sajib.moviesapp.slidingtab.SlidingTabLayout;

/**
 * Created by sajib on 09-04-2016.
 */
public class Tvshow extends AppCompatActivity {
    private ViewPager viewpager;
    private SlidingTabLayout slidingTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tvshow);

        viewpager= (ViewPager) findViewById(R.id.tv_show_viewpager);
        slidingTabLayout= (SlidingTabLayout) findViewById(R.id.tv_show_slidingtab);

        viewpager.setAdapter(new tvfragmentadapter(getSupportFragmentManager()));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        slidingTabLayout.setViewPager(viewpager);
    }

    class tvfragmentadapter extends FragmentStatePagerAdapter {
        String tabs[];
        public tvfragmentadapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
            tabs=getResources().getStringArray(R.array.Tvtab);
        }

        @Override
        public Fragment getItem(int position) {
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
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 4;
        }

    }
}
