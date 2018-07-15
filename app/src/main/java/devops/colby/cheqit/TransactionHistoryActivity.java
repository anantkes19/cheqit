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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class TransactionHistoryActivity extends AppCompatActivity {
    SearchView searchView;
    TextView title;
    Context context = this;
    ArrayList<Transaction> transactionList;

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
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
    }



    private void loadHistory(String searchTerm) {
        searchView = findViewById(R.id.past_search);
        title = findViewById(R.id.past_title);


        JsonHandler<Transaction> handler = (JsonHandler)getApplication();
        transactionList = handler.getJSONObjects("history", Transaction.class);

        final ListView transactions = findViewById(R.id.past_transactions_list_view);

        //We want most recent (date wise) on top
        Collections.sort(transactionList, new Comparator<Transaction>() {
            public int compare(Transaction o1, Transaction o2) {
                if (o1.getDateTime() == null || o2.getDateTime() == null)
                    return 0;
                return o2.getDateTime().compareTo(o1.getDateTime());
            }
        });

        if(!Objects.equals(searchTerm,"")) {
            ArrayList<Transaction> newTransactionList = new ArrayList<>();
            for(int i = 0; i < transactionList.size();i++) {
                //Creating a list to search through of all information
                ArrayList<String> transactionInfo = new ArrayList<>();
                transactionInfo.add(transactionList.get(i).getName());
                transactionInfo.add(transactionList.get(i).getComment());
                transactionInfo.add(transactionList.get(i).getLocation());
                transactionInfo.add(transactionList.get(i).getAccount());
                for(int j = 0; j < transactionInfo.size(); j++) {
                    if(transactionInfo.get(j).contains(searchTerm)) {
                        newTransactionList.add(transactionList.get(i));
                    }
                }
            }

            transactionList = newTransactionList;

        }

        TransactionAdapter transaction = new TransactionAdapter(this, transactionList);
        transactions.setAdapter(transaction);

        searchView.setOnSearchClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                title.setVisibility(View.INVISIBLE);
            }

        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                loadHistory("");
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(context,"Searched for: "+s,Toast.LENGTH_SHORT).show();
                loadHistory(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        final Context context = this;
        transactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Transaction selectedTransaction = transactionList.get(position);

                Intent detailIntent = new Intent(context, TransactionDetailActivity.class);
                JSONObject transactionJSON = selectedTransaction.getJSONObject();

                detailIntent.putExtra("object", transactionJSON.toString()); //Put the whole account into the next view

                startActivityForResult(detailIntent,0);
            }

        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        bottomMenu();

        loadHistory("");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        loadHistory(""); //Reload transactions after being edited
    }
}
