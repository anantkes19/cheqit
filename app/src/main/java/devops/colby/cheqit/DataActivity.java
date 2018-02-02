package devops.colby.cheqit;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DataActivity extends FragmentActivity implements OnMapReadyCallback {
    ArrayList<Transaction> transactionList;
    ArrayList<Account> accountList;
    private GoogleMap mMap;
    HeatmapTileProvider mProvider;
    TileOverlay mOverlay;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addHeatMap();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final TextView mostSpentName = findViewById(R.id.data_most_spent_name);
        final TextView mostSpentAmount = findViewById(R.id.data_most_spent_amount);
        final TextView mostUsedName = findViewById(R.id.data_most_used_name);
        final TextView mostUsedAmount = findViewById(R.id.data_most_used_amount);
        final TextView mostExpensiveName = findViewById(R.id.data_most_expensive_name);
        final TextView mostExpensiveAmount = findViewById(R.id.data_most_expensive_amount);




        JsonHandler<Transaction> transactionHandler = (JsonHandler)getApplication();
        transactionList = transactionHandler.getJSONObjects("history", Transaction.class);

        JsonHandler<Account> accountHandler = (JsonHandler)getApplication();
        accountList = accountHandler.getJSONObjects("accounts", Account.class);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -30); //Past 30 Days
        Date past30 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -30); //Past 60 Days
        Date past60 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -30); //Past 90 Days
        Date past90 = calendar.getTime();

        Collections.sort(transactionList, new Comparator<Transaction>() {
            public int compare(Transaction o1, Transaction o2) {
                if(o1.getAmount() < o2.getAmount()) {
                    return -1;
                } else if(o1.getAmount() > o2.getAmount()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        Collections.reverse(transactionList);
        Collections.sort(accountList, new Comparator<Account>() {
            public int compare(Account o1, Account o2) {
                if(o1.getTotalSpent() < o2.getTotalSpent()) {
                    return -1;
                } else if(o1.getTotalSpent() > o2.getTotalSpent()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        Collections.reverse(accountList);
        Account mostSpent = accountList.get(0);

        Collections.sort(accountList, new Comparator<Account>() {
            public int compare(Account o1, Account o2) {
                if(o1.getTimesUsed() < o2.getTimesUsed()) {
                    return -1;
                } else if(o1.getTimesUsed() > o2.getTimesUsed()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        Collections.reverse(accountList);

        Account mostUsed = accountList.get(0);
        Transaction mostExpensive = transactionList.get(0);

        mostSpentName.setText(mostSpent.getName());
        mostSpentAmount.setText(String.valueOf(mostSpent.getTotalSpent()));
        mostUsedName.setText(mostUsed.getName());
        mostUsedAmount.setText(String.valueOf(mostUsed.getTimesUsed()));
        mostExpensiveName.setText(mostExpensive.getName());
        mostExpensiveAmount.setText(String.valueOf(mostExpensive.getAmount()));

        //Create functions for showing last 30,60,90 days of data, like in a graph. For loop to find those transactions.





    }

    private void addHeatMap() {
        List<LatLng> list = null;

        // Get the data: latitude/longitude positions of police stations.
        try {
            list = readItems();
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private ArrayList<LatLng> readItems() throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();

        JsonHandler<Transaction> handler = (JsonHandler)getApplication();

        ArrayList<Transaction> objectList = handler.getJSONObjects("history", Transaction.class);

        for(int i=0; i<objectList.size(); i++) {
            Transaction object = objectList.get(i);
            double lat = object.getLatitude();
            double lng = object.getLongitude();
            list.add(new LatLng(lat, lng));
        }
        return list;
    }
}
