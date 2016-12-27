package workyfie.github.de.workyfie.presentation.page.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.presentation.page.main.sensor.SensorFragment;

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
                return SensorFragment.newInstance();
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
                return "information";
            case 2:
                return context.getString(R.string.sensor);
            default:
                return "error";
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}
