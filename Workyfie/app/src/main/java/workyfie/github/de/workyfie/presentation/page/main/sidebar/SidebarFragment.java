package workyfie.github.de.workyfie.presentation.page.main.sidebar;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.presentation.page.main.MainActivity;

public class SidebarFragment extends Fragment implements SidebarAdapter.Callback, SelectedCallback {
    public static final String TAG = SidebarFragment.class.getSimpleName();
    private static final String SAVED_STATE_SELECTED_MAIN_ITEM = "SAVED_STATE_SELECTED_MAIN_ITEM";

    public static final List<SidebarItem> SIDEBAR_ITEMS = Arrays.asList(
            SidebarItem.MEASURE,
            SidebarItem.HISTORY,
            SidebarItem.SENSOR,
            SidebarItem.INFORMATION
    );

    private SidebarAdapter adapter;
    private RecyclerView recyclerView;

    public static Fragment newInstance() {
        return new SidebarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_sidebar, container, false);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        SidebarItem selectedItem;

        if (savedInstanceState == null) {
            selectedItem = SidebarItem.MEASURE;
        } else {
            selectedItem = SidebarItem.values()[savedInstanceState.getInt(SAVED_STATE_SELECTED_MAIN_ITEM, 0)];
        }

        adapter = new SidebarAdapter(SIDEBAR_ITEMS, selectedItem);

        recyclerView.setAdapter(adapter);

        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setCallback(this);
        ((MainActivity) getActivity()).setSelectedCallback(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_STATE_SELECTED_MAIN_ITEM, adapter.getSelectedItem().ordinal());
    }

    @Override
    public void onPause() {
        adapter.setCallback(null);
        super.onPause();
    }

    @Override
    public void onItemClicked(SidebarItem sidebarItem) {
        getContainer().onSidebarItemClicked(sidebarItem);
    }

    private Container getContainer() {
        return (Container) getActivity();
    }

    @Override
    public void setSelectedSidebarItem(SidebarItem sidebarItem) {
        adapter.setSelectedItem(sidebarItem);
    }

    public interface Container {
        void onSidebarItemClicked(SidebarItem sidebarItem);
    }
}
