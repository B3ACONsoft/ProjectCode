package beaconsoft.sycorowlayouts.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.EventListAdapter;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.dbobjects.Event;
import beaconsoft.sycorowlayouts.dbobjects.Team;

public class UserHomeActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener {

    private static final String EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private DataSource dataSource = new DataSource(this);
    private ArrayList<Event> arrayListEvents = new ArrayList<>();
    private Event currentEvent;
    private String email = "";
    private ArrayList<Team> arrayListTeams = new ArrayList<>();
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_user_home);

        Intent intent = getIntent();
        email = intent.getStringExtra(EMAIL_KEY);

        CalendarView cal = (CalendarView)findViewById(R.id.calendarViewUserHome);
        cal.setShowWeekNumber(false);
        cal.setOnDateChangeListener(this);

        try {
            dataSource.open();
        } catch (SQLException e) {
            Log.e("DATASOURCE", "Trying to open Datasource\n" + e.getMessage());
        }

        arrayListEvents.clear();
        arrayListTeams.clear();

        List<Team> teamArray = new ArrayList<>();
        teamArray.addAll(dataSource.getListOfTeamsByUser(email));
        for(Team t: teamArray){
            if(!arrayListTeams.contains(t)){
                arrayListTeams.add(t);
            }
        }

        if(arrayListTeams.size() > 0){
            for(Team team: arrayListTeams) {
                List<Event> eventArray = new ArrayList<>();
                eventArray.addAll(dataSource.getListOfEventsByTeam(team));
                for(Event e: eventArray){
                    if(!arrayListEvents.contains(e)){
                        arrayListEvents.add(e);
                    }
                }
            }
        }
        ListAdapter adapter = new EventListAdapter(getApplicationContext(), R.layout.notification_list_item, arrayListEvents, dataSource);
        listview = (ListView) findViewById(R.id.listViewUserHome);
        listview.setAdapter(adapter);

    }

    public void sendToUserRoster(View view){
        Intent intent = new Intent(this, UserRosterActivity.class);
        startActivity(intent);
    }

    String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        Log.e("CALENDAR", "................................DATE TOUCHED: " + year +"/" + month + "/" + dayOfMonth);
        List<Event> singleDayArray = new ArrayList<>();
        for(Event e : arrayListEvents){
            String[]dateThings = e.toString().split(" ");
            String type = dateThings[0];
            String dayOfWeek = dateThings[1];
            String mth = dateThings[2];
            String day = dateThings[3];
            String time = dateThings[4];
            String zone = dateThings[5];
            String yr = dateThings[6];
            if(year == Integer.parseInt(yr) && months[month].equals(mth) && Integer.parseInt(day) == dayOfMonth){
                singleDayArray.add(e);
            }
        }
        ListAdapter adapter = new EventListAdapter(getApplicationContext(), R.layout.notification_list_item, singleDayArray, dataSource);
        listview.clearChoices();
        listview.setAdapter(adapter);
    }


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
    protected void onResume()
    {
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }
}
