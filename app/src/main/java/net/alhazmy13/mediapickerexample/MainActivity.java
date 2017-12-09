package net.alhazmy13.mediapickerexample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private Fragment videoFragment;
    private Fragment imageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PickerAdapter adapter = new PickerAdapter(getFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < adapter.getCount(); i++)
            tabLayout.getTabAt(i).setText(adapter.getTitle(i));
    }

    private class PickerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 2;


        PickerAdapter(FragmentManager fm) {
            super(fm);
            videoFragment = new VideoFragment();
            imageFragment = new ImageFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @VisibleForTesting
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return videoFragment;
                default:
                    return imageFragment;
            }
        }

        @VisibleForTesting
        int getTitle(int position) {
            switch (position) {
                case 0:
                    return R.string.tab_title_video;
                default:
                    return R.string.tab_title_image;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //
        videoFragment.onActivityResult(requestCode, resultCode, data);
        imageFragment.onActivityResult(requestCode, resultCode, data);
    }
}