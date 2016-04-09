package beaconsoft.sycorowlayouts.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.dbobjects.Player;

public class UserRosterActivity extends AppCompatActivity {


    private static final String TEAM_KEY = "beaconsoft.sycorowlayouts.TEAM";
    private int currentTeamId = 0;
    private DataSource dataSource;
    private ArrayList<Player> rosterList = new ArrayList<>();
    private ListView listViewPlayerContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_roster);

        dataSource = new DataSource(this);
        Intent intent = getIntent();
        currentTeamId = intent.getIntExtra(TEAM_KEY, 0);

        rosterList.addAll(dataSource.getListOfPlayersByTeam(currentTeamId));

        listViewPlayerContacts = (ListView)findViewById(R.id.listViewUserActivityTest);

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
