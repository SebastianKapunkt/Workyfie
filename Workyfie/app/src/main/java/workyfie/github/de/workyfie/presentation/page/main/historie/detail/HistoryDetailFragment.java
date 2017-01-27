package workyfie.github.de.workyfie.presentation.page.main.historie.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.List;
import java.util.Locale;

import workyfie.github.de.workyfie.App;
import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;
import workyfie.github.de.workyfie.data.view.models.Session;
import workyfie.github.de.workyfie.data.view.models.common.Tuple2;

public class HistoryDetailFragment extends Fragment implements HistoryDetailView {
    public static final String TAG = HistoryDetailFragment.class.getSimpleName();
    private static final String ARGS_HISTORY_DETAIL_ID = "ARGS_HISTORY_DETAIL_ID";

    private HistoryDetailPresenter presenter;
    private GraphView graphView;
    private LineGraphSeries<GraphDataPoint> series;
    private TextView name;
    private TextView id;
    private TextView startTime;
    private TextView endTime;

    public static HistoryDetailFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString(ARGS_HISTORY_DETAIL_ID, id);

        HistoryDetailFragment fragment = new HistoryDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new HistoryDetailPresenter(
                App.getComponent().getThreadingModule(),
                App.getComponent().getGraphDataPointRepository(),
                App.getComponent().getSessionRepository(),
                getArguments().getString(ARGS_HISTORY_DETAIL_ID)
        );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hisotry_detail_fragment, container, false);

        id = (TextView) rootView.findViewById(R.id.id_field);
        name = (TextView) rootView.findViewById(R.id.name_field);
        startTime = (TextView) rootView.findViewById(R.id.start_time_field);
        endTime = (TextView) rootView.findViewById(R.id.end_time_field);
        graphView = (GraphView) rootView.findViewById(R.id.graph);

        graphView.setTitle("EEG Daten");
        graphView.setTitleColor(R.color.colorPrimary);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(40);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMaxY(1000);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setScrollable(true);

        series = new LineGraphSeries<>();
        graphView.addSeries(series);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attach(this);
        presenter.requestContent();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        presenter.detach();
        super.onStop();
    }

    @Override
    public void drawContent(Tuple2<Session, List<GraphDataPoint>> items) {
        series.resetData(new GraphDataPoint[]{});
        for (GraphDataPoint point : items.item2) {
            series.appendData(point, true, 100);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.UK).withZone(ZoneOffset.UTC);

        id.setText(String.format("id: %s", items.item1.id));
        name.setText(String.format("name: %s", items.item1.name));
        startTime.setText(String.format("startTime: %s", formatter.format(items.item1.startTime)));
        endTime.setText(String.format("endTime: %s", formatter.format(items.item1.endTime)));
    }
}
