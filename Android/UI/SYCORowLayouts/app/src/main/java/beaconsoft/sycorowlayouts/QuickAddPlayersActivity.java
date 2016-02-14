package beaconsoft.sycorowlayouts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class QuickAddPlayersActivity extends AppCompatActivity {

    private static final String ADMIN_KEY =  "beaconsoft.sycorowlayouts.ADMIN_NAME_PASSED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add_players);

        TextView textViewAdminEmail = (TextView)findViewById(R.id.textViewQAAdminEmail);
        Intent intent = getIntent();
        String email = intent.getStringExtra(ADMIN_KEY);
        textViewAdminEmail.setText(email);

    }
}
