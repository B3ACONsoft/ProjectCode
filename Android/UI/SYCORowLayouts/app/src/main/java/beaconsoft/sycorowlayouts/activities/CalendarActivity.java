package beaconsoft.sycorowlayouts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.database.Cursor;
import android.view.Menu;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.dbobjects.Event;

public class CalendarActivity extends AppCompatActivity {

    //I am writing something in the Calendar Activity

    private TextView eventTextPrompt;
    private int currentAdminId;
    private int currentLeagueId;
    private int currentTeamId;
    private String currentAdminEmail;
    private String currentAdminName;
    private static final String   NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private static final String  ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String   TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private List<Event> eventList = new ArrayList<Event>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
    GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
    private DataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        datasource = new DataSource(this);
        try {
            datasource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

       /*Okay Jolichelle, I've got your intent extras passing in the current team and league, and admin id */
        Intent intent = getIntent();
        currentAdminEmail = intent.getStringExtra(EMAIL_KEY);
        currentAdminName  = intent.getStringExtra(NAME_KEY);
        currentAdminId    = intent.getIntExtra(ADMIN_KEY, 0);
        currentLeagueId   = intent.getIntExtra(LEAGUE_KEY, 0);
        currentTeamId     = intent.getIntExtra(TEAM_KEY, 0);

        final CalendarView c = (CalendarView) findViewById(R.id.calendarView1);
        cal.setTimeInMillis(c.getDate());
        //eventList = datasource.getListOfEvents(Integer.parseInt(sdf.format(cal.getTime())));
        eventList = datasource.getListOfEvents();
        ListAdapter adapter = new EventListAdapter(getApplicationContext(), R.layout.notification_list_item, eventList, datasource);
        ListView listview = (ListView) findViewById(R.id.eventList);
        listview.setAdapter(adapter);
//        c.setOnDateChangeListener(new OnDateChangeListener() {
//
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
//                cal.setTimeInMillis(c.getDate());
//                //eventList = datasource.getListOfEvents(Integer.parseInt(sdf.format(cal.getTime())));
//                eventList = datasource.getListOfEvents();
//                ListAdapter adapter = new EventListAdapter(CalendarActivity.this, R.layout.notification_list_item, eventList);
//                ListView listview = (ListView) findViewById(R.id.eventList);
//                listview.setAdapter(adapter);
//            }
//        });
    }

    public void goToEditEvents(View view){
        Intent intent = new Intent(getApplicationContext(), EditEventsActivity.class);
        intent.putExtra(NAME_KEY, currentAdminName);
        intent.putExtra(ADMIN_KEY, currentAdminId);
        intent.putExtra(LEAGUE_KEY, currentLeagueId);
        intent.putExtra(TEAM_KEY, currentTeamId);
        intent.putExtra(EMAIL_KEY, currentAdminEmail);
        startActivity(intent);
    }


    @Override
    protected void onDestroy(){
        datasource.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        try {
            datasource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}