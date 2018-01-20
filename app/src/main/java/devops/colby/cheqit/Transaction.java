package devops.colby.cheqit;


import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Austin Nantkes on 1/13/2018.
 */

public class Transaction implements userObject {
    private String name; //Name optional
    private String time; //Date time?
    private String comment;
    private String amount;
    //Something to store an image
    private Account accountUsed; //How to store this data as json?
    private String location;
    private Integer latitude; //lat/long should be saved as array
    private Integer longitude;

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String newTime) {
        time = newTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String newComment) {
        comment = newComment;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String newAmount) {
        amount = newAmount;
    }

    public void setAttributes(JSONObject jsonString) throws JSONException {
        this.setName(jsonString.getString("name"));
        this.setTime(jsonString.getString("time"));
        this.setComment(jsonString.getString("comment"));
        this.setAmount(jsonString.getString("amount"));
    }

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
