package workyfie.github.de.workyfie.presentation.page.learnsession;

import android.icu.text.DateFormat;
import android.os.SystemClock;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Date;

import workyfie.github.de.workyfie.application.AnalyticsApplication;
import workyfie.github.de.workyfie.data.presentation.session.LearnSessionItem;
import workyfie.github.de.workyfie.data.presentation.session.SessionEnum;
import workyfie.github.de.workyfie.presentation.mvp.Presenter;

import static workyfie.github.de.workyfie.data.presentation.session.SessionEnum.IN_SESSION;
import static workyfie.github.de.workyfie.data.presentation.session.SessionEnum.OUT_SESSION;
import static workyfie.github.de.workyfie.data.presentation.session.SessionEnum.BREAK;

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
                .setAction("Started at")
                .setLabel(new Date(System.currentTimeMillis()).toString())
                .setValue(1)
                .build());
    }

    private void stopSession() {
        item = LearnSessionItem.reset();
        drawView(item);
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Session")
                .setAction("Stopped at")
                .setLabel(new Date(System.currentTimeMillis()).toString())
                .build());
    }

    public void requestContent() {
        drawView(item);
    }

    public void makeABreak() {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Session")
                .setAction("Break after")
                .setLabel(AnalyticsApplication.formatInterval(SystemClock.elapsedRealtime() - item.lastBreak))
                .build());
        item = LearnSessionItem.setState(item, BREAK);
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
            case BREAK:
                continueSession();
                break;
            case UNKNOWN:
                item = LearnSessionItem.reset();
        }
    }

    private void continueSession() {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Session")
                .setAction("Continue after")
                .setLabel(AnalyticsApplication.formatInterval(SystemClock.elapsedRealtime() - item.lastBreak))
                .build());
        item = LearnSessionItem.setLastBreak(item, SystemClock.elapsedRealtime());
        item = LearnSessionItem.setState(item, IN_SESSION);
        drawView(item);
    }

    public SessionEnum getStatus() {
        return item.state;
    }
}
