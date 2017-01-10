package workyfie.github.de.workyfie;

import android.app.Application;

import io.realm.Realm;
import workyfie.github.de.workyfie.application.ApplicationComponent;

public class App extends Application {
    public static final String TAG = App.class.getSimpleName();

    private static App instance;
    private static String baseUrl = "https://www.deviantart.com";

    private ApplicationComponent applicationComponent;

    public static ApplicationComponent getComponent() {
        if (instance.applicationComponent == null) {
            throw new IllegalStateException("Application must not be NULL");
        }
        return instance.applicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        applicationComponent = new ApplicationComponent(instance);
        Realm.init(this);
    }

    public static App getApplication() {
        return instance;
    }
}
