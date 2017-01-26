package workyfie.github.de.workyfie;

import android.app.Application;

import io.realm.Realm;
import workyfie.github.de.workyfie.application.ApplicationComponent;
import workyfie.github.de.workyfie.application.bitalino.config.BitalinoConfig;
import workyfie.github.de.workyfie.application.bitalino.reciever.BitalinoBroadcastReceiver;

public class App extends Application {
    public static final String TAG = App.class.getSimpleName();

    private static App instance;
    private static BitalinoBroadcastReceiver bitalinoReceiver;

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

        bitalinoReceiver = new BitalinoBroadcastReceiver(App.getComponent().getBitalinoReceiveHandler(),
                BitalinoConfig.BITALINO_CONFIG_EEG);

        this.registerReceiver(bitalinoReceiver, bitalinoReceiver.getIntentFilter());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        this.unregisterReceiver(bitalinoReceiver);
    }

    public static App getApplication() {
        return instance;
    }
}
