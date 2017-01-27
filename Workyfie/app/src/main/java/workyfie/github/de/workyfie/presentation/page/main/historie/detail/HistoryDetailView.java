package workyfie.github.de.workyfie.presentation.page.main.historie.detail;

import java.util.List;

import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;
import workyfie.github.de.workyfie.data.view.models.Session;
import workyfie.github.de.workyfie.data.view.models.common.Tuple2;
import workyfie.github.de.workyfie.presentation.mvp.PresenterView;

public interface HistoryDetailView extends PresenterView {
    void drawContent(Tuple2<Session, List<GraphDataPoint>> items);
}
