package devops.colby.cheqit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private ListView pastTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        pastTransactions = (ListView) findViewById(R.id.past_transactions_list_view);

        final ArrayList<Transaction> transactionList = Transaction.getTransactionsFromJSON("history.json", this);
        TransactionAdapter adapter = new TransactionAdapter(this, transactionList);
        pastTransactions.setAdapter(adapter);
    }
}
