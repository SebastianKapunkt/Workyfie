package workyfie.github.de.workyfie.presentation.page.main;

import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.presentation.page.main.sensor.SensorFragment;
import workyfie.github.de.workyfie.presentation.page.main.sidebar.SidebarFragment;
import workyfie.github.de.workyfie.presentation.page.main.sidebar.SidebarItem;

import static workyfie.github.de.workyfie.presentation.page.main.sidebar.SidebarItem.*;
import static workyfie.github.de.workyfie.presentation.page.main.sidebar.SidebarItem.HISTORY;

public class MainActivity extends AppCompatActivity implements SidebarFragment.Container {
    public static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setUpToolbar();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_sidebar, SidebarFragment.newInstance(), SidebarFragment.TAG)
                    .add(R.id.container_main, SensorFragment.newInstance(), SensorFragment.TAG)
                    .commit();
        }
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
        drawerLayout.closeDrawer(Gravity.LEFT);
        switch (sidebarItem) {
            case HISTORY:

                break;
            case INFORMATION:

                break;
            case SENSOR:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_sidebar, SidebarFragment.newInstance(), SidebarFragment.TAG)
                        .add(R.id.container_main, SensorFragment.newInstance(), SensorFragment.TAG)
                        .commit();
                break;
            case MEASURE:

                break;

        }
    }
}
