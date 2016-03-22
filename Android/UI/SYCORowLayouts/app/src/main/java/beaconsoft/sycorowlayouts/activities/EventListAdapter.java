package beaconsoft.sycorowlayouts.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import beaconsoft.sycorowlayouts.R;
import beaconsoft.sycorowlayouts.dbobjects.Event;

public class EventListAdapter extends ArrayAdapter<Event>{

	private List<Event> data = null;
	private static LayoutInflater inflater=null;
	
	public EventListAdapter(Context context, int layoutResourceId, List<Event> data) {
		super(context, layoutResourceId, data);
		this.data = data;
	}
	
		@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if(convertView == null){
			inflater = LayoutInflater.from(getContext());
			row = inflater.inflate(R.layout.notification_list_item, null);
		}
		
		TextView type = (TextView)row.findViewById(R.id.notificationType);
		TextView place = (TextView)row.findViewById(R.id.notificationPlace);
		
		Event e = data.get(position);
		type.setText(e.getEventType());
		place.setText(e.getPlaceID());
		
		return row;
	}
}
