package workyfie.github.de.workyfie.presentation.page.mainpage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.application.AnalyticsApplication;
import workyfie.github.de.workyfie.presentation.page.learnsession.LearnSessionFragment;
import workyfie.github.de.workyfie.presentation.page.sensor.SensorFragment;
import workyfie.github.de.workyfie.presentation.page.sensorstatus.SensorStatusFragment;

public class MainPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private Context context;
    private Tracker tracker;

    public MainPagerAdapter(FragmentManager fm, Context context, Tracker tracker) {
        super(fm);
        this.context = context;
        this.tracker = tracker;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return LearnSessionFragment.newInstance();
            case 1:
                return SensorStatusFragment.newInstance();
            case 2:
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
        return 3;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tracker.setScreenName((String) getPageTitle(position));
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
