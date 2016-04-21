package beaconsoft.sycorowlayouts.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.EventListAdapter;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.activities.adapter.CalendarAdapter;
import beaconsoft.sycorowlayouts.activities.util.CalendarCollection;
import beaconsoft.sycorowlayouts.dbobjects.Event;
import beaconsoft.sycorowlayouts.dbobjects.Place;
import beaconsoft.sycorowlayouts.dbobjects.Team;
import string.utils.MonthFormat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    public GregorianCalendar cal_month, cal_month_copy;
    private CalendarAdapter cal_adapter;
    private TextView tv_month;

    private static final String   NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private static final String  ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private static final String   TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";

    private Team currentTeam = new Team();
    private int currentTeamId;
    private int currentAdminId;
    private int currentLeagueId;
    private String name;
    private String currentAdminEmail;
    private DataSource dataSource;
    private ArrayList<Event> arrayListEvents;
    private ListAdapter eventListAdapter;
    private  ListView listView;
    private Event currentEvent;
    private List<Address> addresses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        dataSource = new DataSource(this);
        arrayListEvents = new ArrayList<>();
        listView = (ListView)findViewById(R.id.lv_android);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        currentLeagueId = intent.getIntExtra(LEAGUE_KEY, 0);
        currentAdminId = intent.getIntExtra(ADMIN_KEY, 0);
        currentAdminEmail = intent.getStringExtra(EMAIL_KEY);
        name = intent.getStringExtra(NAME_KEY);
        currentTeamId = intent.getIntExtra(TEAM_KEY, 0);

        currentTeam = dataSource.getTeamById(currentTeamId);
        arrayListEvents.addAll(dataSource.getListOfEventsByTeam(currentTeam));

        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        cal_adapter = new CalendarAdapter(this, cal_month, /* What is this? */ CalendarCollection.date_collection_arr,
                currentTeam, dataSource);

        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));

        ImageButton previous = (ImageButton) findViewById(R.id.ib_prev);

        previous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
        next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });
        final ListView listView = (ListView)findViewById(R.id.lv_android);
        GridView gridview = (GridView) findViewById(R.id.gv_calendar);
        gridview.setAdapter(cal_adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent == listView) {
                    int placeID = arrayListEvents.get(position).getPlaceID();
                    Place p = dataSource.getPlaceById(placeID);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {

                        addresses.addAll(geocoder.getFromLocationName(
                                String.format(Locale.ENGLISH, "%s %s, %s %d",
                                        p.getStreetAddress(), p.getCity(), p.getState(),
                                        p.getZip()), 1));
                    } catch (IOException e) {
                        Log.e("GEOLOCATION VARIABLES", "***");
                    }
                    if (addresses.size() > 0) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                "google.navigation:q=" +
                                        Uri.encode(p.getStreetAddress() + " " +
                                                p.getCity() + ", " +
                                                p.getState() + " " +
                                                p.getZip()) + "&mode=d&avoid=tf"
                        ));
//                        intent.putExtra("LATITUDE", addresses.get(0).getLatitude());
//                        intent.putExtra("LONGITUDE", addresses.get(0).getLongitude());
//                        intent.putExtra("LOCATION_NAME", p.getPlaceName());
                        intent.setPackage("com.google.android.apps.maps");
//                        intent.setClassName(getApplicationContext(), "beaconsoft.sycorowlayouts.activities.MapsActivity");
                        startActivity(intent);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Perhaps you've typed the wrong address?", Toast.LENGTH_LONG);
                        toast.setText(addresses.size() + " results from " + p.getStreetAddress() + " " +
                                p.getCity() + " , " + p.getState() + " " + p.getZip());
                        toast.show();
                    }
                }
                return true;
            }
        });

        gridview.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ((CalendarAdapter) parent.getAdapter()).setSelected(v,position);
                String selectedGridDate = CalendarAdapter.day_string
                        .get(position);

                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*","");
                int gridvalue = Integer.parseInt(gridvalueString);

                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v, position);
                ((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate, CalendarActivity.this);
                arrayListEvents.addAll(dataSource.getListOfEventsByTeam(currentTeam));
                ArrayList<String> compDates = new ArrayList<String>();
                for(int i = 0; i < arrayListEvents.size(); i++) {
                    String[] splitDate = arrayListEvents.get(i).getStartDateTime().toString().split(" ");
                    Log.e("SPLIT STRING", " ............." + arrayListEvents.get(i).getStartDateTime().toString());
                    int monthInt = -1;
                    String monthIntString = "";
                    for (int j = 0; j < MonthFormat.months.length; j++) {
                        if (splitDate[1].equals(MonthFormat.months[j])) {
                            monthInt = j + 1;
                            if (monthInt < 10) {
                                monthIntString = "0" + monthInt;
                            } else {
                                monthIntString = monthInt + "";
                            }
                            break;
                        }
                    }
                    compDates.add("" + splitDate[5] + "-" + monthIntString + "-" + splitDate[2]);
                }
                listView.clearChoices();
                ArrayList<Event> tempArrayListEvents = (ArrayList<Event>) arrayListEvents.clone();
                arrayListEvents.clear();
                for(int i = 0; i < compDates.size(); i++) {

                    if (compDates.get(i).equals(selectedGridDate)){
                        Event event = tempArrayListEvents.get(i);
                        if(!arrayListEvents.contains(event)) {
                            arrayListEvents.add(event);
                        }
                    }
                }

                populateListView();
            }
        });
    }

    //TODO: --> This stuff first - Put in Datasource, and replace your listview adapter with mine, which is really
    //just the list_view and adapter you sent me 3 weeks ago debugged, "beaconsoft.sycorowlayouts.EventListAdpater.java
    //and the xml file list_view_event_items.xml

    //TODO: --> This stuff second, for sure - short click on a date reconstitutes the listview,
    // Clear the arraylist, repopulate the arraylist, and pass into a new adapter

    //TODO: --> maybe not at all (MAYBE) long-click, make a new intent and pass the date
    public void displayAllEvents(View view){
        arrayListEvents.addAll(dataSource.getListOfEventsByTeam(currentTeam));
        populateListView();
    }

    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
                    cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
                    cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    public void refreshCalendar() {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }
    
    private void populateListView(){

        eventListAdapter = new EventListAdapter(this, R.layout.list_view_event_items, arrayListEvents, dataSource);
        listView.setAdapter(eventListAdapter);
        listView.setItemsCanFocus(true);
        currentEvent = (Event)listView.getSelectedItem();
    }

    public void goToEditEvents(View view){
        Intent intent = new Intent(this, EditEventsActivity.class);
        intent.putExtra(LEAGUE_KEY, currentLeagueId);
        intent.putExtra(NAME_KEY, name);
        intent.putExtra(ADMIN_KEY, currentAdminId);
        intent.putExtra(EMAIL_KEY, currentAdminEmail);
        intent.putExtra(TEAM_KEY, currentTeamId);
        startActivity(intent);
    }

}


