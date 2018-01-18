package devops.colby.cheqit;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.io.IOException;
import java.util.Calendar;


public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final Button submitButton = findViewById(R.id.button_submit);
        final EditText nameText = findViewById(R.id.nameText);
        final EditText amountText = findViewById(R.id.amountText);
        final TimePicker timePicker = findViewById(R.id.timeText);
        final EditText commentText = findViewById(R.id.commentText);

        final Calendar calendar = Calendar.getInstance();
        timePicker.setHour(calendar.HOUR_OF_DAY);
        timePicker.setMinute(calendar.MINUTE);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Transaction newTransaction = new Transaction();
                newTransaction.name = nameText.getText().toString();
                newTransaction.amount = amountText.getText().toString();
                newTransaction.time = String.valueOf(timePicker.getHour()) + ":" +String.valueOf(timePicker.getMinute());
                newTransaction.comment = commentText.getText().toString();

                JsonHandler handler = (JsonHandler)getApplication();
                try {
                    handler.setTransaction(newTransaction);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finishActivity(0);
            }
        });
    }
}
