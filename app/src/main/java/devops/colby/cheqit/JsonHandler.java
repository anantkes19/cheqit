package devops.colby.cheqit;

import android.app.Application;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bybsn on 1/17/2018.
 */

public class JsonHandler extends Application {
    private ArrayList<Transaction> transactionList;



    public ArrayList<Transaction> getTransactions(String filename){
        final ArrayList<Transaction> transactionList = new ArrayList<>();

        try {
            // Load data
            String jsonString = loadJson(filename);

            JSONArray transactions = new JSONArray(jsonString);

            // Get Transaction objects from data
            for(int i = 0; i < transactions.length(); i++){
                Transaction transaction = new Transaction();

                transaction.name = transactions.getJSONObject(i).getString("name");
                transaction.time = transactions.getJSONObject(i).getString("time");
                transaction.comment = transactions.getJSONObject(i).getString("comment");
                transaction.amount = transactions.getJSONObject(i).getString("amount");

                transactionList.add(transaction);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return transactionList;
    }

    public void setTransaction(Transaction transaction) throws IOException {
        ArrayList<Transaction> transactionList = getTransactions("history");

        transactionList.add(transaction);
        JSONArray jsonArray = new JSONArray();


        for(int i=0; i <transactionList.size();i++) {

            jsonArray.put(transactionList.get(i).getJSONObject());
        }

        String jsonString = jsonArray.toString();
        FileOutputStream fos = openFileOutput("history", Context.MODE_PRIVATE);
        fos.write(jsonString.getBytes());
        fos.close();

    }

    private String loadJson(String filename) throws IOException {
        String json;
        FileInputStream fis = openFileInput(filename);
        int size = fis.available();
        byte[] buffer = new byte[size];
        fis.read(buffer);
        json = new String(buffer, "UTF-8");

        return json;
    }


}
