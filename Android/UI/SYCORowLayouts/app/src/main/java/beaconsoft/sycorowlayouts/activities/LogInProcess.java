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

    public LogInProcess(LoginSemaphore semaphore, HashMap<String, String> loginCommandMap) {
        this.remoteConnection = new RemoteConnection();
        this.semaphore = semaphore;
        this.loginCommandMap = loginCommandMap;
    }

    @Override
    public void run() {
       semaphore.loginResult = remoteConnection.login(loginCommandMap);
    }
}
