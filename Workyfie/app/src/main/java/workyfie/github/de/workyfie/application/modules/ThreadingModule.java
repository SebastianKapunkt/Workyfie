package workyfie.github.de.workyfie.application.modules;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ThreadingModule {

    private Scheduler mainScheduler;
    private Scheduler ioScheduler;

    public Scheduler getMainScheduler() {
        if (mainScheduler == null) {
            mainScheduler = provideMainScheduler();
        }

        return mainScheduler;
    }

    private Scheduler provideMainScheduler() {
        return AndroidSchedulers.mainThread();
    }

    public Scheduler getIOScheduler() {
        if (ioScheduler == null) {
            ioScheduler = provideIOScheduler();
        }

        return ioScheduler;
    }

    private Scheduler provideIOScheduler() {
        return Schedulers.io();
    }
}
