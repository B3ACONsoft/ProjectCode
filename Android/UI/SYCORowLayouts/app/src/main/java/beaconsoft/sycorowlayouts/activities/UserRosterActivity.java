package beaconsoft.sycorowlayouts.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.wearable.MessageApi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService;
import beaconsoft.sycorowlayouts.dbobjects.Player;
import beaconsoft.sycorowlayouts.dbobjects.Users;

public class UserRosterActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    private static final String EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";
    private static final String PHONE_KEY = "beaconsoft.sycorowlayouts.PHONE";
    private static final String NAME_KEY = "beaconsoft.sycorowlayouts.NAME";
    private int currentTeamId = 0;
    private String email = "";

    private ArrayList<Player> rosterList = new ArrayList<>();
    private ListView listViewPlayerContacts;
    List<HashMap<String, String>> fillMaps;
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

        if(mBound) {
            Intent intent = getIntent();
            currentTeamId = intent.getIntExtra(TEAM_KEY, 0);
            email = intent.getStringExtra(EMAIL_KEY);
            String[] from = new String[]{"player_fname", "player_lname", "user_fname", "user_lname", "user_phone"};
            int[] to = new int[]{R.id.player_fname, R.id.player_lname, R.id.user_fname, R.id.user_lname, R.id.user_phone};
            rosterList.clear();
            rosterList.addAll(updateService.getListOfPlayersByTeam(currentTeamId));
            fillMaps = new ArrayList<>();
            for (Player p : rosterList) {
                Users user = updateService.getUserById(p.getUserID());
                HashMap<String, String> mapper = new HashMap<>();
                mapper.put("player_fname", p.getFname());
                mapper.put("player_lname", p.getLname());
                mapper.put("user_fname", user.getFname());
                mapper.put("user_lname", user.getLname());

                String phoneNumber = user.getPhone() + "";
                phoneNumber = "(" + phoneNumber.substring(0, 3) + ")" + phoneNumber.substring(3, 6) +
                        "-" + phoneNumber.substring(6);

                mapper.put("user_phone", phoneNumber);
                fillMaps.add(mapper);
            }
            listViewPlayerContacts = (ListView) findViewById(R.id.listViewUserActivityTest);
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, fillMaps, R.layout.list_view_users_row_layout, from, to);
            listViewPlayerContacts.setAdapter(simpleAdapter);
            listViewPlayerContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
                            fillMaps.get(position).get("user_phone")));
                    dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (ActivityCompat.checkSelfPermission(UserRosterActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(dialIntent);
                }
            });
            listViewPlayerContacts.setOnItemLongClickListener(this);

        }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("OPTS ITEM SELECTED BACK", "................HIT BACK ON TOOLBAR");
        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed(){

        Log.e("ON BACK PRESSED", "...............QUICK ADD TEAMS ON BACK PRESSED");
        Intent intent = new Intent();
        intent.putExtra(TEAM_KEY, currentTeamId);
        intent.putExtra(EMAIL_KEY, email);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_roster);



    }

    @Override
    protected void onPause(){

        super.onPause();
    }

    @Override
    protected void onDestroy(){

        super.onDestroy();
    }

    @Override
    protected void onResume()
    {

        super.onResume();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        String phoneNumber = fillMaps.get(position).get("user_phone");
        String fname = fillMaps.get(position).get("user_fname");
        String lname = fillMaps.get(position).get("user_lname");
        String name = fname + " " + lname;
        Intent intent = new Intent(this, MessagingActivity.class);
        intent.putExtra(PHONE_KEY, phoneNumber);
        intent.putExtra(NAME_KEY, name);
        startActivity(intent);
        return true;
    }
}
