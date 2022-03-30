package com.example.food_product_comparison_android_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ComparisonsVPAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> fragments = new ArrayList<>();
    private final ArrayList<String> titles = new ArrayList<>();

    public ComparisonsVPAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String title)
    {
        this.fragments.add(fragment);
        this.titles.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
