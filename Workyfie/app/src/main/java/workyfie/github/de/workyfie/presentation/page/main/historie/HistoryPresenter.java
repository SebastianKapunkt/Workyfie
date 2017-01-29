package workyfie.github.de.workyfie.presentation.page.main.historie;

import android.util.Log;

import org.threeten.bp.Instant;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;
import workyfie.github.de.workyfie.application.modules.ThreadingModule;
import workyfie.github.de.workyfie.data.repos.session.SessionRepository;
import workyfie.github.de.workyfie.data.view.models.Session;
import workyfie.github.de.workyfie.presentation.mvp.Presenter;


public class HistoryPresenter implements Presenter<HistoryView>,
        Observer<List<Session>> {
    public static final String TAG = HistoryPresenter.class.getSimpleName();

    private final ThreadingModule threadingModule;
    private SessionRepository repository;
    private CompositeSubscription subscription;
    private HistoryView view;

    public HistoryPresenter(ThreadingModule threadingModule, SessionRepository sessionRepository) {
        this.threadingModule = threadingModule;
        repository = sessionRepository;
    }

    @Override
    public void attach(HistoryView view) {
        Log.i(TAG, "attach");
        this.view = view;
    }

    @Override
    public void detach() {
        Log.v(TAG, "detach");
        this.view = null;
        subscription.unsubscribe();
        subscription = null;
    }

    private CompositeSubscription subscription() {
        if (subscription == null) {
            subscription = new CompositeSubscription();
        }
        return subscription;
    }

    public void requestContent() {
        subscription().add(
                repository
                        .list()
                        .subscribeOn(threadingModule.getIOScheduler())
                        .observeOn(threadingModule.getMainScheduler())
                        .subscribe(this)
        );
    }

    public void setAdapterClickHistoryItemObserver(Observable<String> adapterClickHistoryItemObserver) {
        subscription().add(
                adapterClickHistoryItemObserver
                        .subscribeOn(threadingModule.getIOScheduler())
                        .observeOn(threadingModule.getMainScheduler())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onCompleted() {
                                Log.i(TAG, "onCompleted called");
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(String id) {
                                view.openHistoryItem(id);
                            }
                        })
        );
    }

    private void drawView(List<Session> items) {
        view.drawHistory(items);
    }

    @Override
    public void onCompleted() {
        Log.i(TAG, "onCompleted called");
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(List<Session> items) {
        drawView(items);
    }
}
