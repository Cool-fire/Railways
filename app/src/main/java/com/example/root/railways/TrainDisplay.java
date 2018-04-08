package com.example.root.railways;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.w3c.dom.Text;

public class TrainDisplay extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String TrainNo;
    private String TrainName;
    private String TrainType;
    private String StartTime;
    private TextView trainName;
    private TextView trainNumber;
    private TextView trainType;
    private TextView stratDate;
    private TextView sourceName;
    private TextView sourceCode;
    private TextView destName;
    private TextView destCode;
    private Source sourceObj;
    private Dest destObj;
    private LatLng SourceCoord;
    private LatLng DestCoord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setTitle("Train Details");
        }

        init();
        RecieveIntent();
        setData();
        setMarker();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setMarker() {
        SourceCoord =getCoord(sourceObj.getLat(),sourceObj.getLng());
        DestCoord = getCoord(destObj.getLat(),destObj.getLng());


    }

    private LatLng getCoord(Double lat, Double lng) {

        return  new LatLng(lat,lng);
    }

    private void setData() {
        trainName.setText(TrainName);
        trainNumber.setText(TrainNo);
        trainType.setText(TrainType);
        stratDate.setText(StartTime);
        sourceName.setText(sourceObj.getName().toString());
        sourceCode.setText(sourceObj.getCode().toString());
        destName.setText(destObj.getName().toString());
        destCode.setText(destObj.getCode().toString());
    }

    private void init() {
        trainName = (TextView)findViewById(R.id.TrainName);
        trainNumber = (TextView)findViewById(R.id.TrainNumber);
        trainType = (TextView)findViewById(R.id.trainType);
        stratDate = (TextView)findViewById(R.id.startDate);
        sourceName = (TextView)findViewById(R.id.SourceName);
        sourceCode = (TextView)findViewById(R.id.SourceCode);
        destName = (TextView)findViewById(R.id.DestName);
        destCode = (TextView)findViewById(R.id.DestCode);
    }

    private void RecieveIntent() {
        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("Train");
        Train train = gson.fromJson(strObj,Train.class);
        
        Dest destination =train.getDest();
        Source source = train.getSource();
        TrainNo = train.getNumber();
        TrainName = train.getName();
        TrainType = train.getType();
        StartTime = train.getStartTime();

        sourceObj = train.getSource();
        destObj = train.getDest();

    }

    private void setToast(String Text) {
        Toast.makeText(getApplicationContext(),Text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


         mMap.addMarker(new MarkerOptions().position(SourceCoord).title(sourceObj.getCode().toString()).snippet(sourceObj.getName()+"\n"+sourceObj.getLat()+"\n"+sourceObj.getLng()));
        //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,500));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(SourceCoord)      // Sets the center of the map to Mountain View
                .zoom(10)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
