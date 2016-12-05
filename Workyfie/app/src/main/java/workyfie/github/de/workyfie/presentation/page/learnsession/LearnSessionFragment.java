package workyfie.github.de.workyfie.presentation.page.learnsession;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.data.presentation.session.LearnSessionItem;

public class LearnSessionFragment extends Fragment implements LearnSessionView, View.OnClickListener, Chronometer.OnChronometerTickListener {
    public static final String TAG = LearnSessionFragment.class.getSimpleName();

    private LearnSessionPresenter presenter;
    private Button toggleSession;
    private View activity;
    private View information_area;
    private Chronometer sessionChronometer;
    private Chronometer breakChronometer;
    private AlertDialog alertDialog;

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
        breakChronometer = (Chronometer) rootView.findViewById(R.id.last_break);

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
        if (alertDialog != null) {
            alertDialog.cancel();
            alertDialog = null;
        }
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
        switch (view.getId()) {
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
    public void startSessionChronometer(long sessionStartTime) {
        Log.i(TAG, "" + SystemClock.elapsedRealtime());
        sessionChronometer.setBase(sessionStartTime);
        sessionChronometer.start();
        sessionChronometer.setOnChronometerTickListener(this);
    }

    @Override
    public void stopSessionChronometer() {
        sessionChronometer.stop();
        breakChronometer.stop();
    }

    private void stopLastBreakChronometer() {
        breakChronometer.stop();
    }

    private void startLastBreakChronometer(long lastBreak) {
        breakChronometer.setBase(lastBreak);
        breakChronometer.start();
    }

    @Override
    public void drawState(LearnSessionItem item) {
        switch (item.state) {
            case IN_SESSION:
                showInSession();
                startSessionChronometer(item.sessionStartTime);
                startLastBreakChronometer(item.lastBreak);
                break;
            case OUT_SESSION:
                showNoSession();
                stopSessionChronometer();
                stopLastBreakChronometer();
                break;
        }
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        if ((SystemClock.elapsedRealtime() - chronometer.getBase()) % 10000 < 1000 && SystemClock.elapsedRealtime() - chronometer.getBase() > 0) {
            Log.i(TAG, " NOW ");
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle("Du brauchst eine Pause!")
                    .setMessage("Du solltest jetzt eine kurze Pause machen.")
                    .setPositiveButton("Ja ich mach eine Pause", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            presenter.makeABreak();
                        }
                    })
                    .setNegativeButton("Nein jetzt noch nicht", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                        }
                    })
                    .create();
            alertDialog.show();
        }
    }
}
