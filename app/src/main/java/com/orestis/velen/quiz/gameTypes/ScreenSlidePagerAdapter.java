package com.orestis.velen.quiz.gameTypes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    private ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public static class Builder {

        private List<Fragment> fragments = new ArrayList<>();
        private FragmentManager fragmentManager;

        public Builder useFragmentManager(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
            return this;
        }

        public Builder addFragment(Fragment fragment) {
            this.fragments.add(fragment);
            return this;
        }

        public ScreenSlidePagerAdapter build() {
            ScreenSlidePagerAdapter adapter = new ScreenSlidePagerAdapter(fragmentManager);
            adapter.fragments = this.fragments;
            return adapter;
        }
    }
}
