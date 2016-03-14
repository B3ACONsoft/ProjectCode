package beaconsoft.sycorowlayouts.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import beaconsoft.sycorowlayouts.R;

public class CoachHomeActivity extends AppCompatActivity {

    private static final String COACH_KEY = "beaconsoft.sycorowlayouts.COACH";
    private static final String EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String NAME_KEY  = "beaconsoft.sycorowlayours.NAME";

    private String name;
    private String email;
    private String coachId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_home);

        Intent intent = getIntent();
        name    = intent.getStringExtra(NAME_KEY);
        email   = intent.getStringExtra(EMAIL_KEY);
        coachId = intent.getStringExtra(COACH_KEY);

        TextView textViewEmail = (TextView)findViewById(R.id.textViewCoachHomeEmail);
        textViewEmail.setText("Coach Email: " + email);
        TextView textViewName  = (TextView)findViewById(R.id.textViewCoachHomeName);
        textViewName.setText(name + " CoachId:" + coachId );

    }
}
