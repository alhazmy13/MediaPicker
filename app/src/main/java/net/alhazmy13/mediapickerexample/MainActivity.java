package net.alhazmy13.mediapickerexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Fragment videoFragment = new VideoFragment();
    private Fragment imageFragment = new ImageFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager mPager = findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new PickerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);
        for (int i = 0; i < mPagerAdapter.getCount(); i++)
            tabLayout.getTabAt(i).setText(mPagerAdapter.getPageTitle(i));
    }

    private class PickerAdapter extends FragmentStatePagerAdapter {
        private static final int NUM_PAGES = 2;


        PickerAdapter(FragmentManager fm) {
            super(fm);
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
        public String getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_title_video);
                default:
                    return getString(R.string.tab_title_image);
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