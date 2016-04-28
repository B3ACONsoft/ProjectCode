package beaconsoft.sycorowlayouts.SYCOServerAccess;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dave on 4/19/2016.
 */
public class ConnectionTask implements Runnable {

    public TaskControl taskControl;             //this is shared memory that is used to control the thread life
    public HashMap<String, String> SQL;         //this is the SQL command to be executed if applicable
    public ArrayList<String> results;           //this is the list of result/s from the post request/s.

    /*
        Constructor with only taskControl argument.
        This will do a "get all"
     */
    public ConnectionTask(TaskControl taskControl) {
        this.taskControl = taskControl;
    }

    /*
        Constructor with taskConrol and
        with SQL parameters which will execute a sql operation on
        the server.
     */
    public ConnectionTask(TaskControl taskControl, HashMap<String, String> SQL) {
        this.taskControl = taskControl;
        this.SQL = SQL;
    }

    /*
        Gets all remote data.
        Places results in the results array.
     */
    private void getAllRemoteData() {

    }

    /*
        Execute SQL command
        Place result in results array.
     */
    private void executeRemoteCommand() {

    }

    /*
        Update local data in SQL database
        if needed.
     */
    private void updateLocalData() {
        //if the response contains data

        //insert in the DB

        //else do nothing
    }

    /*
        Check to see if SQL is null.
            If SQL is null then we are
            doing a "get all data"
                Do post request of get all.

            Else we are executing the command
            defined in SQL.
                Do post request of SQL command.

     */
    @Override
    public void run() {
        if(SQL == null) {
            getAllRemoteData();
        } else {
            executeRemoteCommand();
        }

        updateLocalData();
    }

}
