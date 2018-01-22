package devops.colby.cheqit;

import android.app.Application;
import android.content.Context;

import org.json.JSONArray;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bybsn on 1/17/2018.
 */

public class JsonHandler<T extends userObject> extends Application {

    public ArrayList<T> getJSONObjects(String filename, Class<T> classToLoad){
        final ArrayList<T> objectList = new ArrayList<>();

        try {
            // Load data
            String jsonString = loadJson(filename);

            JSONArray jsonArray = new JSONArray(jsonString);

            // Get Transaction objects from data
            for(int i = 0; i < jsonArray.length(); i++){

                T jsonObject = classToLoad.newInstance();
                jsonObject.setAttributes(jsonArray.getJSONObject(i));
                objectList.add(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return objectList;
    }

    public void setJSONObjects(ArrayList<T> newObjects, String filename) throws IOException {

        JSONArray jsonArray = new JSONArray();


        for(int i=0; i <newObjects.size();i++) {

            jsonArray.put(newObjects.get(i).getJSONObject());
        }

        String jsonString = jsonArray.toString();
        FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
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

    public void deleteAllTransactions() {
        deleteFile("history");
    }


}
