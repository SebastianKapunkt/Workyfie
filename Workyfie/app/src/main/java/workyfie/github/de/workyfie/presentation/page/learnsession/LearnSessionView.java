package workyfie.github.de.workyfie.presentation.page.learnsession;

import workyfie.github.de.workyfie.data.presentation.session.LearnSessionItem;
import workyfie.github.de.workyfie.presentation.mvp.PresenterView;

public interface LearnSessionView extends PresenterView{
    void showNoSession();

    void showInSession();

    void startSessionChronometer(long sessionStartTime);

    void stopSessionChronometer();

    void drawState(LearnSessionItem item);
}
