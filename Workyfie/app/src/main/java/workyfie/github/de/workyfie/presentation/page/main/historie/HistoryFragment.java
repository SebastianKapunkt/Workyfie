package workyfie.github.de.workyfie.presentation.page.main.historie;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import workyfie.github.de.workyfie.App;
import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.data.view.models.Session;
import workyfie.github.de.workyfie.presentation.common.FragmentUtils;
import workyfie.github.de.workyfie.presentation.page.main.historie.detail.HistoryDetailFragment;

public class HistoryFragment extends Fragment
        implements HistoryView {
    public static final String TAG = HistoryFragment.class.getSimpleName();

    private HistoryPresenter presenter;
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private AlertDialog alert;

    public static HistoryFragment newInstance() {
        Bundle args = new Bundle();

        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new HistoryPresenter(
                App.getComponent().getThreadingModule(),
                App.getComponent().getSessionRepository()
        );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hisotry_fragment, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.history_recycler_view);
        recyclerView.setHasFixedSize(true);
        adapter = new HistoryAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attach(this);
        presenter.requestContent();
    }

    @Override
    public void onPause() {
        alert = null;
        super.onPause();
    }

    @Override
    public void onStop() {
        presenter.detach();
        super.onStop();
    }

    @Override
    public void drawHistory(List<Session> items) {
        adapter = new HistoryAdapter(items);
        recyclerView.setAdapter(adapter);
        presenter.setAdapterClickHistoryItemObserver(adapter.getHistoryClicks());
        presenter.registerDeleteClicks(adapter.getDeleteClicks());
    }

    @Override
    public void openHistoryItem(String id) {
        FragmentUtils.defaultAnimation(
                getFragmentManager()
                        .beginTransaction())
                .replace(R.id.container_main, HistoryDetailFragment.newInstance(id), HistoryDetailFragment.TAG)
                .addToBackStack(HistoryDetailFragment.TAG)
                .commit();
    }

    @Override
    public void showDeleteDialog(String s) {
        alert = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.delete_history)
                .setNegativeButton("Nein", (dialog, which) -> alert.dismiss())
                .setPositiveButton("Ja", (dialog, which) -> {
                    presenter.deleteSession(s);
                    alert.dismiss();
                })
                .create();
        alert.show();
    }
}
