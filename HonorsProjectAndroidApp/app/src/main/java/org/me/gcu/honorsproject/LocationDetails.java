package org.me.gcu.honorsproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.ExecutionException;

public class LocationDetails extends AppCompatActivity implements View.OnClickListener {

    private TextView name, city, street, capacity;
    private FloatingActionButton buttonBack;
    private MapView map;
    private Locations location;
    private ProgressBar progressBar;
    private int currentCount;
    private Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationdetails);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        name = (TextView) findViewById(R.id.name);
        city = (TextView) findViewById(R.id.city);
        street = (TextView) findViewById(R.id.streetName);
        capacity = (TextView) findViewById(R.id.capacity);
        map = (MapView) findViewById(R.id.mapView);
        map.onCreate(savedInstanceState);

        refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                setData();
            }
        });

        buttonBack = (FloatingActionButton) findViewById(R.id.backButton);
        buttonBack.setOnClickListener(this);

        getData();
        loadData();
        setData();


        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsInitializer.initialize(getApplicationContext());
                LatLng UK = new LatLng(location.getGlat(), location.getGlong());

                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(UK, 15);
                googleMap.animateCamera(yourLocation);
                googleMap.getUiSettings().setAllGesturesEnabled(false);
                googleMap.addMarker(new MarkerOptions().position(UK).title(location.getName()));
            }
        });

    }

    /**
     * Getting the data for the specific location which was passed in the intent as extra
     */
    private void getData()
    {
        if(getIntent().hasExtra("location"))
        {
            location = getIntent().getParcelableExtra("location");
        }
        else
        {
            Toast.makeText(this, "No data have been passed.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Setting the data for the Location
     */
    private void setData()
    {
        name.setText(location.getName());
        city.setText(location.getCity());
        street.setText(String.format("%s %s", location.getStreetName(), location.getNumber()));

        updateProgressBar(progressBar, location.getMaxCapacity(), currentCount);
    }

    private void updateProgressBar(ProgressBar progressBar, int maxProgress, int curProgress) {
        if(curProgress != 0) {
            progressBar.setVisibility(View.VISIBLE);
            double percentage = (1.0 * curProgress / maxProgress) * 100;
            progressBar.setProgress((int) percentage);
            if (percentage < 33.3) {
                capacity.setText("Low");
            } else if (percentage < 66.6) {
                capacity.setText("Medium");
            } else {
                capacity.setText("High");
            }
        }
        else
        {
            capacity.setText("No recent data");
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == buttonBack) {
            finish();
        }
    }

    /**
     * onResume which is needed to correctly load the map
     */
    @Override
    public void onResume() {
        map.onResume();
        super.onResume();
    }

        private void loadData()
    {
        String temp = location.getContinent() + "#" + location.getCountry() + "#" + location.getState()
                + "#" + location.getCity() + "#" + location.getPostCode() + "#" + location.getStreetName()
                + "#" + location.getNumber();

        RetrieveFeedTask retrieve = new RetrieveFeedTask(this, temp);
        try {
            retrieve.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentCount = retrieve.getCount();
    }

}
