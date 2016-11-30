package workyfie.github.de.workyfie.presentation.page.learnsession;

import data.presentation.session.LearnSessionItem;
import workyfie.github.de.workyfie.presentation.mvp.Presenter;

import static data.presentation.session.SessionEnum.IN_SESSION;
import static data.presentation.session.SessionEnum.OUT_SESSION;

public class LearnSessionPresenter implements Presenter<LearnSessionView> {

    private LearnSessionView view;
    private LearnSessionItem item;

    public LearnSessionPresenter() {
        item = new LearnSessionItem(OUT_SESSION);
    }

    @Override
    public void attach(LearnSessionView view) {
        this.view = view;
    }

    @Override
    public void detach() {
        view = null;
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

    private void startSession() {
        item = LearnSessionItem.setState(item, IN_SESSION);
        view.startSessionChronometer();
        view.showInSession();
    }

    private void stopSession() {
        item = LearnSessionItem.setState(item, OUT_SESSION);
        view.stopSessionChronometer();
        view.showNoSession();
    }

    public void requestContent() {

    }
}
