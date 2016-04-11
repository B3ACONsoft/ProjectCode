package beaconsoft.sycorowlayouts.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.dbobjects.Player;
import beaconsoft.sycorowlayouts.dbobjects.Users;

public class UserRosterActivity extends AppCompatActivity {


    private static final String TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";
    private int currentTeamId = 0;
    private DataSource dataSource;
    private ArrayList<Player> rosterList = new ArrayList<>();
    private ListView listViewPlayerContacts;
    List<HashMap<String, String>> fillMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_roster);

        dataSource = new DataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            Log.e("DATABASE EXCEPTION", "...............FAILED TO OPEN" + "\n" + e.getMessage());
        }
        Intent intent = getIntent();
        currentTeamId = intent.getIntExtra(TEAM_KEY, 0);

        String[] from = new String[]{"player_fname", "player_lname", "user_fname", "user_lname", "user_phone"};
        int[] to = new int[]{R.id.player_fname, R.id.player_lname, R.id.user_fname, R.id.user_lname, R.id.user_phone};
        rosterList.clear();
        rosterList.addAll(dataSource.getListOfPlayersByTeam(currentTeamId));
        fillMaps = new ArrayList<>();
        for (Player p : rosterList) {
            Users user = dataSource.getUserById(p.getUserID());
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
