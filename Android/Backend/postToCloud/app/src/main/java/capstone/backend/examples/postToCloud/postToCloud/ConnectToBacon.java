package capstone.backend.examples.postToCloud.postToCloud;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by JimBob on 2/7/2016.
 */
public class ConnectToBacon
        extends AsyncTask<String, Void, String>
        implements ConnectionConstants, CommandConstants {

    private Object callingTread;                //the calling thread
    private HttpURLConnection conn;             //http connection
    private URL url;                            //connection url object

    /*
        Constructor
            args:
                 command
                    Choose from string constants in CommandConstants.java
                 targetTable
                    Choose from targetTable string names in ConnectionConstants.java
                 data
                    HashMap containing the key/ value pairs of data.
                    Note that the keys must valid constants in the ConnectionConstants.java
                    and correspond to the correct targetTable

                 callingTread
                    a reference to the calling Activity.
                    It allows (callingActivity).updateBacon() to be called from
                    this thread.
     */
    public ConnectToBacon(String command,
                          String targetTable,
                          HashMap<String, String> data,
                          Object callingThread)
            throws Exception {
        this.callingTread = callingThread;


        boolean i = false;
        //is it a valid command?
        switch(command) {
            case INSERT:
            case UPDATE:
            case SELECT:
                i = true;
                break;
            default:
                throw new Exception("ConnectToBacon: INVAID COMMAND");
        }
        //is it a valid table?
        if(i) {
            i = false;
            switch(targetTable) {
                case COACHES_TABLE:
                    i = true;
                    break;
                default:
                    throw new Exception("ConnecToBacon: INVALID TABLE");
            }

        }
        //if all good then we init
        if(i) {
            //add the command and the target table to the post request
            String postData
                    = "commmand=" + command + "&"
                    + "table=" + targetTable + "&";
            postData += parsePostParams(data);
            this.execute(postData);
        }
    }

    //This runs postRequest on a serparate thread and returns the result
    //to onPostExecute.
    @Override
    protected String doInBackground(String... params) {
        try
        {
            return postRequest(SERVER_URL, params[0]);
        } catch (Exception e){
            Toast.makeText(((MainActivity)callingTread).getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_SHORT);
            return " ";
        }

    }

    /*
        When the post request is done, or an error is thrown,
        this method is called.
        It calls the updateBacon method in the callingActivity
        which handles the result or error from the request.
     */
    @Override
    protected  void onPostExecute(String result) {
        super.onPostExecute(result);
        ((BaconUpdate)callingTread).updateBacon(result);
    }

    /*
        Takes a hashmap of key/ value pairs and
        formats correctly for the post request in the form

        key1=value1&key2=value2&key3=value3...key(n)=value(n)

        key value pairs separated by & sign
     */
    private String parsePostParams(HashMap<String, String> paramsMap) throws Exception
    {
        StringBuilder paramsBuilder = new StringBuilder();
        for(String key: paramsMap.keySet()) {
            try {

                paramsBuilder.append(key);
                paramsBuilder.append("=");
                paramsBuilder.append(
                        URLEncoder.encode(paramsMap.get(key), "UTF-8"));            //we must use this encoding
                                                                                    //on the value only
                paramsBuilder.append("&");
            } catch (UnsupportedEncodingException e)
            {
                throw new Exception("param Parsing error: " + e.getMessage());
            }
        }
        return paramsBuilder.toString();
    }

    //make a post request to the url with the given parameters
    //return the response in string form
    public String postRequest(String url, String params) throws Exception {
        StringBuilder responseBuffer = new StringBuilder();
        try
        {
            byte[] bytes = params.getBytes();   //convert the data to bytes

            this.url = new URL(SERVER_URL);                                                      //
            conn = (HttpURLConnection) this.url.openConnection();                               //init the connection objects
            conn.setDoInput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");                                                      //set request method
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");       //set the attributes of the HTTP header
            conn.setRequestProperty("charset", "utf-8");                                        //more attributes...
            conn.setRequestProperty("Content-Length", Integer.toString(bytes.length));          //more attributes...
            conn.setConnectTimeout(10000);                                                      //set the timeout if things break

            DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());   //write data across the socket
            dataOutputStream.write(bytes);
            dataOutputStream.flush();
            dataOutputStream.close();


            InputStreamReader reader = new InputStreamReader(conn.getInputStream());            //get the response
            int input;
            while((input = reader.read()) != -1)
            {
                responseBuffer.append((char)input);
            }
            reader.close();

        } catch (IOException e) {
            throw new Exception("Error making post Request, message: " + e.getMessage());
        } finally {
            conn.disconnect();
        }
        return responseBuffer.toString();                                                       //pass the response to onPostExecute()
    }

}
