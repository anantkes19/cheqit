package devops.colby.cheqit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {

    final Context context = this;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            switch (item.getItemId()) {
                case R.id.navigation_add:
                    Intent detailIntent = new Intent(context, TransactionAddActivity.class);
                    detailIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(detailIntent);
                    break;
                case R.id.navigation_overview:
                    detailIntent = new Intent(context, MainScreen.class);
                    detailIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(detailIntent);
                    break;
                case R.id.navigation_history:
                    detailIntent = new Intent(context, TransactionHistoryActivity.class);
                    detailIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(detailIntent);
                    break;
                case R.id.navigation_data:
                    detailIntent = new Intent(context, DataActivity.class);
                    detailIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(detailIntent);
                    break;
            }
        }, 100);



        return false;
    };
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
    protected void bottomMenu() {
        try {
            FileInputStream fis = openFileInput("accounts"); //Never used, but if accounts file doesnt exist,
            //Exception will help fix that
            JsonHandler<Account> handlerAccount = (JsonHandler) getApplication();
            final ArrayList<Account> accountList = handlerAccount.getJSONObjects("accounts", Account.class);
            if (accountList.size() == 0) {
                Toast.makeText(this, "Please create an account", Toast.LENGTH_LONG).show();
                Intent detailIntent = new Intent(context, AccountAddActivity.class);
                detailIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(detailIntent);
            }

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Please create an account", Toast.LENGTH_LONG).show();
            Intent detailIntent = new Intent(context, AccountAddActivity.class);
            detailIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(detailIntent);
        }




        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        bottomMenu();

    }
}


