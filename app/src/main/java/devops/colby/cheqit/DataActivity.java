package devops.colby.cheqit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class DataActivity extends AppCompatActivity {
    ArrayList<Transaction> transactionList;
    ArrayList<Account> accountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        JsonHandler<Transaction> transactionHandler = (JsonHandler)getApplication();
        transactionList = transactionHandler.getJSONObjects("history", Transaction.class);

        JsonHandler<Account> accountHandler = (JsonHandler)getApplication();
        accountList = accountHandler.getJSONObjects("accounts", Account.class);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -30); //Past 30 Days
        Date past30 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -30); //Past 60 Days
        Date past60 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -30); //Past 90 Days
        Date past90 = calendar.getTime();

        Collections.sort(transactionList, new Comparator<Transaction>() {
            public int compare(Transaction o1, Transaction o2) {
                if(o1.getAmount() > o2.getAmount()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        Collections.sort(accountList, new Comparator<Account>() {
            public int compare(Account o1, Account o2) {
                if(o1.getTotalSpent() > o2.getTotalSpent()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        Collections.sort(accountList, new Comparator<Account>() {
            public int compare(Account o1, Account o2) {
                if(o1.getTimesUsed() > o2.getTimesUsed()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        Account mostSpent = accountList.get(0);
        Account mostUsed = accountList.get(0);
        Transaction mostExpensive = transactionList.get(0);


        //Create functions for showing last 30,60,90 days of data, like in a graph. For loop to find those transactions.
    }
}
