package beaconsoft.sycorowlayouts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CoachHomeActivity extends AppCompatActivity {

    private static final String COACH_KEY = "beaconsoft.sycorowlayouts.COACH";
    private static final String EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_home);

        Intent intent = getIntent();

        TextView textViewEmail = (TextView)findViewById(R.id.textViewCoachHomeEmail);
        textViewEmail.setText("Coach Email: " + intent.getStringExtra(EMAIL_KEY));
    }
}
