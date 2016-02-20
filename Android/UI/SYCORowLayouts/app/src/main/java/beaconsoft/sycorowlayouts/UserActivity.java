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

public class UserActivity extends AppCompatActivity {

    private DBHelper helper = new DBHelper(this);
    private ArrayList<HashMap<String, String>> rosterList;
    private ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        DBHelper helper = new DBHelper(this);
        String query;

//        query = "INSERT INTO users VALUES ('1', 'PATRICK', 'CURRIN', '(919)693-2121', '(919)693-8011', 'CURRIN.PATRICK@YAHOO.COM', 'ADMIN')";
//        helper.getWritableDatabase().execSQL(query);
//        query = "INSERT INTO users VALUES ('2', 'KATY', 'PERRY', '(919)345-6543', '(919)456-7894', 'KATY@PERRY.COM', 'USER')";
//        db.execSQL(query);


        rosterList = helper.getSQLiteUsers();
        ListView testView = (ListView)findViewById(R.id.listViewUserActivityTest);
        ListAdapter adapter = new SimpleAdapter(UserActivity.this, rosterList, R.layout.list_view_users_row_layout,
                new String[] {  "fname", "lname", "phone", "emergency", "email", "user_type" },
                new int[]    {  R.id.testfname, R.id.testlname, R.id.testphone});
        testView.setVisibility(View.VISIBLE);
        testView.setAdapter(adapter);

        helper.getWritableDatabase().delete("users", null, null);


    }

    protected void onDestroy(Bundle savedInstanceState){
        super.onDestroy();

    }
}
