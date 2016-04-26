package beaconsoft.sycorowlayouts.activities;

import java.util.HashMap;

import beaconsoft.sycorowlayouts.SYCOServerAccess.RemoteConnection;

/**
 * Created by Dave on 4/25/2016.
 */
public class LogInProcess implements Runnable {

    private LoginSemaphore semaphore;
    private RemoteConnection remoteConnection;
    HashMap<String, String> loginCommandMap;
    private long end;
    private final long TIMEOUT_VAL = 30000; //30 second timeout


    public LogInProcess(LoginSemaphore semaphore, HashMap<String, String> loginCommandMap) {
        this.remoteConnection = new RemoteConnection();
        this.semaphore = semaphore;
        this.loginCommandMap = loginCommandMap;
    }

    private boolean stillTime(){
        if(System.currentTimeMillis() < end){
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        this.end = System.currentTimeMillis() + TIMEOUT_VAL;

        long now = System.currentTimeMillis();
    }
}
