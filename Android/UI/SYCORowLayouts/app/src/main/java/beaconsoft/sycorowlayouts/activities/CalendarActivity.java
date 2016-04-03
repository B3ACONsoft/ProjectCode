package beaconsoft.sycorowlayouts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.activities.adapter.CalendarAdapter;
import beaconsoft.sycorowlayouts.activities.util.CalendarCollection;

import java.util.GregorianCalendar;

public class CalendarActivity extends AppCompatActivity {

    public GregorianCalendar cal_month, cal_month_copy;
    private CalendarAdapter cal_adapter;
    private TextView tv_month;

    private static final String   NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private static final String  ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String  EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String LEAGUE_KEY = "beaconsoft.sycorowlayouts.LEAGUE";
    private static final String   TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";

    private int currentTeamId;
    private int currentAdminId;
    private int currentLeagueId;
    private String name;
    private String currentAdminEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Intent intent = getIntent();
        currentLeagueId = intent.getIntExtra(LEAGUE_KEY, 0);
        currentAdminId = intent.getIntExtra(ADMIN_KEY, 0);
        currentAdminEmail = intent.getStringExtra(EMAIL_KEY);
        name = intent.getStringExtra(NAME_KEY);
        currentTeamId = intent.getIntExtra(TEAM_KEY, 0);

        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        cal_adapter = new CalendarAdapter(this, cal_month, /* What is this? */ CalendarCollection.date_collection_arr);



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

        GridView gridview = (GridView) findViewById(R.id.gv_calendar);
        gridview.setAdapter(cal_adapter);
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
                ((CalendarAdapter) parent.getAdapter()).setSelected(v,position);

                ((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate, CalendarActivity.this);
            }

        });
    }

    //TODO: --> This stuff first - Put in Datasource, and replace your listview adapter with mine, which is really
    //just the list_view and adapter you sent me 3 weeks ago debugged, "beaconsoft.sycorowlayouts.EventListAdpater.java
    //and the xml file list_view_event_items.xml

    //TODO: --> This stuff second, for sure - short click on a date reconstitutes the listview,
    // Clear the arraylist, repopulate the arraylist, and pass into a new adapter

    //TODO: --> maybe not at all (MAYBE) long-click, make a new intent and pass the date


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


