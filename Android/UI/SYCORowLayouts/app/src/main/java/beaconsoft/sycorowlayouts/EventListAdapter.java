package beaconsoft.sycorowlayouts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import beaconsoft.sycorowlayouts.DataSource;
import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.dbobjects.Event;
import beaconsoft.sycorowlayouts.dbobjects.Place;
import beaconsoft.sycorowlayouts.dbobjects.Team;

public class EventListAdapter extends ArrayAdapter<Event>{
	private DataSource dataSource;
	private List<Event> data = null;
	private static LayoutInflater inflater=null;

	public EventListAdapter(Context context, int layoutResourceId, List<Event> data, DataSource dataSource) {
		super(context, layoutResourceId, data);
		this.data = data;
		this.dataSource = dataSource;
	}
	
		@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if(convertView == null){
			inflater = LayoutInflater.from(getContext());
			row = inflater.inflate(R.layout.list_view_event_items, null);
		}
		
		TextView type = (TextView)row.findViewById(R.id.eventItemTypeDateTime);
		TextView place = (TextView)row.findViewById(R.id.eventItemPlace);
			TextView participants = (TextView)row.findViewById(R.id.eventParticipants);

			Event e = data.get(position);
			Place p = dataSource.getPlaceById(e.getPlaceID());
		type.setText(e.getEventType() + " " + e.getStartDateTime());
		place.setText(p.getPlaceName() + "\n" + p.getStreetAddress() + " " + p.getCity() + ", " +
				p.getState() + " " + p.getZip());
			Team t2 = dataSource.getTeamById(e.getAwayTeamID());
			Team t1 = dataSource.getTeamById(e.getHomeTeamID());
		if(t2 == null){
			participants.setText(t1.getTeamName());
		}else {
			participants.setText(t1.getTeamName() + " vs " + t2.getTeamName());
		}
			return row;
	}
}
