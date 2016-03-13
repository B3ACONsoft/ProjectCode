package beaconsoft.sycorowlayouts;

import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class UserRosterActivity extends AppCompatActivity {

//    private DBHelper helper = new DBHelper(this);
//    private ArrayList<HashMap<String, String>> rosterList;
//    private ProgressDialog prgDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_roster);
//
//
//        rosterList = helper.getSQLiteUsers();
//        ListView testView = (ListView)findViewById(R.id.listViewUserActivityTest);
//        ListAdapter adapter = new SimpleAdapter(UserRosterActivity.this, rosterList, R.layout.list_view_users_row_layout,
//                new String[] {  "user_id", "lname", "phone" },
//                new int[]    {  R.id.testfname, R.id.testlname, R.id.testphone});
//        testView.setVisibility(View.VISIBLE);
//        testView.setAdapter(adapter);
//
//
//
//    }
//
//    protected void onDestroy(Bundle savedInstanceState){
//        super.onDestroy();
//
//    }
}
