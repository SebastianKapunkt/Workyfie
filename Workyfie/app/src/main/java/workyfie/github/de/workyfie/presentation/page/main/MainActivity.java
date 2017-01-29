package workyfie.github.de.workyfie.presentation.page.main;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import workyfie.github.de.workyfie.App;
import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.application.bitalino.reciever.BitalinoReceiveHandler;
import workyfie.github.de.workyfie.application.bitalino.reciever.IBitalinoReceiverStateCallback;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateConnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateConnecting;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateDisconnected;
import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.presentation.common.FragmentUtils;
import workyfie.github.de.workyfie.presentation.page.main.historie.HistoryFragment;
import workyfie.github.de.workyfie.presentation.page.main.historie.detail.HistoryDetailFragment;
import workyfie.github.de.workyfie.presentation.page.main.information.InformationFragment;
import workyfie.github.de.workyfie.presentation.page.main.measure.MeasureFragment;
import workyfie.github.de.workyfie.presentation.page.main.sensor.SensorFragment;
import workyfie.github.de.workyfie.presentation.page.main.sidebar.SelectedCallback;
import workyfie.github.de.workyfie.presentation.page.main.sidebar.SidebarFragment;
import workyfie.github.de.workyfie.presentation.page.main.sidebar.SidebarItem;

public class MainActivity extends AppCompatActivity implements SidebarFragment.Container {
    public static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private BitalinoReceiveHandler bitalinoReceiveHandler;
    private IBitalinoReceiverStateCallback stateChangeCallback;
    private SelectedCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        bitalinoReceiveHandler = App.getComponent().getBitalinoReceiveHandler();
        stateChangeCallback = state -> onSensorStateChange(state);

        onSensorStateChange(App.getComponent().getBitalinoProxy().getBitalinoState());
        setUpToolbar();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container_sidebar, SidebarFragment.newInstance(), SidebarFragment.TAG)
                    .add(R.id.container_main, MeasureFragment.newInstance(), MeasureFragment.TAG)
                    .commit();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        bitalinoReceiveHandler.subscribeState(stateChangeCallback);
    }

    @Override
    protected void onPause() {
        bitalinoReceiveHandler.unsubscribeState(stateChangeCallback);
        super.onPause();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);
//        drawerToggle.setDrawerArrowDrawable(getResources().getDrawable(R.drawable.bluetooth_transfer));
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    public void onSidebarItemClicked(SidebarItem sidebarItem) {
        switch (sidebarItem) {
            case HISTORY:
                FragmentUtils.defaultAnimation(
                        getFragmentManager()
                                .beginTransaction())
                        .replace(R.id.container_main, HistoryFragment.newInstance(), HistoryFragment.TAG)
                        .addToBackStack(HistoryFragment.TAG)
                        .commit();
                break;
            case INFORMATION:
                FragmentUtils.defaultAnimation(
                        getFragmentManager()
                                .beginTransaction())
                        .replace(R.id.container_main, InformationFragment.newInstance(), InformationFragment.TAG)
                        .addToBackStack(InformationFragment.TAG)
                        .commit();
                break;
            case SENSOR:
                FragmentUtils.defaultAnimation(
                        getFragmentManager()
                                .beginTransaction())
                        .replace(R.id.container_main, SensorFragment.newInstance(), SensorFragment.TAG)
                        .addToBackStack(SensorFragment.TAG)
                        .commit();
                break;
            case MEASURE:
                FragmentUtils.defaultAnimation(
                        getFragmentManager()
                                .beginTransaction())
                        .replace(R.id.container_main, MeasureFragment.newInstance(), MeasureFragment.TAG)
                        .addToBackStack(MeasureFragment.TAG)
                        .commit();
                break;

        }
        setTitle(getResources().getString(sidebarItem.resTitle));
        callback.setSelectedSidebarItem(sidebarItem);
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        SidebarItem item;
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.container_main);
        if (currentFragment instanceof MeasureFragment) {
            item = SidebarItem.MEASURE;
        } else if (currentFragment instanceof SensorFragment) {
            item = SidebarItem.SENSOR;
        } else if (currentFragment instanceof InformationFragment) {
            item = SidebarItem.INFORMATION;
        } else if (currentFragment instanceof HistoryFragment) {
            item = SidebarItem.HISTORY;
        } else if (currentFragment instanceof HistoryDetailFragment) {
            item = SidebarItem.HISTORY;
        } else{
            Log.e(TAG, "SidbarItem to Fragment not Found");
            return;
        }
        setTitle(getResources().getString(item.resTitle));
        callback.setSelectedSidebarItem(item);
    }

    public void onSensorStateChange(IBitalinoState state) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView image = (ImageView) toolbar.findViewById(R.id.toolbar_image);
        if (state instanceof BitalinoStateConnected) {
            image.setImageDrawable(getDrawable(R.drawable.bluetooth_transfer));
        } else if (state instanceof BitalinoStateDisconnected) {
            image.setImageDrawable(getDrawable(R.drawable.bluetooth_off));
        } else if (state instanceof BitalinoStateConnecting) {
            image.setImageDrawable(getDrawable(R.drawable.bluetooth_connect));
        }
    }

    public void setSelectedCallback(SelectedCallback callback) {
        this.callback = callback;
    }
}
