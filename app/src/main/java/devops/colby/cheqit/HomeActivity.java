package devops.colby.cheqit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Button historyButton = findViewById(R.id.button_history);


        try {
            FileInputStream fis = openFileInput("accounts");
            JsonHandler<Account> handlerAccount = (JsonHandler)getApplication();
            final ArrayList<Account> accountList = handlerAccount.getJSONObjects("accounts", Account.class);
            if(accountList.size()==0) {
                Toast.makeText(this, "Please create an account", Toast.LENGTH_LONG).show();
                Intent detailIntent = new Intent(context,AccountAddActivity.class);
                startActivity(detailIntent);
            }

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Please create an account", Toast.LENGTH_LONG).show();
            Intent detailIntent = new Intent(context,AccountAddActivity.class);
            startActivity(detailIntent);
        }

        historyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent detailIntent = new Intent(context,TransactionHistoryActivity.class);
                startActivity(detailIntent);
            }
        });

        final Button addButton = findViewById(R.id.button_add);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent detailIntent = new Intent(context,TransactionAddActivity.class);
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
