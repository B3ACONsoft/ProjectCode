package beaconsoft.sycorowlayouts.activities;

/**
 * Created by Dave on 4/25/2016.
 */
public class LoginSemaphore {
    public boolean running;
    public String loginResult;

    public LoginSemaphore() {
        this.running = true;
        loginResult = null;
    }
}
