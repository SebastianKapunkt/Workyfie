package workyfie.github.de.workyfie.presentation.page.sensor;

import android.app.Activity;
import android.content.Context;

import info.plux.pluxapi.Constants;
import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.data.models.SensorData;
import workyfie.github.de.workyfie.presentation.mvp.PresenterView;

/**
 * Created by insanemac on 10.12.16.
 */

public interface SensorView extends PresenterView {
    void drawState(IBitalinoState state);

    void drawData(SensorData data);

    void registerRecieverView();

    void registerRecieverCalcData();

    void unregisterReceiverView();

    void unregisterReceiverCalcData();

    void errMsg(String msg);
}
