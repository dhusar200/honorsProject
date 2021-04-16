package org.me.gcu.honorsproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsFragment extends Fragment {

    private ArrayList<Locations> locationsList;
    private HashMap<Circle, Locations> hashMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng UK = new LatLng(55.861947, -4.252275);
            hashMap = new HashMap<>();

            //change to location of the device instead of manual location
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(UK, 15);
            googleMap.animateCamera(yourLocation);
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            for (int i = 0; i < locationsList.size(); i++)
            {
                Log.e("MyTag", String.valueOf(locationsList.get(i).getGlat()));
                Log.e("MyTag", String.valueOf(locationsList.get(i).getGlong()));
                Circle tempCyrcle = googleMap.addCircle( new CircleOptions()
                        .center(new LatLng(locationsList.get(i).getGlat(),locationsList.get(i).getGlong()))
                        .fillColor(0xFFFF0000)
                        .strokeColor(0xFFFF0000)
                        .clickable(true)
                        .radius(10)); // In meters
                hashMap.put(tempCyrcle, locationsList.get(i));
            }

            googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
                @Override
                public void onCircleClick(Circle circle) {
                    Intent i = new Intent(getActivity(), LocationDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("location", hashMap.get(circle));
                    i.putExtras(bundle);
                    startActivity(i);

                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if(savedInstanceState!=null) {
            locationsList = savedInstanceState.getParcelableArrayList("locationList");
        }
        else{
            locationsList = ((MainActivity) getActivity()).getLocationsList();
            Log.e("MyTag", String.valueOf(locationsList.size()));
        }
        Log.e("MyTag", "Passed Map");

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putParcelableArrayList("locationList", locationsList);
        // etc.
    }

}