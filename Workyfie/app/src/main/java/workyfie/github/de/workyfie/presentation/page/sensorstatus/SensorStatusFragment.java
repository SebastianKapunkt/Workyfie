package workyfie.github.de.workyfie.presentation.page.sensorstatus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import workyfie.github.de.workyfie.R;

public class SensorStatusFragment extends Fragment {

    public static SensorStatusFragment newInstance() {
        Bundle args = new Bundle();

        SensorStatusFragment fragment = new SensorStatusFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        presenter = new LearnSessionPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sensor_status_fragment, container, false);

        return rootView;
    }
}
