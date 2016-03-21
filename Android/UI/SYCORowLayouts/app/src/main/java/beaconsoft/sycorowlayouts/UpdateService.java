package beaconsoft.sycorowlayouts;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;

import beaconsoft.sycorowlayouts.activities.CustomFlag;

/**
 * Created by Patrick on 3/18/2016.
 */
public class UpdateService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * Used to name the worker thread, important only for debugging.
     */
    public UpdateService(){
        super("updateService");
    }

    public UpdateService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int count = 0;
        while(true){
            count++;
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Log.e("ERROR", "..." + e.getMessage().toString());
            }
        }
    }
}
