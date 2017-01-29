package workyfie.github.de.workyfie.presentation.page.main.historie.detail;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import java.util.List;

import rx.subjects.PublishSubject;
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
    private EditText name;
    private TextView dateView;
    private TextView durationView;
    private TextView startTime;
    private TextView endTime;
    private View progressBar;
    private final PublishSubject<String> onClickNameField = PublishSubject.create();

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                progressBar.setVisibility(View.VISIBLE);
                onClickNameField.onNext(s.toString());
            }
        }
    };

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

        name = (EditText) rootView.findViewById(R.id.name_field);
        durationView = (TextView) rootView.findViewById(R.id.duration_field);
        dateView = (TextView) rootView.findViewById(R.id.date_field);
        startTime = (TextView) rootView.findViewById(R.id.start_time_field);
        endTime = (TextView) rootView.findViewById(R.id.end_time_field);
        graphView = (GraphView) rootView.findViewById(R.id.graph);
        progressBar = rootView.findViewById(R.id.progressbar);

        graphView.setTitle("EEG Daten");
        graphView.setTitleColor(R.color.primaryTextDark);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(40);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMaxY(1000);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setScrollable(true);

        series = new LineGraphSeries<>();
        series.setColor(getResources().getColor(R.color.colorAccentDark));
        series.setAnimated(true);
        series.setThickness(10);
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
        name.removeTextChangedListener(watcher);
    }

    @Override
    public void onStop() {
        presenter.detach();
        super.onStop();
    }

    @Override
    public void drawContent(Tuple2<Session, List<GraphDataPoint>> items) {
        name.removeTextChangedListener(watcher);
        series.resetData(new GraphDataPoint[]{});
        progressBar.setVisibility(View.INVISIBLE);

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        for (GraphDataPoint point : items.item2) {
            series.appendData(point, true, 100);
        }

        graphView.requestFocus();

        LocalDateTime startDateTime = LocalDateTime.ofInstant(items.item1.startTime, ZoneId.systemDefault());
        LocalDateTime endDateTime = LocalDateTime.ofInstant(items.item1.endTime, ZoneId.systemDefault());

        name.setText(String.format("%s", items.item1.name));
        dateView.setText(String.format(
                "%s %s %s",
                startDateTime.getDayOfMonth(),
                startDateTime.getMonth(),
                startDateTime.getYear())
        );
        durationView.setText(String.format(
                "%s h %s min %s sek",
                Duration.between(items.item1.startTime, items.item1.endTime).toHours(),
                Duration.between(items.item1.startTime, items.item1.endTime).toMinutes(),
                Duration.between(items.item1.startTime, items.item1.endTime).toMillis() / 1000)
        );
        startTime.setText(String.format(
                "%s:%s:%s",
                startDateTime.getHour(),
                startDateTime.getMinute(),
                startDateTime.getSecond()
        ));
        endTime.setText(String.format(
                "%s:%s:%s",
                endDateTime.getHour(),
                endDateTime.getMinute(),
                endDateTime.getSecond()
        ));
        name.addTextChangedListener(watcher);
        presenter.setTextChangeObserver(onClickNameField);
    }


}
