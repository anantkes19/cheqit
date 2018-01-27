package devops.colby.cheqit;

import android.Manifest;
import android.content.Context;
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
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class TransactionDetailActivity extends AppCompatActivity {
    final Context context = this;
    static final int REQUEST_TAKE_PHOTO = 1;
    ImageView photoImage;

    String photoPath;

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        System.out.println(photoPath);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);

        this.sendBroadcast(mediaScanIntent);
    }
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
        photoPath = image.getAbsolutePath();
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
            Bitmap imageBitmap = BitmapFactory.decodeFile(photoPath);

            photoImage.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        final long id = this.getIntent().getExtras().getLong("id");
        JSONObject jsonString = null;
        try {
            jsonString = new JSONObject(this.getIntent().getExtras().getString("object"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final Transaction transaction = new Transaction();
        try {
            transaction.setAttributes(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Button submitButton = findViewById(R.id.edit_button_submit);
        final Button deleteButton = findViewById(R.id.edit_button_delete);
        final TimePicker timePicker = findViewById(R.id.edit_timeText);
        final EditText nameText = findViewById(R.id.edit_nameText);
        final EditText amountText = findViewById(R.id.edit_amountText);
        final EditText commentText = findViewById(R.id.edit_commentText);
        final EditText locationText = findViewById(R.id.edit_locationText);
        final Spinner accountUsed = findViewById(R.id.edit_accountSelection);
        final RadioGroup expenseGroup = findViewById(R.id.edit_radioGroup);
        final Button takePhoto = findViewById(R.id.edit_photo_button);

        photoImage = findViewById(R.id.edit_photo_image);

        //TODO Load time picker with same time as submitted
        //timePicker.setHour();
        photoPath = transaction.getPhotoUri();
        nameText.setText(transaction.getName());
        amountText.setText(String.valueOf(transaction.getAmount()));
        commentText.setText(transaction.getComment());

        //Loading image
        System.out.println("Photo Path: "+photoPath);
        if(photoPath != null) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(photoPath);
            photoImage.setImageBitmap(imageBitmap);
        }
        System.out.println("Photo Path: "+photoPath);
        //Setting radio boxes
        if(transaction.getIsExpense()) {
            findViewById(R.id.edit_radioExpense).performClick();
        }
        else {
            findViewById(R.id.edit_radioIncome).performClick();
        }


        //Filling in spinner of accounts
        JsonHandler<Account> handler = (JsonHandler) getApplication();
        final ArrayList<Account> accountList = handler.getJSONObjects("accounts", Account.class);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, accountList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        accountUsed.setAdapter(adapter);

        //Preparing photo button
        takePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent();

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                galleryAddPic();
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

                transaction.setName(nameText.getText().toString());
                transaction.setAmount(Double.parseDouble(amountText.getText().toString()));
                transaction.setTime(String.valueOf(timePicker.getHour()) + ":" +String.valueOf(timePicker.getMinute()));
                transaction.setComment(commentText.getText().toString());
                transaction.setPhotoUri(photoPath);



                RadioButton expenseButton = (RadioButton) findViewById(expenseGroup.getCheckedRadioButtonId());

                boolean expense = true;
                if(expenseButton.getText() == "Income") {
                    expense = false;
                }

                newTransaction.setIsExpense(expense);

                newTransaction.setLocation(locationText.getText().toString());
                newTransaction.setAccount(accountUsed.getSelectedItem().toString());

                JsonHandler<Transaction> handler = (JsonHandler)getApplication();

                ArrayList<Transaction> objectList = handler.getJSONObjects("history", Transaction.class);
                objectList.remove((int)id);
                objectList.add(transaction);
                try {
                    handler.setJSONObjects(objectList,"history");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //End current activity
                Toast.makeText(getApplicationContext(), "Edits Saved", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                setResult(RESULT_OK,intent );
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /*AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage("Are you sure you wish to delete this account?");
                builder.setCancelable(false);
                final boolean[] confirmation = {false};
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        return;
                    }
                });
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        confirmation[0] = true;
                    }
                });

                //Actually fix this with dialogs.

                AlertDialog dialog = builder.create();
                dialog.show();*/

                JsonHandler<Transaction> handler = (JsonHandler)getApplication();

                ArrayList<Transaction> objectList = handler.getJSONObjects("history", Transaction.class);
                objectList.remove((int)id);
                try {
                    handler.setJSONObjects(objectList,"history");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //End current activity
                Toast.makeText(getApplicationContext(), "Transaction Deleted", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                setResult(RESULT_OK,intent );
                finish();
            }
        });
    }
}
