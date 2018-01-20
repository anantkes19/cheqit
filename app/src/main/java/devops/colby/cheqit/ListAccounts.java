package devops.colby.cheqit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ListAccounts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_accounts);

        JsonHandler<Account> handler = (JsonHandler)getApplication();
        ArrayList<Account> accountList = handler.getJSONObjects("accounts", Account.class);

        ListView accounts = findViewById(R.id.account_list_view);

        System.out.println(accountList);

        AccountAdapter adapter = new AccountAdapter(this, accountList);
        accounts.setAdapter(adapter);
    }
}
