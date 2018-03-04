package devops.colby.cheqit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class AccountDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        final long id = this.getIntent().getExtras().getLong("id");
        JSONObject jsonString = null;
        try {
            jsonString = new JSONObject(this.getIntent().getExtras().getString("object"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final Account account = new Account();
        try {
            account.setAttributes(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Button submitButton = findViewById(R.id.settings_add_button_submit);
        final Button deleteButton = findViewById(R.id.settings_add_button_delete);
        final EditText nameText = findViewById(R.id.settings_add_nameText);
        final EditText amountText = findViewById(R.id.settings_add_amountText);
        final EditText commentText = findViewById(R.id.settings_add_commentText);

        nameText.setText(account.getName());
        amountText.setText(String.valueOf(account.getAmount()));
        commentText.setText(account.getComment());


        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                account.setName(nameText.getText().toString());
                account.setAmount(Double.parseDouble(amountText.getText().toString()));
                account.setComment(commentText.getText().toString());

                JsonHandler<Account> handler = (JsonHandler)getApplication();

                ArrayList<Account> objectList = handler.getJSONObjects("accounts", Account.class);
                objectList.remove((int)id);
                objectList.add(account);
                try {
                    handler.setJSONObjects(objectList,"accounts");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //End current activity
                Toast.makeText(getApplicationContext(), "Edits Saved", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                setResult(RESULT_OK,intent );
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /*AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage("Are you sure you wish to delete this account?");
                builder.setCancelable(false);
                final boolean[] confirmation = {false};
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        return;
                    }
                });
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        confirmation[0] = true;
                    }
                });

                //Actually fix this with dialogs.

                AlertDialog dialog = builder.create();
                dialog.show();*/

                JsonHandler<Account> handler = (JsonHandler)getApplication();

                ArrayList<Account> objectList = handler.getJSONObjects("accounts", Account.class);
                objectList.remove((int)id);
                try {
                    handler.setJSONObjects(objectList,"accounts");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //End current activity
                Toast.makeText(getApplicationContext(), "Account Deleted", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                setResult(RESULT_OK,intent );
                finish();
            }
        });
    }
}
