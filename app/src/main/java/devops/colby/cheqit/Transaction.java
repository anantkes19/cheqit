package devops.colby.cheqit;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Austin Nantkes on 1/13/2018.
 */

public class Transaction implements userObject{
    private String name;
    private String time;
    private String date;
    private String comment;
    private double amount;
    private String accountUsed;
    private String location;
    private Double latitude; //lat/long could be saved as array
    private Double longitude;
    private boolean expense;
    private String photoUri;
    private String id;

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double newAmount) {
        amount = newAmount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String newLocation) {
        location = newLocation;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double newLatitude) {
        latitude = newLatitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double newLongitude) {
        longitude = newLongitude;
    }

    public String getAccount() { //If this is causing problems, just save the name of it instead. Much easier, but it would be nice to be able to click through from the transaction right to the account
        return accountUsed;
    }

    public void setAccount(String newAccount) {
        accountUsed = newAccount;
    }

    public boolean getIsExpense() {
        return expense;
    }

    public void setIsExpense(boolean newExpense) {
        expense = newExpense;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String newPhotoUri) {
        photoUri = newPhotoUri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String newDate) {
        date = newDate;
    }

    public Calendar getDateTime() {
        String time[] = this.getTime().split(":");

        String[] dateString = this.getDate().split(":");
        Calendar dateTime = Calendar.getInstance();
        dateTime.set(Integer.parseInt(dateString[0]),Integer.parseInt(dateString[1]),Integer.parseInt(dateString[2]),Integer.parseInt(time[0]),Integer.parseInt(time[1]));

        return dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String newId) {
        id = newId;
    }

    public void setAttributes(JSONObject jsonString) throws JSONException {
        this.setName(jsonString.getString("name"));
        this.setTime(jsonString.getString("time"));
        this.setComment(jsonString.getString("comment"));
        this.setAmount(jsonString.getDouble("amount"));
        this.setLatitude(jsonString.getDouble("latitude"));
        this.setLongitude(jsonString.getDouble("longitude"));
        this.setLocation(jsonString.getString("location"));
        this.setAccount(jsonString.getString("account"));
        this.setIsExpense(jsonString.getBoolean("expense"));
        this.setPhotoUri(jsonString.getString("photo"));
        this.setDate(jsonString.getString("date"));
        this.setId(jsonString.getString("id"));
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
            obj.put("account", accountUsed);
            obj.put("expense", expense);
            obj.put("photo",photoUri);
            obj.put("date",date);
            obj.put("id",id);

        }
        catch (JSONException e) {
            System.out.println("Error converting JSON to Object");
        }

        return obj;
    }



}
