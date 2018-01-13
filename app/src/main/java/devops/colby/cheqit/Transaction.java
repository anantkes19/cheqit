package devops.colby.cheqit;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Austin Nantkes on 1/13/2018.
 */

public class Transaction {
    public String name; //Name optional
    public String time; //Date time?
    public String comment;
    public String amount;
    //Something to store an image
    public String Location;
    public Integer Latitude;
    public Integer Longitude;

    public static ArrayList<Transaction> getTransactionsFromJSON(String filename, Context context){
        final ArrayList<Transaction> transactionList = new ArrayList<>();

        try {
            // Load data
            String jsonString = loadJsonFromAsset("history.json", context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray recipes = json.getJSONArray("history");

            // Get Recipe objects from data
            for(int i = 0; i < recipes.length(); i++){
                Transaction transaction = new Transaction();

                transaction.name = recipes.getJSONObject(i).getString("name");
                transaction.time = recipes.getJSONObject(i).getString("time");
                transaction.comment = recipes.getJSONObject(i).getString("comment");
                transaction.amount = recipes.getJSONObject(i).getString("amount");

                transactionList.add(transaction);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return transactionList;
    }

    private static String loadJsonFromAsset(String filename, Context context) {
        String json = null;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }
}
