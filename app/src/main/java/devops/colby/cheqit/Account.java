package devops.colby.cheqit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bybsn on 1/18/2018.
 */

public class Account implements userObject{
    private String name;
    private String comment;
    private double amount; //Current amount in Account
    private Integer timesUsed;
    private double totalSpent;


    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String newComment) {
        comment = newComment;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double newAmount) {
        amount = newAmount;
    }

    public Integer getTimesUsed() {
        return timesUsed;
    }

    public void setTimesUsed(Integer newTimesUsed) {
        timesUsed = newTimesUsed;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double newTotalSpent) {
        totalSpent = newTotalSpent;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("name", name);
            obj.put("amount", amount);
            obj.put("timesUsed", timesUsed);
            obj.put("totalSpent", totalSpent);
            obj.put("comment", comment);
        }
        catch (JSONException e) {
            System.out.println("Error converting JSON to Object");
        }

        return obj;
    }

    public void setAttributes(JSONObject jsonString) throws JSONException {
        this.setName(jsonString.getString("name"));
        this.setComment(jsonString.getString("comment"));
        this.setAmount(jsonString.getDouble("amount"));
        this.setTotalSpent(jsonString.getDouble("totalSpent"));
        this.setTimesUsed((jsonString.getInt("timesUsed")));

    }
}
