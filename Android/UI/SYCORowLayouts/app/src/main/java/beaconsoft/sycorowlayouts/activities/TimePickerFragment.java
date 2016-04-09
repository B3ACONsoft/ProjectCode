package beaconsoft.sycorowlayouts.activities;

import android.app.Activity;
import android.content.Intent;
import android.text.format.DateFormat;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import beaconsoft.sycorowlayouts.R;

public class TimePickerFragment extends DialogFragment
             implements TimePickerDialog.OnTimeSetListener {

    private static final int TIME_PICKER_REQUESTCODE = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), (EditEventsActivity)getActivity(), hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Intent intent = new Intent();
        intent.putExtra("hour_of_day", hourOfDay);
        intent.putExtra("minute", minute);
        this.onActivityResult(TIME_PICKER_REQUESTCODE, Activity.RESULT_OK, intent);

    }

    
}
