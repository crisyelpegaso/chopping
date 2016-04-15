package org.chopping.activity;

import android.app.Activity;
import android.os.Bundle;

import com.chopping.org.chopping.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.chopping.model.Store;

import java.util.ArrayList;

/**
 * Created by OE on 30/07/2015.OnMapReadyCallback
 */
public class MapActivity extends Activity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        //LatLng storeLocation = new LatLng(store.getLatitude(), store.getLongitude());
        Bundle data = getIntent().getExtras();
        ArrayList<Store> stores = data.getParcelableArrayList("stores");

        if(stores == null){
            throw new RuntimeException("Store from listener is null");
        }

        for(Store store : stores){
            LatLng storeLocation = new LatLng(store.getLatitude(),store.getLongitude());
            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 16));

            map.addMarker(new MarkerOptions()
                    .title(store.getName())
                    .snippet("Yeah baby!.")
                    .position(storeLocation));
        }
    }

}
