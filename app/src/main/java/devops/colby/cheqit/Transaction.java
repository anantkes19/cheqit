package devops.colby.cheqit;


import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Austin Nantkes on 1/13/2018.
 */

public class Transaction {
    public String name; //Name optional
    public String time; //Date time?
    public String comment;
    public String amount;
    //Something to store an image
    public String location;
    public Integer latitude; //lat/long should be saved as array
    public Integer longitude;

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("name", name);
            obj.put("time", time);
            obj.put("amount", amount);
            obj.put("latitude", latitude);
            obj.put("longitude", longitude);
            obj.put("comment", comment);
            obj.put("location", location);
        }
        catch (JSONException e) {
            System.out.println("Error converting JSON to Object");
        }

        return obj;
    }


}
