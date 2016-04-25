package beaconsoft.sycorowlayouts.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.EventListAdapter;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService;
import beaconsoft.sycorowlayouts.activities.UserRosterActivity;
import beaconsoft.sycorowlayouts.dbobjects.Event;
import beaconsoft.sycorowlayouts.dbobjects.Team;

public class UserHomeActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener,
    Spinner.OnItemSelectedListener {

    private static final String EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";
    private static final int INTENT_REQUEST_CODE_USER_ROSTER = 1;

    private ArrayList<Event> arrayListEvents = new ArrayList<>();
    private Event currentEvent;
    private String email = "";
    private ArrayList<Team> arrayListTeams = new ArrayList<>();
    private ListView listview;
    private Spinner spinnerTeams;
    private Team currentTeam;
    private int currentTeamId;
    UpdateService updateService;        //reference to the update service
    boolean mBound = false;             //to bind or not to bind...


    /**
     *
     * Defines callbacks for service binding, passed to bindService()
     *
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService.UpdateServiceBinder binder = (beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService.UpdateServiceBinder) service;

            updateService = binder.getService();

            mBound = true;

            List<Team> teamArray = new ArrayList<>();
            teamArray.addAll(updateService.getListOfTeamsByUser(email));
            for (Team t : teamArray) {
                if (!arrayListTeams.contains(t)) {
                    arrayListTeams.add(t);
                }
            }
            Team all = new Team();
            all.setTeamName("ALL");
            all.setTeamID(0);
            arrayListTeams.add(all);

            spinnerTeams = (Spinner) findViewById(R.id.spinnerUserHomeTeams);
            spinnerTeams.setOnItemSelectedListener(UserHomeActivity.this);
            setSpinnerAdapter();

            setListAdapter();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this,
                UpdateService.class), mConnection, Context.BIND_AUTO_CREATE);






    }

    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_user_home);

        Intent intent = getIntent();
        email = intent.getStringExtra(EMAIL_KEY);

        CalendarView cal = (CalendarView) findViewById(R.id.calendarViewUserHome);
        cal.setShowWeekNumber(false);
        cal.setOnDateChangeListener(this);


        arrayListEvents.clear();
        arrayListTeams.clear();



    }

    private void onSpinnerTeamsChange() {
        arrayListEvents.clear();
        currentTeam = (Team) spinnerTeams.getSelectedItem();
        if (currentTeam != null) {
            currentTeamId = currentTeam.getTeamID();
            if (currentTeamId == 0 && arrayListTeams.size() > 1) {
                for (Team team : arrayListTeams) {
                    if (team.getTeamID() != 0) {
                        List<Event> eventArray = new ArrayList<>();
                        eventArray.addAll(updateService.getListOfEventsByTeam(team));
                        for (Event e : eventArray) {
                            if (!arrayListEvents.contains(e)) {
                                arrayListEvents.add(e);
                            }
                        }

                    }
                }

            } else if (arrayListTeams.size() > 1) {
                List<Event> eventArray = new ArrayList<>();
                eventArray.addAll(updateService.getListOfEventsByTeam(currentTeam));
                for (Event e : eventArray) {
                    if (!arrayListEvents.contains(e)) {
                        arrayListEvents.add(e);
                    }
                }

            }

        }
    }

    private void setSpinnerAdapter() {
        ArrayAdapter adapterSpinnerTeams = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                arrayListTeams);
        spinnerTeams.setAdapter(adapterSpinnerTeams);
    }

    private void setListAdapter() {
        ListAdapter adapter = new EventListAdapter(getApplicationContext(), R.layout.notification_list_item, arrayListEvents, updateService);
        listview = (ListView) findViewById(R.id.listViewUserHome);
        listview.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    public void goToRoster(View view) {
        Team tempTeam = (Team) spinnerTeams.getSelectedItem();
        if (tempTeam.getTeamName().equals("ALL")) {
            Toast toast = Toast.makeText(this, "You must choose a team...", Toast.LENGTH_LONG);
            toast.show();
        } else {

            Intent intent = new Intent(this, UserRosterActivity.class);
            intent.putExtra(EMAIL_KEY, email);
            intent.putExtra(TEAM_KEY, tempTeam.getTeamID());
            startActivityForResult(intent, INTENT_REQUEST_CODE_USER_ROSTER);
        }
    }

    String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        Log.e("CALENDAR", "................................DATE TOUCHED: " + year + "/" + month + "/" + dayOfMonth);
        List<Event> singleDayArray = new ArrayList<>();
        for (Event e : arrayListEvents) {
            String[] dateThings = e.toString().split(" ");
            String mth = dateThings[2];
            String day = dateThings[3];
            String yr = dateThings[6];
            if (year == Integer.parseInt(yr) && months[month].equals(mth) && Integer.parseInt(day) == dayOfMonth) {
                singleDayArray.add(e);
            }
        }
        ListAdapter adapter = new EventListAdapter(getApplicationContext(), R.layout.notification_list_item, singleDayArray, updateService);
        listview.clearChoices();
        listview.setAdapter(adapter);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spinnerTeams) {
            onSpinnerTeamsChange();
            setListAdapter();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == INTENT_REQUEST_CODE_USER_ROSTER && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e("REQUEST CODE", "................................................REQUEST CODE " + requestCode);

                Team passedTeam = updateService.getTeamById(data.getIntExtra(TEAM_KEY, 0));
                int passedTeamId = currentTeam.getTeamID();

                email = data.getStringExtra(EMAIL_KEY);
                CalendarView cal = (CalendarView) findViewById(R.id.calendarViewUserHome);
                cal.setShowWeekNumber(false);
                cal.setOnDateChangeListener(this);

                arrayListEvents.clear();
                arrayListTeams.clear();

                List<Team> teamArray = new ArrayList<>();
                teamArray.addAll(updateService.getListOfTeamsByUser(email));
                for (Team t : teamArray) {
                    if (!arrayListTeams.contains(t)) {
                        arrayListTeams.add(t);
                    }
                }
                Team all = new Team();
                all.setTeamName("ALL");
                all.setTeamID(0);
                arrayListTeams.add(all);

                spinnerTeams = (Spinner) findViewById(R.id.spinnerUserHomeTeams);
                spinnerTeams.setOnItemSelectedListener(this);
                setSpinnerAdapter();
                int findId = 0;
                for(int i = 0; i < arrayListTeams.size(); i++){
                    if(arrayListTeams.get(i).getTeamID() == passedTeamId){
                        findId = i;
                        break;
                    }
                }
                spinnerTeams.setSelection(findId);
                setListAdapter();
            }
        }
    }
}