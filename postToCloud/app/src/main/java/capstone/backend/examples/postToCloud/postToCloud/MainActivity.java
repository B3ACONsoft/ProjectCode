package capstone.backend.examples.postToCloud.postToCloud;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements CommandConstants, ConnectionConstants, BaconUpdate {

    //these are the parameters for the select commmand
    private static final HashMap<String, String> selectValues;
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
        insertValues.put(ID,
                ((EditText)findViewById(R.id.tbId)).getText().toString());
        insertValues.put(FIRSTNAME,
                ((EditText)findViewById(R.id.tbFirstName)).getText().toString());
        insertValues.put(LASTNAME,
                ((EditText)findViewById(R.id.tbLastName)).getText().toString());
        insertValues.put(PHONE,
                ((EditText)findViewById(R.id.tbPhone)).getText().toString());
        insertValues.put(EMAIL,
                ((EditText)findViewById(R.id.tbEmail)).getText().toString());

        try
        {
            new ConnectToBacon(INSERT, COACHES_TABLE, insertValues, this);
        }catch(Exception e) {
            Toast.makeText(this.getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT);
        }

    }

    /*
        This performs a select * from coaches
     */
    public void selectDemo(View v)
    {
        try {
            new ConnectToBacon(INSERT, COACHES_TABLE, selectValues, this);
        }catch(Exception e) {
            Toast.makeText(this.getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    //updates the result text field
    public void updateBacon(String update)
    {
        EditText editText = (EditText)findViewById(R.id.tbResult);
        editText.setText("");
        editText.setText(update);
    }


}
