package com.example.user.trainclientapp.activities;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.user.trainclientapp.R;
import com.example.user.trainclientapp.geolocation.MyGPS;
import com.example.user.trainclientapp.servernetworking.URLASyncTask;
import com.example.user.trainclientapp.stationlist.StationListFactory;
import com.example.user.trainclientapp.stationlist.StationsAdapter;
import com.example.user.trainclientapp.stationlist.TrainStation;

import java.util.ArrayList;

public class NearestStationListActivity extends AppCompatActivity {


    Button refreshButton;
    LocationManager lm;
    ArrayList<TrainStation> trainStationArrayList;
    StationsAdapter adapter;
    ListView listView;
    String srvData;
    int listLength;
    MyGPS myGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_station_list);

        refreshButton = (Button) findViewById(R.id.refreshButton);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        listView = (ListView) findViewById(R.id.stationList);

        updateMyGPS();
        createStationList();
        updateList(adapter, listView);


    }


    private void createStationList(){
        StationListFactory stationListFactory = new StationListFactory(srvData,  myGPS.getMyLat(), myGPS.getMyLong(), listLength);
        trainStationArrayList = stationListFactory.getTrainStationArrayList();
    }

    private void  updateList(StationsAdapter adapter, ListView listView){
        adapter = new StationsAdapter(this, android.R.layout.simple_list_item_1, trainStationArrayList);
        this.adapter = adapter;
        listView.setAdapter(adapter);
        this.listView = listView;
    }

    private void updateMyGPS(){
        myGPS = new MyGPS(lm,this);
    }

    private void getNearestStationDataFromSrv(){
        new URLASyncTask(this, myGPS.getMyLat().toString(), myGPS.getMyLong().toString()).execute();
    }

    public void updataData(String newData){
        srvData =  newData;
    }

    public void updateLocation(View view) {
        updateMyGPS();
        createStationList();
        updateList(adapter, listView);
    }

    public void listItemClicked(View view) {
        Intent startIntent = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(startIntent);
    }
}
