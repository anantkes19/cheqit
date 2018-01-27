package devops.colby.cheqit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private void loadHistory() {
        JsonHandler<Transaction> handler = (JsonHandler)getApplication();
        final ArrayList<Transaction> transactionList = handler.getJSONObjects("history", Transaction.class);

        final ListView transactions = findViewById(R.id.past_transactions_list_view);
        System.out.println("Loaded history!");
        System.out.println(transactionList);

        TransactionAdapter transaction = new TransactionAdapter(this, transactionList);
        transactions.setAdapter(transaction);

        final Context context = this;
        transactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Transaction selectedTransaction = transactionList.get(position);

                Intent detailIntent = new Intent(context, TransactionDetailActivity.class);
                JSONObject transactionJSON = selectedTransaction.getJSONObject();
                detailIntent.putExtra("id",id);
                detailIntent.putExtra("object", transactionJSON.toString()); //Put the whole account into the next view

                startActivityForResult(detailIntent,0);
            }

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        loadHistory();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        loadHistory(); //Reload transactions after being edited
    }
}
