package com.example.analembargo.posttocloud;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by AnalEmbargo on 2/7/2016.
 */
public class ConnectToBacon
        extends AsyncTask<String, Void, String>
        implements ConnectionConstants, CommandConstants {

    private Object callingTread;                //the calling thread
    private HttpURLConnection conn;
    private URL url;

    public ConnectToBacon(String command,
                          String targetTable,
                          HashMap<String, String> data,
                          Object callingThread) {
        this.callingTread = callingThread;

        //add the command and the target table to the post request

        boolean i = false;
        //is it a valid command?
        switch(command) {
            case INSERT:
            case UPDATE:
            case SELECT:
                i = true;
                break;
            default:
                ((BaconUpdate)callingThread).updateBacon("ERROR: INVALID COMMAND");
        }
        //is it a valid table?
        if(i) {
            i = false;
            switch(targetTable) {
                case COACHES_TABLE:
                    i = true;
                    break;
            }
        }
        //if all good then we init
        if(i) {
            String postData
                    = "commmand=" + command + "&"
                    + "table=" + targetTable + "&";
            postData += parsePostParams(data);
            this.execute(postData);
        }
    }

    @Override
    protected String doInBackground(String... params) {
        return postRequest(SERVER_URL, params[0]);
    }

    @Override
    protected  void onPostExecute(String result) {
        super.onPostExecute(result);
        ((BaconUpdate)callingTread).updateBacon(result);
    }

    private String parsePostParams(HashMap<String, String> paramsMap)
    {
        StringBuilder paramsBuilder = new StringBuilder();
        for(String key: paramsMap.keySet()) {
            try {

                paramsBuilder.append(key);
                paramsBuilder.append("=");
                paramsBuilder.append(
                        URLEncoder.encode(paramsMap.get(key), "UTF-8"));
                paramsBuilder.append("&");
            } catch (UnsupportedEncodingException e)
            {
                //oh well we don't catch this cuz nope...
            }
        }
        return paramsBuilder.toString();
    }

    //make a post request
    public String postRequest(String url, String params){
        StringBuilder responseBuffer = new StringBuilder();
        try
        {
            byte[] bytes = params.getBytes();   //convert the data to bytes

            this.url = new URL(SERVER_URL);
            conn = (HttpURLConnection) this.url.openConnection();   //init the connection objects
            conn.setDoInput(true);
            conn.setDoInput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");                          //set request method
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");       //set the attributes of the HTTP header
            conn.setRequestProperty("charset", "utf-8");                                        //more attributes...
            conn.setRequestProperty("Content-Length", Integer.toString(bytes.length));          //more attributes...
            conn.setConnectTimeout(10000);                                                      //set the timeout if things break

            DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());   //get output stream from websocket
            dataOutputStream.write(bytes);                                                      //write data across the socket
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
            return e.getMessage();
        } finally {
            conn.disconnect();
        }
        return responseBuffer.toString();                                                       //pass the response to onPostExecute()
    }

}
