package workyfie.github.de.workyfie.application.bitalino.reciever;

import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.data.models.SensorData;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;

public interface IBitalinoOnReceiveCallback {
    void onReceiveData(SensorData data);
    void onReceiveState(IBitalinoState state);
}
