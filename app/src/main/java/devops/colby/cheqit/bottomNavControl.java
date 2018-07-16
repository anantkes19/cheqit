package devops.colby.cheqit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class bottomNavControl {
    private Context context;
    private Activity activity;
    private int navId = 0;


    public bottomNavControl(Context context, Activity activity, int navId) {
        this.context = context;
        this.activity = activity;
        this.navId = navId;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            switch (item.getItemId()) {
                case R.id.navigation_add:
                    Intent detailIntent = new Intent(context, TransactionAddActivity.class);
                    detailIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    activity.startActivity(detailIntent);
                    break;
                case R.id.navigation_overview:
                    detailIntent = new Intent(context, MainScreen.class);
                    detailIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    activity.startActivity(detailIntent);
                    break;
                case R.id.navigation_history:
                    detailIntent = new Intent(context, TransactionHistoryActivity.class);
                    detailIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    activity.startActivity(detailIntent);
                    break;
                case R.id.navigation_data:
                    detailIntent = new Intent(context, DataActivity.class);
                    detailIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    activity.startActivity(detailIntent);
                    break;
            }
        }, 100);



        return false;
    };

    public void bottomMenu() {
        try {
            FileInputStream fis = context.openFileInput("accounts"); //Never used, but if accounts file doesnt exist,
            //Exception will help fix that
            JsonHandler<Account> handlerAccount = (JsonHandler) activity.getApplication();
            final ArrayList<Account> accountList = handlerAccount.getJSONObjects("accounts", Account.class);
            if (accountList.size() == 0) {
                Toast.makeText(context, "Please create an account", Toast.LENGTH_LONG).show();
                Intent detailIntent = new Intent(context, AccountAddActivity.class);
                detailIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(detailIntent);
            }

        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Please create an account", Toast.LENGTH_LONG).show();
            Intent detailIntent = new Intent(context, AccountAddActivity.class);
            detailIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(detailIntent);
        }




        BottomNavigationView navigation = (BottomNavigationView) activity.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(navId);
        menuItem.setChecked(true);
    }
}
