package workyfie.github.de.workyfie.presentation.page.mainpage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.presentation.page.learnsession.LearnSessionFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return LearnSessionFragment.newInstance();
            case 1:
                return MainActivity.PlaceholderFragment.newInstance(position);
            case 2:
                return MainActivity.PlaceholderFragment.newInstance(position);
            default:
                return MainActivity.PlaceholderFragment.newInstance(position);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.session);
            case 1:
                return "settings";
            case 2:
                return "information";
            default:
                return "error";
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
