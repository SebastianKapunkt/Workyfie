package workyfie.github.de.workyfie.presentation.page.main.information;

import workyfie.github.de.workyfie.presentation.mvp.Presenter;

public class InformationPresenter implements Presenter<InformationView> {
    private InformationView view;

    public InformationPresenter() {
    }

    @Override
    public void attach(InformationView view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }
}
