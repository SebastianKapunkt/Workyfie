package workyfie.github.de.workyfie.presentation.page.main.sensor;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.data.models.SensorData;
import workyfie.github.de.workyfie.presentation.mvp.PresenterView;

public interface SensorView extends PresenterView {
    void drawState(IBitalinoState state);

    void drawData(SensorData data);

    void errMsg(String msg);

    void addGraphData(DataPoint dataPoint);
}
