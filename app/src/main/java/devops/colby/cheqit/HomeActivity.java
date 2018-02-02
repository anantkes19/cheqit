package devops.colby.cheqit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Button historyButton = findViewById(R.id.button_history);

        historyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent detailIntent = new Intent(context,HistoryActivity.class);
                startActivity(detailIntent);
            }
        });

        final Button addButton = findViewById(R.id.button_add);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent detailIntent = new Intent(context,AddActivity.class);
                startActivity(detailIntent);
            }
        });

        final Button dataButton = findViewById(R.id.button_data);

        dataButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent detailIntent = new Intent(context,DataActivity.class);
                startActivity(detailIntent);
            }
        });

        final Button settingsButton = findViewById(R.id.button_settings);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent detailIntent = new Intent(context,SettingsActivity.class);
                startActivity(detailIntent);
            }
        });

    }

}
