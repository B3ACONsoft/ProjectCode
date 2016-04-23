package beaconsoft.sycorowlayouts.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.PlaceListAdapter;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.SYCOServerAccess.UpdateService;
import beaconsoft.sycorowlayouts.dbobjects.Place;

public class AddPlaceActivity extends AppCompatActivity implements OnItemSelectedListener {

    private Spinner spinnerStates;
    private ArrayList<String> states = new ArrayList<>();
    private List<Place>      places  = new ArrayList<>();
    private String[]arrayStates = new String[] { "AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA",
            "HI","ID","IL","IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE",
            "NV","NH","NJ","NM","NY","NC","ND","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT",
            "VT","VA","WA","WV","WI","WY" };

    private ArrayAdapter spinnerStatesAdapter;
    private ListAdapter spinnerPlacesAdapter;
    private ListView placesAddPlacesListView;
    private EditText          name;
    private EditText streetAddress;
    private EditText          city;
    private EditText           zip;
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
            for(String s: arrayStates){
                states.add(s);
            }
            spinnerStates = (Spinner) findViewById(R.id.spinnerStates);
            spinnerStatesAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                    states);
            spinnerStates.setOnItemSelectedListener(this);
            spinnerStates.setAdapter(spinnerStatesAdapter);
            placesAddPlacesListView = (ListView)findViewById(R.id.listViewAddPlaces);
            places.addAll(updateService.getListOfPlaces());
            if (places.size() > 0){
                spinnerPlacesAdapter = new PlaceListAdapter(this, R.layout.custom_list_view_places, places, updateService);
                placesAddPlacesListView.setAdapter(spinnerPlacesAdapter);
            }

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
    protected void onResume(){

        super.onResume();
    }

    @Override
    protected void onDestroy(){

        super.onDestroy();
    }

    @Override
    protected void onPause(){

        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.setSelection(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void addNewPlace(View view){
                 name = (EditText)findViewById(R.id.editTextAddPlacePlaceName);
        streetAddress = (EditText)findViewById(R.id.editTextAddPlacetreetAddress);
                 city = (EditText)findViewById(R.id.editTextAddPlaceCity);
                  zip = (EditText)findViewById(R.id.editTextAddPlaceZip);


        if(mBound) {
            updateService.createPlace(name.getText().toString().toUpperCase(),
                    streetAddress.getText().toString().toUpperCase(),
                    city.getText().toString().toUpperCase(),
                    spinnerStates.getSelectedItem().toString().toUpperCase(),
                    Integer.parseInt(zip.getText().toString()));

            places.clear();
            places.addAll(updateService.getListOfPlaces());
            if (places.size() > 0){
                spinnerPlacesAdapter = new PlaceListAdapter(this, R.layout.custom_list_view_places, places, updateService);
                placesAddPlacesListView.setAdapter(spinnerPlacesAdapter);
            }
        }

    }

    public void openGoogleMaps(View view){

    }
}
