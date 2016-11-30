package workyfie.github.de.workyfie.presentation.page.learnsession;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import workyfie.github.de.workyfie.R;

public class LearnSessionFragment extends Fragment implements LearnSessionView, View.OnClickListener{
    public static final String TAG = LearnSessionFragment.class.getSimpleName();

    private LearnSessionPresenter presenter;
    private Button toggleSession;
    private View activity;
    private View information_area;
    private Chronometer sessionChronometer;

    public static LearnSessionFragment newInstance() {
        Bundle args = new Bundle();

        LearnSessionFragment fragment = new LearnSessionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new LearnSessionPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.learn_session_fragment, container, false);

        toggleSession = (Button) rootView.findViewById(R.id.toggle_session);
        activity = rootView.findViewById(R.id.brain_activity);
        information_area = rootView.findViewById(R.id.information_table);
        sessionChronometer = (Chronometer) rootView.findViewById(R.id.session_chronometer);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attach(this);
        presenter.requestContent();
        Log.i(TAG, "attach");
    }

    @Override
    public void onResume() {
        super.onResume();
        toggleSession.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        toggleSession.setOnClickListener(null);
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "detach");
        presenter.detach();
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toggle_session:
                presenter.toggleSession();
                break;
            default:
        }
    }

    @Override
    public void showNoSession() {
        toggleSession.setText(R.string.start_session);
        information_area.setVisibility(View.GONE);
        activity.setVisibility(View.GONE);
    }

    @Override
    public void showInSession() {
        toggleSession.setText(R.string.stop_session);
        information_area.setVisibility(View.VISIBLE);
        activity.setVisibility(View.VISIBLE);
    }

    @Override
    public void startSessionChronometer() {
        Log.i(TAG, "" + SystemClock.elapsedRealtime());
        sessionChronometer.setBase(SystemClock.elapsedRealtime());
        sessionChronometer.start();
    }

    @Override
    public void stopSessionChronometer() {
        sessionChronometer.stop();
    }
}
