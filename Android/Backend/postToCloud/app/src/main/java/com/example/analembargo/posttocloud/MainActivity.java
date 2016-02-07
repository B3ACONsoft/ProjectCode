package com.example.analembargo.posttocloud;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //this is url i'm posting data to
    private static final String postURL = "http://capstone1.netai.net/devDave/postDemo.php";
    //private static final String postURL = "http://172.20.23.100/postDemo.php";

    //these are the parameters for the select commmand
    private static final Map<String, String> selectValues;
    static
    {
        selectValues = new HashMap<String, String>();
        {
            selectValues.put("command", "SELECT");
            selectValues.put("options", "ALL");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
        This takes the values from the text fields, and packs them up as
        key-value pairs in a HashMap.

        It then performs an http Post to insert the key-value pairs
     */
    public void insertDemo(View v)
    {

        HashMap<String, String> insertValues = new HashMap<String, String>();
        ////these are the parameters for the insert command
        insertValues.put("command", "INSERT");
        insertValues.put("table", "coaches");
        insertValues.put("id",
                ((EditText)findViewById(R.id.tbId)).getText().toString());
        insertValues.put("firstname",
                ((EditText)findViewById(R.id.tbFirstName)).getText().toString());
        insertValues.put("lastname",
                ((EditText)findViewById(R.id.tbLastName)).getText().toString());
        insertValues.put("phone",
                ((EditText)findViewById(R.id.tbPhone)).getText().toString());
        insertValues.put("email",
                ((EditText)findViewById(R.id.tbEmail)).getText().toString());

        new postRequestTask().execute(parseParams((HashMap<String, String>)insertValues));
    }

    /*
        This performs a select * from coaches
     */
    public void selectDemo(View v)
    {
        new postRequestTask().execute(parseParams((HashMap<String, String>)selectValues));
    }

    /*
        updates the results text field
     */
    public void updateTextField(String update) {
        EditText editText = (EditText)findViewById(R.id.tbResult);
        editText.setText("");
        editText.setText(update);
    }


    /*
        This formats the key-values pairs correctly for the
        Post request in the format:
        key1=value1&key2=value2 etc etc

        Post requests key- value pair must be separated by  "&"
     */
    private String parseParams(HashMap<String, String> paramsMap)
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
                updateTextField(e.getMessage());
            }
        }
        return paramsBuilder.toString();
    }



    /*
        This is function fires a thread which makes HTTP POST Request
        and gets the response, and updates the result text area with it.

     */
    private class postRequestTask extends AsyncTask<String, Void, String> {

        //these are the http connection objects
        private URL url;
        private HttpURLConnection conn;

        @Override
        protected String doInBackground(String ...params) {
            return postRequest(postURL, params[0]);
        }

        //make a post request
        public String postRequest(String url, String params){
            StringBuilder responseBuffer = new StringBuilder();
            try
            {
                byte[] bytes = params.getBytes();   //convert the data to bytes

                this.url = new URL(url);
                conn = (HttpURLConnection) this.url.openConnection();   //init the connection objects
                //conn.setDoInput(true);
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

            } catch (IOException e) {
                updateTextField(e.getMessage());
            } finally {
                conn.disconnect();
            }
            return responseBuffer.toString();                                                       //pass the response to onPostExecute()
        }

        @Override
        protected  void onPostExecute(String result) {
            super.onPostExecute(result);
            updateTextField(result);                                                                //show result
        }
    }
}
