package devops.colby.cheqit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Button accountsButton = findViewById(R.id.button_settings_accounts);

        accountsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent detailIntent = new Intent(context,ListAccounts.class);
                startActivity(detailIntent);
            }
        });

        final Button addButton = findViewById(R.id.button_settings_add);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent detailIntent = new Intent(context,AddAccountActivity.class);
                startActivity(detailIntent);
            }
        });

        final Button clearAllButton = findViewById(R.id.button_settings_clear);

        clearAllButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO Add a "ARE YOU SURE?" confirmation
                deleteFile("history");
            }
        });
    }
}
