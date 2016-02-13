package beaconsoft.sycorowlayouts;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddPlayerActivity extends AppCompatActivity {

    private final static String ADMIN_NAME = "beaconsoft.sycorowlayouts.ADMIN_NAME_PASSED";
    private String email;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_add_player);

        TextView textViewAdminName = (TextView)findViewById(R.id.textViewLeaguesActivityPassedName);
        Intent intent = getIntent();
        email = intent.getStringExtra(ADMIN_NAME);
        textViewAdminName.setText(email);

    }
}
