package SYCOServerAccess;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.LinkedList;
import java.util.Queue;

import SYCOServerAccess.RemoteDeleteOperations;
import SYCOServerAccess.RemoteInsertOperations;
import SYCOServerAccess.RemoteConnection;
import SYCOServerAccess.UpdateOperations;

/**
 * Created by Dave on 3/18/2016.
 */
public class UpdateService extends Service {

    private class ServerConnectionInterface {
        public boolean running;
        public Queue<Object> insertQueue;
        public Queue<Object> updateQueue;
        public Queue<Object> deleteQueue;

        public ServerConnectionInterface() {
            insertQueue = new LinkedList<Object>();
            updateQueue = new LinkedList<Object>();
            deleteQueue = new LinkedList<Object>();
        }

        /**
         * Check if any of the queue have data
         * @return
         */
        public boolean hasData() {
            if(this.insertQueue.size() > 0
                    || this.updateQueue.size() > 0
                    || this.deleteQueue.size() > 0) {
                return true;
            }
            return false;
        }

    }

    private class ServerConnectionProcess implements Runnable {

        private ServerConnectionInterface serverConnectionInterface;                    //a reference to the serverConnectionInterface
        private RemoteConnection remoteConnection;
        private RemoteInsertOperations insertOperations;
        private UpdateOperations updateOperations;
        private RemoteDeleteOperations deleteOperations;
        private RemoteSelectOperations selectOperations;
        private final int SYNC_RATE_MILLISECONDS = 10000;                               //the rate at which the sync operation cycles in milliseconds

        public ServerConnectionProcess(ServerConnectionInterface serverConnectionInterface) {
            this.remoteConnection = new RemoteConnection();
            this.insertOperations = new RemoteInsertOperations(remoteConnection);
            this.updateOperations = new UpdateOperations(remoteConnection);
            this.deleteOperations = new RemoteDeleteOperations(remoteConnection);
            this.selectOperations = new RemoteSelectOperations(remoteConnection);
            this.serverConnectionInterface = serverConnectionInterface;

        }
        
        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {
            while(serverConnectionInterface.running) {
                //get all data from server
                //TODO implement get all

                //check to see if any data to upload to server
                if(serverConnectionInterface.hasData()) {
                    //do delete operations
                    while(serverConnectionInterface.deleteQueue.peek() != null) {
                        deleteOperations.deletePOJO(serverConnectionInterface.deleteQueue.poll());
                    }
                    //do insert operations
                    while(serverConnectionInterface.insertQueue.peek() != null) {
                        //poll the queue and do the the operation
                        insertOperations.insertPOJO(serverConnectionInterface.insertQueue.poll());
                    }
                    //do update operations
                    while(serverConnectionInterface.updateQueue.peek() != null) {
                        //poll the queue and do the the operation
                        updateOperations.updatePOJO(serverConnectionInterface.updateQueue.poll());
                    }
                }

                //sleep for SYNC_RATE_MILLISECONDS
                //is sleep the right thing to do?
                //is it too expensive
                try {
                    Thread.sleep(SYNC_RATE_MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private ServerConnectionInterface serverConnectionInterface;              //this the memory space that serverConnectionProcess can access
    private ServerConnectionProcess serverConnectionProcess;            //this the class which is run in the background connection thread
    private Thread serverConnectionThread;

    @Override
    public void onCreate() {
        //init serverConnectionInterface memory
        serverConnectionInterface = new ServerConnectionInterface();
        
        //start the serverConnectionThread
        startServerConnectionProcess();
    }
    
    /*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }
    */
    /**
        We retun null because we don't want activities binding to this.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //make the sure the thread is dead
    @Override
    public void onDestroy() {
        stopServerConnectionProcess();
    }

    public void startServerConnectionProcess() {
        serverConnectionInterface.running = true;
        serverConnectionProcess = new ServerConnectionProcess(serverConnectionInterface);
        serverConnectionThread = new Thread(serverConnectionProcess);
        serverConnectionThread.start();
    }

    public void stopServerConnectionProcess() {
        this.serverConnectionInterface.running = false;
    }

    public void queueInsert(Object insert) {
        this.serverConnectionInterface.insertQueue.add(insert);
    }

    public void queueUpdate(Object update) {
        this.serverConnectionInterface.updateQueue.add(update);
    }

    public void queueDelete(Object delete) {
        this.serverConnectionInterface.deleteQueue.add(delete);
    }

}
