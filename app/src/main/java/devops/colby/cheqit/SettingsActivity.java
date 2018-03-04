package devops.colby.cheqit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SettingsActivity extends AppCompatActivity {
    final Context context = this;
    FileWriter mFileWriter;
    ArrayList<Transaction> transactionList;
    String[] data;
    private void createCSV() throws IOException {
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "transactionData.csv";
        String filePath = baseDir + File.separator + fileName;
        File f = new File(filePath );
        CSVWriter writer;
// File exist
        if(f.exists() && !f.isDirectory()){
            mFileWriter = new FileWriter(filePath , false);
            writer = new CSVWriter(mFileWriter);
        }
        else {
            writer = new CSVWriter(new FileWriter(filePath));
        }

        JsonHandler<Transaction> handler = (JsonHandler)getApplication();
        transactionList = handler.getJSONObjects("history", Transaction.class);

        //We want most recent (date wise) on top
        Collections.sort(transactionList, new Comparator<Transaction>() {
            public int compare(Transaction o1, Transaction o2) {
                if (o1.getDateTime() == null || o2.getDateTime() == null)
                    return 0;
                return o2.getDateTime().compareTo(o1.getDateTime());
            }
        });
        data = new String[]{"Name", "Location", "Amount", "Expense", "Date", "Time","Comment"};
        writer.writeNext(data);
        for(int i = 0; i < transactionList.size(); i++) {
            data = new String[]{transactionList.get(i).getName(),
                    transactionList.get(i).getLocation(),
                    String.valueOf(transactionList.get(i).getAmount()),
                    String.valueOf(transactionList.get(i).getIsExpense()),
                    transactionList.get(i).getDate(),
                    transactionList.get(i).getTime(),
                    transactionList.get(i).getComment()};
            writer.writeNext(data);

        }




        writer.close();
        Toast.makeText(this, "CSV Created: transactionData.csv", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Button accountsButton = findViewById(R.id.button_settings_accounts);

        accountsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent detailIntent = new Intent(context,AccountList.class);
                startActivity(detailIntent);
            }
        });

        final Button addButton = findViewById(R.id.button_settings_add);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent detailIntent = new Intent(context,AccountAddActivity.class);
                startActivity(detailIntent);
            }
        });

        final Button exportButton = findViewById(R.id.button_settings_export);

        exportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    createCSV();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button clearAllButton = findViewById(R.id.button_settings_clear);

        clearAllButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO Add a "ARE YOU SURE?" confirmation
                deleteFile("history");
            }
        });
    }
}
