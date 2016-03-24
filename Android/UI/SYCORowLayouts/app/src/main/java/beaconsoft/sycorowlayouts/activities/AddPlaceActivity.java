package beaconsoft.sycorowlayouts.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import beaconsoft.sycorowlayouts.dbobjects.Place;

public class AddPlaceActivity extends AppCompatActivity implements OnItemSelectedListener {

    private DataSource dataSource = new DataSource(this);
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

    @Override
    protected void onResume(){
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy(){
        dataSource.close();
        super.onDestroy();
    }

    @Override
    protected void onPause(){
        dataSource.close();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(String s: arrayStates){
            states.add(s);
        }
        spinnerStates = (Spinner) findViewById(R.id.spinnerStates);
        spinnerStatesAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                states);
        spinnerStates.setOnItemSelectedListener(this);
        spinnerStates.setAdapter(spinnerStatesAdapter);
        placesAddPlacesListView = (ListView)findViewById(R.id.listViewAddPlaces);
        places.addAll(dataSource.getListOfPlaces());
        if (places.size() > 0){
            spinnerPlacesAdapter = new PlaceListAdapter(this, R.layout.custom_list_view_places, places, dataSource);
            placesAddPlacesListView.setAdapter(spinnerPlacesAdapter);
        }
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



        dataSource.createPlace(name.getText().toString().toUpperCase(),
                streetAddress.getText().toString().toUpperCase(),
                city.getText().toString().toUpperCase(),
                spinnerStates.getSelectedItem().toString().toUpperCase(),
                Integer.parseInt(zip.getText().toString()));

        places.clear();
        places.addAll(dataSource.getListOfPlaces());
        if (places.size() > 0){
            spinnerPlacesAdapter = new PlaceListAdapter(this, R.layout.custom_list_view_places, places, dataSource);
            placesAddPlacesListView.setAdapter(spinnerPlacesAdapter);
        }
    }

    public void openGoogleMaps(View view){

    }
}
