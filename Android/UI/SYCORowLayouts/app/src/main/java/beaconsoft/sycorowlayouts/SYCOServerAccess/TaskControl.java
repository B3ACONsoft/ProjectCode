package beaconsoft.sycorowlayouts.SYCOServerAccess;

/**
 * Created by Patrick on 3/15/2016.
 */
public class TaskControl {
    private String result;

    private boolean running;

    public TaskControl() {
        this.result = "";
        this.running = false;
    }

    public void start() {
        this.running = true;
    }

    public void stop() {
        this.running = false;
    }

    public boolean isRunning() {
        return this.running;
    }


   public boolean hasResult() {
       if (this.result.equals("")) {
           return false;
       }
       return true;
   }

    public void setResult(String result) {
        this.result = result;
    }
}
