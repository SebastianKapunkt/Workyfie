package workyfie.github.de.workyfie.presentation.page.main.measure;

import com.jjoe64.graphview.series.DataPoint;

import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.presentation.mvp.PresenterView;

public interface MeasureView extends PresenterView {
    void drawState(IBitalinoState state);

    void addGraphData(DataPoint dataPoint);

    void setGraphData(DataPoint[] dataPoint);

    void errMsg(String msg);
}
