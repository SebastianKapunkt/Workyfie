package workyfie.github.de.workyfie.presentation.page.learnsession;

import android.os.SystemClock;

import workyfie.github.de.workyfie.data.presentation.session.LearnSessionItem;
import workyfie.github.de.workyfie.presentation.mvp.Presenter;

import static workyfie.github.de.workyfie.data.presentation.session.SessionEnum.IN_SESSION;
import static workyfie.github.de.workyfie.data.presentation.session.SessionEnum.OUT_SESSION;

public class LearnSessionPresenter implements Presenter<LearnSessionView> {

    private LearnSessionView view;
    private LearnSessionItem item;

    public LearnSessionPresenter() {
        item = new LearnSessionItem(OUT_SESSION, SystemClock.elapsedRealtime(), SystemClock.elapsedRealtime());
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
    }

    private void stopSession() {
        item = LearnSessionItem.setState(item, OUT_SESSION);
        drawView(item);
    }

    public void requestContent() {
        drawView(item);
    }

    public void makeABreak() {
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
