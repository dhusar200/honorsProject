package org.me.gcu.honorsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Locations> locationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();

    }

    private void loadData()
    {
        RetrieveLocationsTask retrieve = new RetrieveLocationsTask(this);
        try {
            retrieve.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        locationsList = new ArrayList<Locations>(retrieve.getLocations());
    }

    public ArrayList<Locations> getLocationsList() {
        return locationsList;
    }
}