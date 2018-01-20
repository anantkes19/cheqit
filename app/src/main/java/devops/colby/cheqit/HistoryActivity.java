package devops.colby.cheqit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        JsonHandler<Transaction> handler = (JsonHandler)getApplication();
        ArrayList<Transaction> transactionList = handler.getJSONObjects("history", Transaction.class);

        ListView pastTransactions = findViewById(R.id.past_transactions_list_view);



        TransactionAdapter adapter = new TransactionAdapter(this, transactionList);
        pastTransactions.setAdapter(adapter);
    }
}
