package workyfie.github.de.workyfie.presentation.page.main.historie;

import java.util.List;

import workyfie.github.de.workyfie.data.view.models.Session;
import workyfie.github.de.workyfie.presentation.mvp.PresenterView;

public interface HistoryView extends PresenterView {

    void drawHistory(List<Session> items);

    void openHistoryItem(String id);
}
