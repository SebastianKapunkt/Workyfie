package workyfie.github.de.workyfie.presentation.page.launch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, LaunchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity);
//
//        startActivity(DeviationActivity.createIntent(getApplicationContext(), "1"));
    }
}