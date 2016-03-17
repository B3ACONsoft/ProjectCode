package beaconsoft.sycorowlayouts.activities;

import beaconsoft.sycorowlayouts.TestRoutines.AdminTests;
import beaconsoft.sycorowlayouts.TestRoutines.UserValidationTests;

/**
 * Created by Patrick on 3/15/2016.
 */
public class TestConnectionThread implements Runnable {

    private CustomFlag refFlag;

    public TestConnectionThread(CustomFlag refFlag) {
        this.refFlag = refFlag;
    }
    @Override
    public void run() {
        AdminTests adminTest = new AdminTests();
        UserValidationTests userValidationTests = new UserValidationTests();
        refFlag.start();

        while(refFlag.isRunning() && !refFlag.hasResult()) {
            refFlag.setResult(adminTest.testAdminGetAllSports());
        }
    }
}
