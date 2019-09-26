package zkezic.fesb.mimosproject.presentation.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import zkezic.fesb.mimosproject.R;
import zkezic.fesb.mimosproject.data.models.Location;
import zkezic.fesb.mimosproject.data.source.LocationDataSource;
import zkezic.fesb.mimosproject.presentation.utils.Router;

public class AllLocationsMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public ArrayList<Location> locations;
    int locationToZoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_locations_map);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupLayout();
    }

    @Override //dohvaca sve lokacije iz baze i za njih postavlja markere na mapu
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        LocationDataSource myDb = new LocationDataSource(getApplicationContext());
        myDb.open();
        locations = myDb.getAllLocations();

        LatLng latLngToZoom = null;

        for (int i = 0; i < locations.size(); i++) {
            LatLng pos = new LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude());
            if(locations.get(i).getId() == locationToZoom) latLngToZoom = pos;
            mMap.addMarker(new MarkerOptions().position(pos)
                    .title(locations.get(i).getName())
                    .snippet(locations.get(i).getDate())).setTag(i);
        }

        if(latLngToZoom != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngToZoom, 12));
        }

        myDb.close();

        mMap.setOnMarkerClickListener(marker -> {
            int position = (int)(marker.getTag());
            Location location = locations.get(position);
            Router.showLocationDetails(this, location.getId());
            return false;
        });
    }

    @OnClick(R.id.btnLocList)
    public void onDetailsButtonClicked(final View view) {
        Router.showMainMenu(this);
    }

    public void setupLayout() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.get("ID") != null) locationToZoom = Integer.valueOf(extras.get("ID").toString());
        }
    }
}