package zkezic.fesb.mimosproject.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zkezic.fesb.mimosproject.R;
import zkezic.fesb.mimosproject.presentation.utils.Router;

public class AddNewLocationActivity extends AppCompatActivity implements OnMapReadyCallback, PlaceSelectionListener {

    public static String TAG = "AddNewLocationActivity";
    private GoogleMap mMap;
    private String locationName;
    private String locationAddress;
    private double locationLat;
    private double locationLon;

    SupportMapFragment mapFragment;

    @BindView(R.id.mark_selected_location_button) LinearLayout addLocationButton;

    @BindView(R.id.cancel_button) LinearLayout cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_location);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupLayout();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "Place: " + place.getName());
        locationName = place.getName().toString(); // Location name
        locationAddress = place.getAddress().toString(); // Location name
        locationLat = place.getLatLng().latitude;
        locationLon = place.getLatLng().longitude;

        LatLng latLng = new LatLng(locationLat, locationLon);  //koordinate lokacije
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title(locationName).snippet(locationAddress));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationLat, locationLon), 12));
    }

    @Override
    public void onError(Status status) {
        // TODO: Handle the error.
        Log.i(TAG, "An error occurred: " + status);
    }

    @OnClick(R.id.cancel_button)
    public void onCancelButtonClicked(final View view) {
        onBackPressed();
    }

    @OnClick(R.id.mark_selected_location_button)
    public void onMarkSelectedLocationButtonClicked(final View view) {
        if (!TextUtils.isEmpty(locationName)) {
            Router.showAddLocationDetails(this, locationName, locationAddress, locationLat, locationLon);
        } else {
            Toast.makeText(getApplicationContext(), "Enter the location", Toast.LENGTH_SHORT).show();
        }
    }

    public void setupLayout() {
        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(this);
    }
}