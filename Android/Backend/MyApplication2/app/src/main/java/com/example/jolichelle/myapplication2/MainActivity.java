package com.example.jolichelle.myapplication2;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends Activity {

    private static final BasicNameValuePair insertCommand = new BasicNameValuePair("command", "INSERT");
    private static final BasicNameValuePair selectCommand = new BasicNameValuePair("select", "SELECT");
    private static final BasicNameValuePair selectOption = new BasicNameValuePair("option", "ALL");

    String id;
    String firstname;
    String lastname;
    String phone;
    String email;

    private EditText editTextID,editTextFName, editTextLName, editTextPhone, editTextEmail;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextID = (EditText)findViewById(R.id.editText);
        editTextFName = (EditText) findViewById(R.id.editTextFName);
        editTextLName = (EditText) findViewById(R.id.editTextLName);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

      /*  buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonView = (Button) findViewById(R.id.buttonView);

        //Setting listeners to button
        buttonAdd.setOnClickListener(this);
        buttonView.setOnClickListener(this);*/


        button = (Button) findViewById(R.id.buttonAdd);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                id = editTextID.getText().toString();
                firstname = editTextFName.getText().toString();
                lastname = editTextLName.getText().toString();
                phone = editTextPhone.getText().toString();
                email = editTextEmail.getText().toString();
                new SummaryAsyncTask().execute();
            }
        });
    }

    private void upDateResponseText(String update) {
        EditText text = (EditText)findViewById(R.id.tbResponse);
        text.setText(" ");
        text.setText(update);
    }

    @TargetApi(19)
    class SummaryAsyncTask extends AsyncTask<String, Void, String> {

        private String postData(String firstname, String lastname, String phone,
                              String email) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://capstone1.netai.net/devLisa/postDemo.php");
            StringBuilder builder = new StringBuilder();

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
                nameValuePairs.add(insertCommand);
                nameValuePairs.add(new BasicNameValuePair("id", id));
                nameValuePairs.add(new BasicNameValuePair("firstname", firstname));
                nameValuePairs.add(new BasicNameValuePair("lastname", lastname));
                nameValuePairs.add(new BasicNameValuePair("phone", phone));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                //Thread.sleep(2000);

                try
                        (InputStream input = response.getEntity().getContent();
                        )
                {

                    int respLength = (int)response.getEntity().getContentLength();
                    byte[] byteBuffer = new byte[respLength];
                    input.read(byteBuffer, 0, respLength);

                    for(int i = 0; i < respLength; i++) {
                        builder.append((char)byteBuffer[i]);
                    }

                }
            }
            catch(Exception e)
            {
                Log.e("log_tag", "Error:  "+e.toString());
            }
            return builder.toString();
        }

        @Override
        protected String doInBackground(String... params) {
            return postData(firstname, lastname, phone, email);
        }

        @Override
        protected  void onPostExecute(String result) {
            super.onPostExecute(result);
            upDateResponseText(result);                                                                //show result
        }
    }
}

