package com.example.root.railways;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelledTrainsActivity extends AppCompatActivity implements TrainCancelledAdapter.TrainsAdapterListener {

    private static final String TAG = "CancelledTrains" ;
    private SwipeRefreshLayout SwipeRefresh;
    private List<Train> Trains;
    private RecyclerView Recyclerview;
    private TrainCancelledAdapter mAdapter;
    private String date;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private TextView noOfCancelledTrains;
    private TextView QueryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled_trains);
        intializeViews();
        calendar = Calendar.getInstance();
         year = calendar.get(Calendar.YEAR);
         month = calendar.get(Calendar.MONTH);
         day = calendar.get(Calendar.DAY_OF_MONTH);

     setDate(year,month+1,day);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: ");
                try
                {
                    mAdapter.getFilter().filter(query);

                }
                catch (Exception e)
                {

                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: ");
                try
                {
                    mAdapter.getFilter().filter(newText);

                }
                catch (Exception e)
                {

                }
                return false;

            }
        });

        return true;
    }

    private void callForData() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CancelledTrains> call  = apiService.getCancelledTrains(date);
        call.enqueue(new Callback<CancelledTrains>() {
            @Override
            public void onResponse(Call<CancelledTrains> call, Response<CancelledTrains> response) {

                try
                {
                    Trains = response.body().getTrains();
                     Log.d(TAG, "onResponse: "+Trains.get(0).getName().toString());
                    Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                    SwipeRefresh.setRefreshing(false);


                    show();
                }
                catch (Exception e)
                {
                    Log.d(TAG, "onResponse: exception");
                    SwipeRefresh.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<CancelledTrains> call, Throwable t) {
                Log.d(TAG, "onFailure: failed");
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                SwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void intializeViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setTitle("Cancelled Trains");
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               callDate();
            }
        });
        SwipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callForData();
                Toast.makeText(getApplicationContext(),"Refreshed"+date,Toast.LENGTH_SHORT).show();
            }
        });
        SwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                SwipeRefresh.setRefreshing(true);
                callForData();
            }
        });

        noOfCancelledTrains = (TextView)findViewById(R.id.numcancel);
        QueryDate = (TextView)findViewById(R.id.Date);
    }


    private void callDate() {

        Log.d(TAG, "callDate: called");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        setDate(year,month+1,dayOfMonth);
                        callForData();
                        SwipeRefresh.setRefreshing(true);
                Log.d(TAG, "onDateSet: "+date);
            }
        },year,month,day);
        datePickerDialog.show();
    }

    private void setDate(int year, int month, int dayOfMonth) {
        date = Integer.toString(dayOfMonth)+"-"+Integer.toString(month)+"-"+Integer.toString(year);

        Toast.makeText(getApplicationContext(),date,Toast.LENGTH_SHORT).show();
    }

    private void show() {

        noOfCancelledTrains.setText(Integer.toString(Trains.size()));
        QueryDate.setText(date);

        Recyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        Recyclerview.setLayoutManager(mLayoutManager);
        mAdapter = new TrainCancelledAdapter(Trains,this);
        Recyclerview.setAdapter(mAdapter);


    }

    @Override
    public void onTrainSelected(Train train) {
        Gson gson = new Gson();
       Intent intent = new Intent(getApplicationContext(),TrainDisplay.class);
       intent.putExtra("Train",gson.toJson(train));
       startActivity(intent);
        //Toast.makeText(getApplicationContext(),train.getName(),Toast.LENGTH_SHORT).show();
    }
}


