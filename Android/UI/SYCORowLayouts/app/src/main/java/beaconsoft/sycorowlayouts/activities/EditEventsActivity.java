package beaconsoft.sycorowlayouts.activities;

import android.app.*;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.*;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.EventListAdapter;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.dbobjects.Attendance;
import beaconsoft.sycorowlayouts.dbobjects.Event;
import beaconsoft.sycorowlayouts.dbobjects.League;
import beaconsoft.sycorowlayouts.dbobjects.Place;
import beaconsoft.sycorowlayouts.dbobjects.Sport;
import beaconsoft.sycorowlayouts.dbobjects.Team;

public class EditEventsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int currentAdminId;
    private int currentLeagueId;
    private int currentTeamId;
    private String currentAdminEmail;
    private String currentAdminName;
    private TextView testPlace;
    private Team currentHomeTeam;
    private Team currentAwayTeam;
    private Place currentPlace;
    private Event currentEvent;
    private static final String   NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private static final String  ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String   TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private DataSource dataSource;
    private ArrayList<Team>  arrayListHomeTeamCandidates  = new ArrayList<>();
    private ArrayList<Team>  arrayListAwayTeamCandidates  = new ArrayList<>();
    private ArrayList<Place> arrayListPlaces              = new ArrayList<>();
    private ArrayList<Event> eventList                    = new ArrayList<>();
    private CheckBox isGame;
    private Spinner spinnerHomeTeam;
    private Spinner spinnerAwayTeam;
    private Spinner spinnerPlaces;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private SpinnerAdapter spinnerAdapterHomeTeam;
    private SpinnerAdapter spinnerAdapterAwayTeam;
    private ArrayList<Team> teamsList = new ArrayList<>();
    private ListView listview;
    private String eventType = "PRACTICE";

    @Override
    protected void onPause(){
        dataSource.close();
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        dataSource.close();
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
        initializePlaces();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_events);

        dataSource = new DataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        currentAdminEmail = intent.getStringExtra(EMAIL_KEY);
        currentAdminName  = intent.getStringExtra(NAME_KEY);
        currentAdminId    = intent.getIntExtra(ADMIN_KEY, 0);
        currentLeagueId   = intent.getIntExtra(LEAGUE_KEY, 0);
        currentTeamId     = intent.getIntExtra(TEAM_KEY, 0);

        testPlace = (TextView)findViewById(R.id.textViewEditEventsPlace);
        isGame = (CheckBox)findViewById(R.id.checkBoxIsGame);
        isGame.setChecked(false);
        spinnerHomeTeam = (Spinner)findViewById(R.id.spinnerHomeTeam);
        spinnerAwayTeam = (Spinner)findViewById(R.id.spinnerAwayTeam);
        spinnerAwayTeam.setEnabled(false);
        spinnerPlaces   = (Spinner)findViewById(R.id.spinnerPlaces);
        spinnerHomeTeam.setOnItemSelectedListener(EditEventsActivity.this);
        spinnerAwayTeam.setOnItemSelectedListener(EditEventsActivity.this);
        spinnerPlaces  .setOnItemSelectedListener(EditEventsActivity.this);
        isGame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isGame.isChecked()) {
                    spinnerAwayTeam.setEnabled(true);
                    eventType = "GAME";
                } else if (!isGame.isChecked()) {
                    spinnerAwayTeam.setEnabled(false);
                    eventType = "PRACTICE";
                }
            }
        });
        initializeTeams();
        initializePlaces();
        initializeListView(currentHomeTeam);
        spinnerHomeTeam.isInEditMode();
        spinnerAwayTeam.isInEditMode();
        spinnerPlaces.isInEditMode();
    }

    private void initializeListView(Team team) {
        eventList.clear();
        eventList.addAll(dataSource.getListOfEventsByHomeTeam(team));
        ListAdapter adapter = new EventListAdapter(getApplicationContext(), R.layout.notification_list_item, eventList, dataSource);
        listview = (ListView) findViewById(R.id.listViewLeagueEvents);
        listview.setAdapter(adapter);
    }

    private void initializePlaces() {
        arrayListPlaces.clear();
        arrayListPlaces.addAll(dataSource.getListOfPlaces());
        spinnerPlaces.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                arrayListPlaces));

    }

    private void initializeTeams() {
        teamsList.clear();
        teamsList.addAll(dataSource.getListOfTeamsByLeague(currentLeagueId));
        arrayListHomeTeamCandidates.clear();
        arrayListAwayTeamCandidates.clear();
        arrayListHomeTeamCandidates.addAll(teamsList);
        arrayListAwayTeamCandidates.addAll(teamsList);
        spinnerAdapterHomeTeam = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arrayListHomeTeamCandidates);
        spinnerHomeTeam.setAdapter(spinnerAdapterHomeTeam);
        currentHomeTeam = (Team)spinnerHomeTeam.getSelectedItem();
        spinnerAdapterAwayTeam = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arrayListAwayTeamCandidates);
        spinnerAwayTeam.setAdapter(spinnerAdapterAwayTeam);
        currentAwayTeam = (Team)spinnerAwayTeam.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(parent.getCount() - 1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);

        if (parent == spinnerHomeTeam) {
            onSpinnerHomeChange();
        }
        if (parent == spinnerAwayTeam) {
            onSpinnerAwayChange();
        }
        if (parent == spinnerPlaces)   {
            onSpinnerPlacesChange();
        }
    }

    private void onSpinnerPlacesChange() {
        currentPlace = (Place)spinnerPlaces.getSelectedItem();
    }

    private void onSpinnerAwayChange() {
        currentAwayTeam = (Team)spinnerAwayTeam.getSelectedItem();
        testPlace.setText("AwayTeam: " + currentAwayTeam);
        initializeListView(currentAwayTeam);
    }

    private void onSpinnerHomeChange() {
        currentHomeTeam = (Team)spinnerHomeTeam.getSelectedItem();
        testPlace.setText("HomeTeam: " + currentHomeTeam);
        initializeListView(currentHomeTeam);
    }

    public void goToAddPlace(View view){
        Intent intent = new Intent(getApplicationContext(), AddPlaceActivity.class);
        startActivity(intent);
    }

    public void forwardDatePicker(View view){
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    public void forwardTimePicker(View view){
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getFragmentManager(), "timePicker");
    }

    public void addEventToLeagueEventList(View view) {

        Date date = new Date();
        Event event = new Event();

        if (eventType.equals("GAME") && currentHomeTeam != currentAwayTeam) {
            event = dataSource.createEvent(eventType, date, currentPlace.getPlaceID(),
                    currentHomeTeam.getTeamID(), currentAwayTeam.getTeamID());
        } else if (eventType.equals("PRACTICE")) {
            event = dataSource.createEvent(eventType, date, currentPlace.getPlaceID(),
                    currentHomeTeam.getTeamID(), currentAwayTeam.getTeamID());
        }else{
            Toast toast = Toast.makeText(this, "A team cannot play itself. Please uncheck the 'game' box for a practice!",
                    Toast.LENGTH_LONG);
            toast.show();
        }
        initializeListView(event.getEventID());
    }

    private void initializeListView(int eventId) {
        eventList.clear();
        eventList.addAll(dataSource.getListOfEventsById(eventId));
        ListAdapter adapter = new EventListAdapter(getApplicationContext(), R.layout.notification_list_item, eventList, dataSource);
        listview = (ListView) findViewById(R.id.listViewLeagueEvents);
        listview.setAdapter(adapter);
    }
}
