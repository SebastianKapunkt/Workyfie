package workyfie.github.de.workyfie.presentation.mvp;

public interface Presenter<T extends PresenterView> {

    void attach(T view);

    void detach();
}
