package beaconsoft.sycorowlayouts.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.CalendarView;
import android.widget.DatePicker;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by Patrick on 3/24/2016.
 */
public class DatePickerFragment extends DialogFragment {

    // Use the current date as the default date in the picker
    private final Calendar c = Calendar.getInstance();
    private int year = c.get(Calendar.YEAR);
    private int month = c.get(Calendar.MONTH);
    private int day = c.get(Calendar.DAY_OF_MONTH);

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), (EditEventsActivity)getActivity(), year, month, day);
    }


}