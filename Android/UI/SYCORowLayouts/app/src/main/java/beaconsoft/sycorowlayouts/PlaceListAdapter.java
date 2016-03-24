package beaconsoft.sycorowlayouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import beaconsoft.sycorowlayouts.dbobjects.Place;

/**
 * Created by Patrick on 3/23/2016.
 */
public class PlaceListAdapter extends ArrayAdapter<Place> {
    private DataSource dataSource;
    private List<Place> data = null;
    private static LayoutInflater inflater=null;

    public PlaceListAdapter(Context context, int layoutResourceId, List<Place> data, DataSource dataSource) {
        super(context, layoutResourceId, data);
        this.data = data;
        this.dataSource = dataSource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (convertView == null) {
            inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.custom_list_view_places, null);
        }

        TextView venueName = (TextView) row.findViewById(R.id.textViewCustomPlacesVenueName);
        TextView streetAddress = (TextView) row.findViewById(R.id.textViewCustomPlacesStreetAddress);
        TextView cityStateZip = (TextView) row.findViewById(R.id.textViewCustomPlacesCityStateZip);


        Place placeForView = data.get(position);
        venueName.setText(placeForView.getPlaceName());
        streetAddress.setText(placeForView.getStreetAddress());
        cityStateZip.setText(placeForView.getCity() + ", " + placeForView.getState() + " " +
                placeForView.getZip());

        return row;
    }
}
