package devops.colby.cheqit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

public class AccountList extends AppCompatActivity {
    private void loadAccounts() {
        JsonHandler<Account> handler = (JsonHandler)getApplication();
        final ArrayList<Account> accountList = handler.getJSONObjects("accounts", Account.class);

        final ListView accounts = findViewById(R.id.account_list_view);
        //System.out.println("Loaded Accounts!");
        //System.out.println(accountList);

        AccountAdapter adapter = new AccountAdapter(this, accountList);
        accounts.setAdapter(adapter);

        final Context context = this;
        accounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Account selectedAccount = accountList.get(position);

                Intent detailIntent = new Intent(context, AccountDetailActivity.class);
                JSONObject accountJson = selectedAccount.getJSONObject();
                detailIntent.putExtra("id",id);
                detailIntent.putExtra("object", accountJson.toString()); //Put the whole account into the next view

                startActivityForResult(detailIntent,0);
            }

        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_accounts);
        loadAccounts();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        loadAccounts(); //Reload accounts after being edited
    }
}
