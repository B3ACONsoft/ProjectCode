package beaconsoft.sycorowlayouts.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;

import beaconsoft.sycorowlayouts.MySQLiteHelper;
import beaconsoft.sycorowlayouts.dbobjects.Users;
import string.utils.ProperCase;

public class MainActivity extends AppCompatActivity {

    private String permission;
    private String email;
    private static final String EMAIL_KEY = "beaconsoft.sycorowlayouts.EMAIL";
    private static final String LEVEL_KEY = "beaconsoft.sycorowlayouts.LEVEL";
    private static final String COACH_KEY = "beaconsoft.sycorowlayouts.COACH";
    private static final String ADMIN_KEY = "beaconsoft.sycorowlayouts.ADMIN";
    private static final String NAME_KEY  = "beaconsoft.sycorowlayouts.NAME";
    private static final String ADMIN = "ADMIN";
    private static final String COACH = "COACH";
    private beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService updateService;        //reference to the update service
    boolean mBound = false;                                                                //to bind or not to bind...

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

            Intent intentLogin = getIntent();
            email = intentLogin.getStringExtra(EMAIL_KEY);
            permission = intentLogin.getStringExtra(LEVEL_KEY);
            switch (permission) {
                case ADMIN:
                    sendToLeaguesActivity(email);
                    break;
                case COACH:
                    sendToCoachHomeActivity(email);
                    break;
                default:
                    sendToUserHomeActivity(email);
                    break;
            }
//
//            String siteResponse = ""; //AdminTests.testAdminGetAllSports();
//            StringBuilder responseJsonBuilder = new StringBuilder();

//            int i = 0;
//            while(siteResponse.charAt(i) != '<' && siteResponse.charAt(i+1) != '!') {
//                responseJsonBuilder.append(siteResponse).charAt(i);
//                i++;
//            }
//            String responseJson = responseJsonBuilder.toString();
//            StringReader stringReader = new StringReader(responseJson);
//
//
//            JsonReader jsonReader = null;
//            JsonArray jsonArray = null;
//
//
//            jsonReader = Json.createReader(stringReader);
//            jsonArray = (JsonArray) jsonReader.readArray();

//            for(i = 0; i < jsonArray.size(); i++) {
//                System.out.println(jsonArray.get(i));
//            }

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
                beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService.class), mConnection, Context.BIND_AUTO_CREATE);



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

       
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySQLiteHelper.getInstance(this.getApplicationContext());
        /*

        Intent intent = new Intent();
        intent.setClassName("SYCOServerAccess", "SYCOServerAccess.UpdateService");
        startService(intent);


        */






//        CustomFlag outFlag = new CustomFlag();
//        TestConnectionThread connInstance = new TestConnectionThread(outFlag);
//        Thread t = new Thread(connInstance);
//        t.start();
//
//        while(!outFlag.hasResult())
//            ;
//
//        int i = 0;



    }

    public void sendToUserHomeActivity(String name) {
        Intent intent = new Intent(this, UserHomeActivity.class);
        intent.putExtra(EMAIL_KEY, name);
        startActivity(intent);

    }

    public void sendToLeaguesActivity(String email){

        Users tempUser = updateService.getUserByEmail(email.toUpperCase());
                int adminId  = tempUser.getUserID();
                String fname = tempUser.getFname();
                fname = ProperCase.toProperCase(fname);
                String lname = tempUser.getLname();
                lname = ProperCase.toProperCase(lname);
                String name  = fname + lname;
                email = tempUser.getEmail();
                email = email.toLowerCase();
        Intent intent = new Intent(this, LeaguesActivity.class);
        intent.putExtra(EMAIL_KEY, email);
        intent.putExtra(ADMIN_KEY, adminId);
        intent.putExtra(NAME_KEY, name);
        startActivity(intent);

    }

    public void sendToCoachHomeActivity(String email){

        Users coach = updateService.getUserByEmail(email.toUpperCase());
        int coachId  = coach.getUserID();

        Intent intent = new Intent(this, CoachHomeActivity.class);
        intent.putExtra(EMAIL_KEY, email);
        intent.putExtra(COACH_KEY, coachId);
        intent.putExtra(NAME_KEY, coach.getFname() + " " + coach.getLname());
        startActivity(intent);

    }
}
