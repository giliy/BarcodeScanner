package com.barcodescanner.gili.scan9;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentPageAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Search", "Cart", "Quick Order" };

    public FragmentPageAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int arg0) {
        switch (arg0)
        {
            case 0:
                return Search.newInstance(arg0 + 1);
            case 1:
                return MyCart.newInstance(arg0 + 1);
            case 2:
                return QuickOrder.newInstance((arg0 + 1));
            default:
                break;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }

}