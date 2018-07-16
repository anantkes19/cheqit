package devops.colby.cheqit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DataActivity extends FragmentActivity implements OnMapReadyCallback {
    ArrayList<Transaction> transactionList;
    ArrayList<Transaction> graphTransactionList;
    ArrayList<Account> accountList;
    private GoogleMap mMap;
    HeatmapTileProvider mProvider;
    TileOverlay mOverlay;
    double sum;
    final Context context = this;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }




    public LineGraphSeries<DataPoint> getGraphData() {
        LineGraphSeries<DataPoint> dataPoints = new LineGraphSeries<>();
        Date today = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(today);
        graphTransactionList = new ArrayList<>(transactionList); //Make shallow copy of transactionList

        Collections.sort(graphTransactionList, new Comparator<Transaction>() {
            public int compare(Transaction o1, Transaction o2) {
                if (o1.getDateTime() == null || o2.getDateTime() == null)
                    return 0;
                return o1.getDateTime().compareTo(o2.getDateTime());
            }
        });
        double numDays = TimeUnit.MILLISECONDS.toDays(Math.abs(now.getTimeInMillis() - graphTransactionList.get(0).getDateTime().getTimeInMillis()));


        ArrayList<ArrayList<Double>> graphData = new ArrayList<>();
        for(int fill = 0; fill < (int) numDays+1; fill ++) {
            graphData.add(new ArrayList<Double>());
        }

        for(int i = 0; i<graphTransactionList.size(); i++) {
            if(graphTransactionList.get(i).getDateTime().before(now) && graphTransactionList.get(i).getIsExpense()) {
                System.out.println("####### Transaction Found for Graph");
                double timeDifference = TimeUnit.MILLISECONDS.toDays(Math.abs(now.getTimeInMillis() - graphTransactionList.get(i).getDateTime().getTimeInMillis()));

                graphData.get((int) timeDifference).add(graphTransactionList.get(i).getAmount());

            }
        }
        for(int day = 0; day < graphData.size(); day++) {
            sum = 0;
            for(int transaction = 0; transaction < graphData.get(day).size(); transaction++) {
                sum += graphData.get(day).get(transaction);
            }
            dataPoints.appendData(new DataPoint(day,sum),false,10000,false);
        }
            //
        dataPoints.setColor(Color.GREEN);
        dataPoints.setDataPointsRadius(5);
        dataPoints.setThickness(4);
        dataPoints.setTitle("Money Spent per Day");
        return dataPoints;
    }

    public LineGraphSeries<DataPoint> getGraphDataTotal(Date date) {
        LineGraphSeries<DataPoint> dataPoints = new LineGraphSeries<>();
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        calendar.setTime(date);
        now.setTime(today);
        graphTransactionList = new ArrayList<>(transactionList); //Make shallow copy of transactionList
        ArrayList<ArrayList<Double>> graphData = new ArrayList<>();
        for(int fill = 0; fill < 30; fill ++) {
            graphData.add(new ArrayList<Double>());
        }

        //Graph for expenses in the past month
        for(int i = 0; i<graphTransactionList.size(); i++) {
            if(graphTransactionList.get(i).getDateTime().before(now) && graphTransactionList.get(i).getDateTime().after(calendar) && graphTransactionList.get(i).getIsExpense()) {
                System.out.println("####### Transaction Found for Graph");
                double timeDifference = TimeUnit.MILLISECONDS.toDays(Math.abs(now.getTimeInMillis() - graphTransactionList.get(i).getDateTime().getTimeInMillis()));

                graphData.get((int) timeDifference).add(graphTransactionList.get(i).getAmount());

            }
        }

        //Sum for each day
        sum = 0;
        for(int day = 29; day >= 0; day--) {

            for(int transaction = 0; transaction < graphData.get(day).size(); transaction++) {
                sum += graphData.get(day).get(transaction);
            }

        }

        for(int day = 0; day < graphData.size(); day++) {
            dataPoints.appendData(new DataPoint(day,sum),false,10000,false);
            for(int transaction = 0; transaction < graphData.get(day).size(); transaction++) {
                sum -= graphData.get(day).get(transaction);
            }
        }
        //
        dataPoints.appendData(new DataPoint(30,0),false,10000,false);
        dataPoints.setColor(Color.GREEN);
        dataPoints.setDataPointsRadius(5);
        dataPoints.setThickness(4);
        dataPoints.setTitle("Total Money Spent per Day");
        return dataPoints;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng center = new LatLng(transactionList.get(0).getLatitude(),transactionList.get(0).getLongitude());
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));

        addHeatMap();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        bottomNavControl bottomNav = new bottomNavControl(context, this, 3);
        bottomNav.bottomMenu();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final TextView mostSpentName = findViewById(R.id.data_most_spent_name);
        final TextView mostSpentAmount = findViewById(R.id.data_most_spent_amount);
        final TextView mostUsedName = findViewById(R.id.data_most_used_name);
        final TextView mostUsedAmount = findViewById(R.id.data_most_used_amount);
        final TextView mostExpensiveName = findViewById(R.id.data_most_expensive_name);
        final TextView mostExpensiveAmount = findViewById(R.id.data_most_expensive_amount);
        final GraphView past30Day = findViewById(R.id.data_graph_30);
        final GraphView pastSpending = findViewById(R.id.data_graph_spending);




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

        pastSpending.addSeries(getGraphData());
        pastSpending.getViewport().setScrollable(true);
        pastSpending.getViewport().setScrollableY(true);
        pastSpending.getViewport().setScalable(true);
        pastSpending.getViewport().setScalableY(true);

        past30Day.addSeries(getGraphDataTotal(past30));
        past30Day.getViewport().setScrollable(true);
        past30Day.getViewport().setScrollableY(true);
        past30Day.getViewport().setScalable(true);
        past30Day.getViewport().setScalableY(true);


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
