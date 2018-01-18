package devops.colby.cheqit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final Button submitButton = findViewById(R.id.button_submit);
        final EditText nameText = findViewById(R.id.nameText);
        final EditText amountText = findViewById(R.id.amountText);
        final EditText timeText = findViewById(R.id.timeText);
        final EditText commentText = findViewById(R.id.commentText);
        final Context context = this;
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Transaction newTransaction = new Transaction();
                newTransaction.name = nameText.getText().toString();
                newTransaction.amount = amountText.getText().toString();
                newTransaction.time = timeText.getText().toString();
                newTransaction.comment = commentText.getText().toString();

                JsonHandler handler = (JsonHandler)getApplication();
                try {
                    handler.setTransaction(newTransaction, context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finishActivity(0);
            }
        });
    }
}
