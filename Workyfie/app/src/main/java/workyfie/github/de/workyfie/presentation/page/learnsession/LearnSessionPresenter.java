package workyfie.github.de.workyfie.presentation.page.learnsession;

import android.os.SystemClock;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import workyfie.github.de.workyfie.application.AnalyticsApplication;
import workyfie.github.de.workyfie.data.presentation.session.LearnSessionItem;
import workyfie.github.de.workyfie.presentation.mvp.Presenter;

import static workyfie.github.de.workyfie.data.presentation.session.SessionEnum.IN_SESSION;
import static workyfie.github.de.workyfie.data.presentation.session.SessionEnum.OUT_SESSION;

public class LearnSessionPresenter implements Presenter<LearnSessionView> {

    private LearnSessionView view;
    private LearnSessionItem item;
    private Tracker tracker;

    public LearnSessionPresenter(Tracker tracker) {
        item = new LearnSessionItem(OUT_SESSION, SystemClock.elapsedRealtime(), SystemClock.elapsedRealtime());
        this.tracker = tracker;
    }

    @Override
    public void attach(LearnSessionView view) {
        this.view = view;
    }

    @Override
    public void detach() {
        view = null;
    }

    private void drawView(LearnSessionItem item) {
        view.drawState(item);
    }

    private void startSession() {
        item = LearnSessionItem.setState(item, IN_SESSION);
        item = LearnSessionItem.setSessionStartTime(item, SystemClock.elapsedRealtime());
        item = LearnSessionItem.setLastBreak(item, SystemClock.elapsedRealtime());
        drawView(item);
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Session")
                .setAction("Started")
                .setLabel(AnalyticsApplication.formatInterval(item.sessionStartTime))
                .setValue(1)
                .build());
    }

    private void stopSession() {
        item = LearnSessionItem.setState(item, OUT_SESSION);
        drawView(item);
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Session")
                .setAction("Stopped")
                .setLabel(AnalyticsApplication.formatInterval(SystemClock.elapsedRealtime() - item.sessionStartTime))
                .build());
    }

    public void requestContent() {
        drawView(item);
    }

    public void makeABreak() {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Session")
                .setAction("Break")
                .setLabel(AnalyticsApplication.formatInterval(SystemClock.elapsedRealtime() - item.lastBreak))
                .build());
        item = LearnSessionItem.setLastBreak(item, SystemClock.elapsedRealtime());
        drawView(item);
    }

    public void toggleSession() {
        switch (item.state) {
            case IN_SESSION:
                stopSession();
                break;
            case OUT_SESSION:
                startSession();
                break;
        }
    }
}
