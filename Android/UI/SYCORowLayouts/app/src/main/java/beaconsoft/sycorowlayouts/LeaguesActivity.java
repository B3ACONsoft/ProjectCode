package beaconsoft.sycorowlayouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class LeaguesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static final String EMAIL_KEY = "beaconsoft.sycorowlayours.EMAIL_PASSED";
    private TextView textViewGreeting;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leagues);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(true);

        Intent intent = getIntent();
        email = intent.getStringExtra(EMAIL_KEY);

        textViewGreeting = (TextView)findViewById(R.id.textViewLeaguesActivityPassedName);
        textViewGreeting.setTextSize(40);
        textViewGreeting.setText(email);

        Spinner spinnerLeagues = (Spinner)findViewById(R.id.leagues_spinner);
        ArrayAdapter adapterSpinnerLeagues = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.dummy_leagues, android.R.layout.simple_spinner_dropdown_item);
        adapterSpinnerLeagues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLeagues.setAdapter(adapterSpinnerLeagues);
        spinnerLeagues.setOnItemSelectedListener(this);
        spinnerLeagues.setEnabled(true);
        spinnerLeagues.setSelection(0);

        Spinner spinnerTeams = (Spinner)findViewById(R.id.teams_spinner);
        ArrayAdapter adapterSpinnerTeams = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.dummy_teams, android.R.layout.simple_spinner_dropdown_item);
        adapterSpinnerTeams.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeams.setAdapter(adapterSpinnerTeams);
        spinnerTeams.setOnItemSelectedListener(this);
        spinnerTeams.setEnabled(true);
        spinnerTeams.setSelection(0);

        Spinner spinnerSports = (Spinner)findViewById(R.id.sports_spinner);
        ArrayAdapter adapterSpinnerSports = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.dummy_sports, android.R.layout.simple_spinner_dropdown_item);
        adapterSpinnerSports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSports.setAdapter(adapterSpinnerSports);
        spinnerSports.setOnItemSelectedListener(this);
        spinnerSports.setEnabled(true);
        spinnerSports.setSelection(0);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
        /*This is where the code needs to reach out and grab the teams in each league
        * We should be able to highlight a team and then "Add Coach" or "Add Player"
        * or "Add Event"..."maybe add specific places as well.*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void goToAddPlayerFromLeagues(View view){
        Intent intent = new Intent(getApplicationContext(), AddPlayerActivity.class);
        intent.putExtra(EMAIL_KEY, email);
        startActivity(intent);
    }
}
