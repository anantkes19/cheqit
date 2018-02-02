package devops.colby.cheqit;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    final Context context = this;
    final Activity activity = this;
    static final int REQUEST_TAKE_PHOTO = 1;
    ImageCapture imageCapture;
    ImageView image;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    double lat;
    double lng;
    private boolean userAcknowledged = false;
    String mCurrentPhotoPath = "";

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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        final Button submitButton = findViewById(R.id.button_submit);
        final EditText nameText = findViewById(R.id.nameText);
        final EditText amountText = findViewById(R.id.amountText);
        final TimePicker timePicker = findViewById(R.id.timeText);
        final EditText commentText = findViewById(R.id.commentText);
        final EditText locationText = findViewById(R.id.locationText);
        final Spinner accountUsed = findViewById(R.id.accountSelection);
        final RadioGroup expenseGroup = findViewById(R.id.radioGroup);
        final Button takePhoto = findViewById(R.id.photo_button);
        final DatePicker datePicker = findViewById(R.id.dateText);

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

                //Attempt to get coordinates of user
                /*
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Permission Granted");
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    newTransaction.setLatitude(location.getLatitude());
                    newTransaction.setLongitude(location.getLongitude());

                } else {
                    System.out.println("No Permissions granted");
                    newTransaction.setLatitude(0.00000);
                    newTransaction.setLongitude(0.00000);
                }*/



                //Setting attributes of transaction
                newTransaction.setName(nameText.getText().toString());
                newTransaction.setAmount(Double.parseDouble(amountText.getText().toString()));
                newTransaction.setTime(String.valueOf(timePicker.getHour()) + ":" +String.valueOf(timePicker.getMinute()));
                newTransaction.setComment(commentText.getText().toString());
                newTransaction.setPhotoUri(mCurrentPhotoPath);
                newTransaction.setLocation(locationText.getText().toString());
                newTransaction.setAccount(accountUsed.getSelectedItem().toString());
                newTransaction.setId(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
                newTransaction.setLatitude(lat);
                newTransaction.setLongitude(lng);

                String dateString = datePicker.getYear() + ":" + datePicker.getMonth() + ":" + datePicker.getDayOfMonth();
                newTransaction.setDate(dateString);

                System.out.println("Photo Path: "+mCurrentPhotoPath);

                RadioButton expenseButton = (RadioButton) findViewById(expenseGroup.getCheckedRadioButtonId());

                boolean expense = true;
                System.out.println(expenseButton.getText());
                if(expenseButton.getText().equals("Income")) {
                    System.out.println("Is Income");
                    expense = false;
                }
                System.out.println(expense);
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

                if(!expense) {
                    selectedAccount.setAmount(selectedAccount.getAmount() + newTransaction.getAmount());
                } else {

                    selectedAccount.setAmount(selectedAccount.getAmount() - newTransaction.getAmount());
                    selectedAccount.setTotalSpent(selectedAccount.getTotalSpent() + newTransaction.getAmount());
                    selectedAccount.setTimesUsed(selectedAccount.getTimesUsed() + 1);
                }

                accountList.remove((int)accountUsed.getSelectedItemId());
                accountList.add(selectedAccount);

                try {
                    handlerAccount.setJSONObjects(accountList,"accounts");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(context, "Transaction Added", Toast.LENGTH_LONG).show();
                //End current activity
                finish();
            }
        });


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
