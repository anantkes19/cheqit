package devops.colby.cheqit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class AccountAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        final Button submitButton = findViewById(R.id.settings_add_button_submit);
        final EditText nameText = findViewById(R.id.settings_add_nameText);
        final EditText amountText = findViewById(R.id.settings_add_amountText);
        final EditText commentText = findViewById(R.id.settings_add_commentText);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                JsonHandler<Account> handlerAccount = (JsonHandler)getApplication();
                final ArrayList<Account> accountList = handlerAccount.getJSONObjects("accounts", Account.class);

                //Checking to make sure no other account has the same name to prevent confusion
                for(int i = 0; i < accountList.size(); i++) {
                    if(accountList.get(i).getName().toLowerCase().equals(nameText.getText().toString().toLowerCase())) {
                        Toast.makeText(getApplicationContext(), "An Account already has that name!", Toast.LENGTH_LONG).show();
                        return;
                    }

                }

                Account newAccount = new Account();


                newAccount.setName(nameText.getText().toString());
                newAccount.setAmount(Double.parseDouble(amountText.getText().toString()));
                newAccount.setComment(commentText.getText().toString());
                newAccount.setTimesUsed(0);
                newAccount.setTotalSpent(0.0);



                JsonHandler<Account> handler = (JsonHandler)getApplication();

                ArrayList<Account> objectList = handler.getJSONObjects("accounts", Account.class);

                objectList.add(newAccount);

                try {
                    handler.setJSONObjects(objectList, "accounts");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //End current activity
                Toast.makeText(getApplicationContext(), "Account Added", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
