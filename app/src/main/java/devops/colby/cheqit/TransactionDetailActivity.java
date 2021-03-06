package devops.colby.cheqit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TransactionDetailActivity extends AppCompatActivity {
    final Context context = this;
    static final int REQUEST_TAKE_PHOTO = 1;
    ImageView photoImage;
    String photoPath ="";
    final Activity activity = this;
    ImageCapture imageCapture;
    int newId;




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            photoImage.setImageBitmap(imageCapture.getPhoto());
            photoPath = imageCapture.getmCurrentPhotoPath();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        imageCapture = new ImageCapture(activity, context);
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
        final DatePicker datePicker = findViewById(R.id.edit_datePicker);

        photoImage = findViewById(R.id.edit_photo_image);



        String time[] = transaction.getTime().split(":");
        timePicker.setHour(Integer.parseInt(time[0]));
        timePicker.setMinute(Integer.parseInt(time[1]));

        String[] dateString = transaction.getDate().split(":");
        datePicker.init(Integer.parseInt(dateString[0]),Integer.parseInt(dateString[1]),Integer.parseInt(dateString[2]), null);


        photoPath = transaction.getPhotoUri();
        nameText.setText(transaction.getName());
        amountText.setText(String.valueOf(transaction.getAmount()));
        commentText.setText(transaction.getComment());

        //Loading image
        if(photoPath != null && !Objects.equals(photoPath, "")) {
            imageCapture.setmCurrentPhotoPath((photoPath));
            photoImage.setImageBitmap(imageCapture.getPhoto());

        }


        photoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(context,ZoomedImageActivity.class);
                detailIntent.putExtra("photo",photoPath);
                startActivity(detailIntent);
            }
        });


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

        //Setting Spinner to the account used
        accountUsed.setAdapter(adapter);

        int spinnerPosition=0;
        System.out.println("Items in adapter");
        for(int i=0 ; i<adapter.getCount() ; i++){
            Account acc = (Account) adapter.getItem(i);
            if(acc.getName().equals(transaction.getAccount())) {
                spinnerPosition = i;
            }
        }


        accountUsed.setSelection(spinnerPosition);

        //Preparing photo button
        takePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                imageCapture.dispatchTakePictureIntent();


            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Transaction newTransaction = new Transaction();

                newTransaction.setName(nameText.getText().toString());
                newTransaction.setAmount(Double.parseDouble(amountText.getText().toString()));
                newTransaction.setTime(String.valueOf(timePicker.getHour()) + ":" +String.valueOf(timePicker.getMinute()));
                newTransaction.setComment(commentText.getText().toString());
                newTransaction.setPhotoUri(photoPath);
                newTransaction.setId(transaction.getId());
                String dateString = datePicker.getYear() + ":" + datePicker.getMonth() + ":" + datePicker.getDayOfMonth();
                newTransaction.setDate(dateString);
                newTransaction.setLatitude(transaction.getLatitude());
                newTransaction.setLongitude(transaction.getLongitude());


                RadioButton expenseButton = (RadioButton) findViewById(expenseGroup.getCheckedRadioButtonId());

                //System.out.println(expenseButton.getText());
                boolean expense = true;
                if(expenseButton.getText().equals("Income")) {
                    System.out.println("INCOME");
                    expense = false;
                }

                newTransaction.setIsExpense(expense);

                newTransaction.setLocation(locationText.getText().toString());
                newTransaction.setAccount(accountUsed.getSelectedItem().toString());

                JsonHandler<Transaction> handler = (JsonHandler)getApplication();

                ArrayList<Transaction> objectList = handler.getJSONObjects("history", Transaction.class);

                System.out.println(transaction.getName());
                for(int i=0; i<objectList.size(); i++) {
                    if(Long.parseLong(transaction.getId()) == Long.parseLong(objectList.get(i).getId())) {
                        newId = i;
                    }
                }

                objectList.remove(newId);
                objectList.add(newTransaction);
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
                //Delete photo associated with the transaction
                if(!transaction.getPhotoUri().equals("")) {
                    File file = new File(transaction.getPhotoUri());
                    file.delete();
                }

                JsonHandler<Transaction> handler = (JsonHandler)getApplication();

                ArrayList<Transaction> objectList = handler.getJSONObjects("history", Transaction.class);

                for(int i=0; i<objectList.size(); i++) {
                    if(Long.parseLong(transaction.getId()) == Long.parseLong(objectList.get(i).getId())) {
                        newId = i;
                    }
                }

                objectList.remove(newId);
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
