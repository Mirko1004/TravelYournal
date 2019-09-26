package zkezic.fesb.mimosproject.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zkezic.fesb.mimosproject.R;
import zkezic.fesb.mimosproject.data.source.LocationDataSource;
import zkezic.fesb.mimosproject.presentation.utils.Router;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.add_location_button) TextView addNewLocationButton;

    @BindView(R.id.visited_locations_button) TextView visitedLocationsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupLayout();
    }

    @OnClick(R.id.visited_locations_button)
    public void onVisitedLocationsButtonClicked(final View view) {
        Router.showMainMenu(this);
    }

    @OnClick(R.id.add_location_button)
    public void onAddNewLocationButtonClicked(final View view) {
        Router.showAddNewLocation(this);
    }

    public void setupLayout() {
        LocationDataSource locationDataSource = new LocationDataSource(getApplicationContext());
        if(locationDataSource.getLocationsCount() > 0) {
            visitedLocationsButton.setVisibility(View.VISIBLE);
        } else {
            visitedLocationsButton.setVisibility(View.GONE);
        }
    }

}
