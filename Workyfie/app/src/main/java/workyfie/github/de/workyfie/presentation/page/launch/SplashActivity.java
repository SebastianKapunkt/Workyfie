package workyfie.github.de.workyfie.presentation.page.launch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(LaunchActivity.createIntent(getApplicationContext()));

        finish();
    }
}
