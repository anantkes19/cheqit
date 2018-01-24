package devops.colby.cheqit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class AddAccountActivity extends AppCompatActivity {

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
