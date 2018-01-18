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



    public ArrayList<Transaction> getTransactions(String filename, String jsonName, Context context){
        final ArrayList<Transaction> transactionList = new ArrayList<>();

        try {
            // Load data
            String jsonString = loadJsonFromAsset(filename, context);

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
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactionList;
    }

    public void setTransaction(Transaction transaction, Context context) throws IOException {
        ArrayList<Transaction> transactionList = getTransactions("history.json","history",context);

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

    private String loadJsonFromAsset(String filename, Context context) throws IOException {
        String json;
        FileInputStream fis = openFileInput("history");
        int size = fis.available();
        byte[] buffer = new byte[size];
        fis.read(buffer);
        json = new String(buffer, "UTF-8");

        return json;
    }


}
