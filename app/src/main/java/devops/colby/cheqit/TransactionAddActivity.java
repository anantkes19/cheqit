package devops.colby.cheqit;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class TransactionAddActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private final Context context = this;
    private final Activity activity = this;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private ImageCapture imageCapture;
    private ImageView image;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private double lat;
    private double lng;
    private boolean userAcknowledged = false;
    private String mCurrentPhotoPath = "";
    private boolean permissionsEnabled = false;

    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;


    Button takePhoto;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            image.setImageBitmap(imageCapture.getPhoto());
            mCurrentPhotoPath = imageCapture.getmCurrentPhotoPath();

        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);


            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode==225 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            permissionsEnabled = true;
            System.out.println("Permission Granted and used");
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                lat = mLastLocation.getLatitude();
                lng = mLastLocation.getLongitude();
            }
        } else {
            Toast.makeText(context, "Disabling Photos and Location Saving", Toast.LENGTH_LONG).show();
            takePhoto.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        return;
    }

    public void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        bottomNavControl bottomNav = new bottomNavControl(context, this, 1);
        bottomNav.bottomMenu();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        final Button submitButton = findViewById(R.id.button_submit);
        final EditText nameText = findViewById(R.id.nameText);
        final EditText amountText = findViewById(R.id.amountText);

        final EditText commentText = findViewById(R.id.commentText);
        final EditText locationText = findViewById(R.id.locationText);
        final Spinner accountUsed = findViewById(R.id.accountSelection);
        final RadioGroup expenseGroup = findViewById(R.id.radioGroup);
        takePhoto = findViewById(R.id.photo_button);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);


        //Special thanks to : https://www.journaldev.com/9976/android-date-time-picker-dialog
        //For the help with time and date pop up dialogs
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        String date = mDay + "-" + (mMonth + 1) + "-" + mYear;
        txtDate.setText(date);
        String time = mHour + ":" + mMinute;
        txtTime.setText(time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        /*final TimePicker timePicker = findViewById(R.id.timeText);
        final DatePicker datePicker = findViewById(R.id.dateText);*/



        image = findViewById(R.id.photo_image);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(context, ZoomedImageActivity.class);
                detailIntent.putExtra("photo", mCurrentPhotoPath);
                startActivity(detailIntent);
            }
        });


        //Bring up keyboard on activity start
        showInputMethod();

        //final boolean[] userAcknowledged = {false}; //Todo fix this
        JsonHandler<Account> handler = (JsonHandler) getApplication();

        final ArrayList<Account> accountList = handler.getJSONObjects("accounts", Account.class);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, accountList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        accountUsed.setAdapter(adapter);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imageCapture = new ImageCapture(activity, context);
                imageCapture.dispatchTakePictureIntent();


            }
        });

        //When submit transaction button is clicked
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Transaction newTransaction = new Transaction();


                //Setting attributes of transaction
                newTransaction.setName(nameText.getText().toString());
                newTransaction.setAmount(Double.parseDouble(amountText.getText().toString()));
                newTransaction.setTime(String.valueOf(mHour + ":" +mMinute));
                newTransaction.setComment(commentText.getText().toString());
                newTransaction.setPhotoUri(mCurrentPhotoPath);
                newTransaction.setLocation(locationText.getText().toString());
                newTransaction.setAccount(accountUsed.getSelectedItem().toString());
                newTransaction.setId(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

                //Setting Latitude Longitude based on permissions
                if(!permissionsEnabled) {
                    lat = 0.0;
                    lng = 0.0;
                }
                newTransaction.setLatitude(lat);
                newTransaction.setLongitude(lng);

                String dateString = mYear + ":" + mMonth + ":" + mDay;
                newTransaction.setDate(dateString);

                //System.out.println("Photo Path: "+mCurrentPhotoPath);

                RadioButton expenseButton = (RadioButton) findViewById(expenseGroup.getCheckedRadioButtonId());

                boolean expense = true;
                //System.out.println(expenseButton.getText());
                if(expenseButton.getText().equals("Income")) {
                    //System.out.println("Is Income");
                    expense = false;
                }
                //System.out.println(expense);
                newTransaction.setIsExpense(expense);




                //Check if account has enough money, subtract if it does. If it doesn't, cancel this addition (but save all info)
                Account selectedAccount = accountList.get((int)accountUsed.getSelectedItemId());

                if(selectedAccount.getAmount() < newTransaction.getAmount() && !userAcknowledged && expense) {
                    //Invalid Transaction
                    //Notify User invalid #, cancel this action.

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Warning: Insufficient Funds");
                    builder.setCancelable(false);

                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        userAcknowledged = true;
                        }
                    });



                    AlertDialog dialog = builder.create();
                    dialog.show();

                    System.out.println("Not enough Money");
                    return;
                }

                //Adding a transaction to the file and saving
                JsonHandler<Transaction> handlerTransaction = (JsonHandler)getApplication();
                ArrayList<Transaction> objectList = handlerTransaction.getJSONObjects("history", Transaction.class);
                objectList.add(newTransaction);

                try {
                    handlerTransaction.setJSONObjects(objectList, "history");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Updating the amount of an account and saving the file
                JsonHandler<Account> handlerAccount = (JsonHandler)getApplication();

                //If it's income we add money to the selected account
                if(!expense) {
                    selectedAccount.setAmount(selectedAccount.getAmount() + newTransaction.getAmount());
                } else { //Else we remove money, increment the amount spent and times used.

                    selectedAccount.setAmount(selectedAccount.getAmount() - newTransaction.getAmount());
                    selectedAccount.setTotalSpent(selectedAccount.getTotalSpent() + newTransaction.getAmount());
                    selectedAccount.setTimesUsed(selectedAccount.getTimesUsed() + 1);
                }

                accountList.remove((int)accountUsed.getSelectedItemId()); //Here we have to remove the account and resave it
                accountList.add(selectedAccount);

                try {
                    handlerAccount.setJSONObjects(accountList,"accounts");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(context, "Transaction Added", Toast.LENGTH_LONG).show();
                //End current activity
                Intent detailIntent = new Intent(context, MainScreen.class);
                detailIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(detailIntent);
            }
        });


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
