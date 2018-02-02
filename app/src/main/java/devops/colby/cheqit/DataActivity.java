package devops.colby.cheqit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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

        final TextView mostSpentName = findViewById(R.id.data_most_spent_name);
        final TextView mostSpentAmount = findViewById(R.id.data_most_spent_amount);
        final TextView mostUsedName = findViewById(R.id.data_most_used_name);
        final TextView mostUsedAmount = findViewById(R.id.data_most_used_amount);
        final TextView mostExpensiveName = findViewById(R.id.data_most_expensive_name);
        final TextView mostExpensiveAmount = findViewById(R.id.data_most_expensive_amount);




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
                if(o1.getAmount() < o2.getAmount()) {
                    return -1;
                } else if(o1.getAmount() > o2.getAmount()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        Collections.reverse(transactionList);
        Collections.sort(accountList, new Comparator<Account>() {
            public int compare(Account o1, Account o2) {
                if(o1.getTotalSpent() < o2.getTotalSpent()) {
                    return -1;
                } else if(o1.getTotalSpent() > o2.getTotalSpent()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        Collections.reverse(accountList);
        Account mostSpent = accountList.get(0);

        Collections.sort(accountList, new Comparator<Account>() {
            public int compare(Account o1, Account o2) {
                if(o1.getTimesUsed() < o2.getTimesUsed()) {
                    return -1;
                } else if(o1.getTimesUsed() > o2.getTimesUsed()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        Collections.reverse(accountList);

        Account mostUsed = accountList.get(0);
        Transaction mostExpensive = transactionList.get(0);

        mostSpentName.setText(mostSpent.getName());
        mostSpentAmount.setText(String.valueOf(mostSpent.getTotalSpent()));
        mostUsedName.setText(mostUsed.getName());
        mostUsedAmount.setText(String.valueOf(mostUsed.getTimesUsed()));
        mostExpensiveName.setText(mostExpensive.getName());
        mostExpensiveAmount.setText(String.valueOf(mostExpensive.getAmount()));

        //Create functions for showing last 30,60,90 days of data, like in a graph. For loop to find those transactions.
    }
}
