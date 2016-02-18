package beaconsoft.sycorowlayouts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.HashMap;

public class QuickAddTeamsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerCoaches;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add_teams);

        spinnerCoaches = (Spinner)findViewById(R.id.spinnerCoachesQuickAddTeams);
        ArrayAdapter adapterSpinnerCoaches = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.dummy_coaches, android.R.layout.simple_spinner_dropdown_item);
        adapterSpinnerCoaches.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCoaches.setAdapter(adapterSpinnerCoaches);
        spinnerCoaches.setOnItemSelectedListener(this);
        spinnerCoaches.setEnabled(true);
        spinnerCoaches.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void createTeam(View view){
        String coach = this.spinnerCoaches.getSelectedItem().toString();
        EditText editTeamName = (EditText)findViewById(R.id.editTextNewTeamName);
        String teamName = editTeamName.getText().toString();
        HashMap<String, String> hashMapNewTeam = new HashMap<>();
        hashMapNewTeam.put("keyTeamName", teamName);
        hashMapNewTeam.put("keyCoach", coach);

        //insert into TEAM table
        //test shot changes teamName box to coach name
        editTeamName.setText(coach);
    }
}
