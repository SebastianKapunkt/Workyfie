package workyfie.github.de.workyfie.presentation.page.sensor;

import android.app.Activity;
import android.content.Context;

import info.plux.pluxapi.Constants;
import workyfie.github.de.workyfie.presentation.mvp.PresenterView;

/**
 * Created by insanemac on 10.12.16.
 */

public interface SensorView extends PresenterView {

    void connected();

    void err(String msg);

    void showIsRecording();

    void showNoRecording();

    void setStatusSensor (Constants.States state);

    void appendSensorData(String data);
}
