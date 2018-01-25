package devops.colby.cheqit;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddActivity extends AppCompatActivity {
    final Context context = this;
    static final int REQUEST_TAKE_PHOTO = 1;
    ImageView image;

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    //Method to take photos of the reciept/purchase for logging
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                System.out.println(MediaStore.EXTRA_OUTPUT);
                Uri photoURI = FileProvider.getUriForFile(this,
                        "devops.colby.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            /*System.out.println(data);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("output");*/
            Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            image.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final Button submitButton = findViewById(R.id.button_submit);
        final EditText nameText = findViewById(R.id.nameText);
        final EditText amountText = findViewById(R.id.amountText);
        final TimePicker timePicker = findViewById(R.id.timeText);
        final EditText commentText = findViewById(R.id.commentText);
        final EditText locationText = findViewById(R.id.locationText);
        final Spinner accountUsed = findViewById(R.id.accountSelection);
        final RadioGroup expenseGroup = findViewById(R.id.radioGroup);
        final Button takePhoto = findViewById(R.id.photo_button);
        image = findViewById(R.id.photo_image);


        final boolean[] userAcknowledged = {false};
        JsonHandler<Account> handler = (JsonHandler) getApplication();
        final ArrayList<Account> accountList = handler.getJSONObjects("accounts", Account.class);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, accountList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        accountUsed.setAdapter(adapter);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent();

            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Transaction newTransaction = new Transaction();
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
                }


                newTransaction.setName(nameText.getText().toString());
                newTransaction.setAmount(Double.parseDouble(amountText.getText().toString()));
                newTransaction.setTime(String.valueOf(timePicker.getHour()) + ":" +String.valueOf(timePicker.getMinute()));
                newTransaction.setComment(commentText.getText().toString());
                newTransaction.setphotoUri(mCurrentPhotoPath);

                RadioButton expenseButton = (RadioButton) findViewById(expenseGroup.getCheckedRadioButtonId());

                boolean expense = true;
                if(expenseButton.getText() == "Income") {
                    expense = false;
                }

                newTransaction.setIsExpense(expense);

                newTransaction.setLocation(locationText.getText().toString());
                newTransaction.setAccount(accountUsed.getSelectedItem().toString());

                //Check if account has enough money, subtract if it does. If it doesn't, cancel this addition (but save all info)
                Account selectedAccount = accountList.get((int)accountUsed.getSelectedItemId());

                if(selectedAccount.getAmount() < newTransaction.getAmount() && !userAcknowledged[0]) {
                    //Invalid Transaction
                    //Notify User invalid #, cancel this action.

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Warning: Insufficient Funds");
                    builder.setCancelable(false);

                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            userAcknowledged[0] = true;
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
                System.out.println(expenseButton.isChecked());
                if(expense) {
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

}
