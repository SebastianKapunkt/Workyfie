package workyfie.github.de.workyfie.presentation.page.main.information;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import workyfie.github.de.workyfie.R;

public class InformationFragment extends Fragment implements InformationView {
    public static final String TAG = InformationFragment.class.getSimpleName();

    private InformationPresenter presenter;

    public static InformationFragment newInstance() {
        Bundle args = new Bundle();

        InformationFragment fragment = new InformationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new InformationPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.information_fragment, container, false);

        return rootView;
    }
}
