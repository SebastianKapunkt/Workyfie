package workyfie.github.de.workyfie.presentation.page.mainpage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.google.android.gms.analytics.HitBuilders;

import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.application.AnalyticsApplication;
import workyfie.github.de.workyfie.presentation.page.learnsession.LearnSessionFragment;
import workyfie.github.de.workyfie.presentation.page.sensor.SensorFragment;

public class MainPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{

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
                return SensorFragment.newInstance();
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
                return context.getString(R.string.sensor);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        AnalyticsApplication.mTracker.setScreenName(String.valueOf(getPageTitle(position)));
        AnalyticsApplication.mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
