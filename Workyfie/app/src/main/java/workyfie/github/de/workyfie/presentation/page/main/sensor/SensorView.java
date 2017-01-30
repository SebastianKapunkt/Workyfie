package workyfie.github.de.workyfie.presentation.page.main.sensor;

import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.presentation.mvp.PresenterView;

public interface SensorView extends PresenterView {
    void drawState(IBitalinoState state);

    void drawIsScanning(boolean isScanning);

    void notifyDeviceDataChange();

    void errMsg(String msg);
}
