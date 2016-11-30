package workyfie.github.de.workyfie.presentation.page.learnsession;

import workyfie.github.de.workyfie.presentation.mvp.PresenterView;

public interface LearnSessionView extends PresenterView{
    void showNoSession();

    void showInSession();

    void startSessionChronometer();

    void stopSessionChronometer();
}
