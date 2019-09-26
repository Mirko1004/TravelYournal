package zkezic.fesb.mimosproject.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zkezic.fesb.mimosproject.R;
import zkezic.fesb.mimosproject.data.models.Location;
import zkezic.fesb.mimosproject.data.source.LocationDataSource;
import zkezic.fesb.mimosproject.presentation.adapters.LocationListAdapter;
import zkezic.fesb.mimosproject.presentation.utils.Router;

public class MainMenuActivity extends AppCompatActivity {

    @BindView(R.id.locations_marked_recycler_view) RecyclerView visitedLocationsRecyclerView;

    @BindView(R.id.empty_list_container) LinearLayout emptyListContainer;

    LocationListAdapter visitedLocationsListAdapter;
    LocationDataSource dataSource;
    List<Location> visitedLocations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupLayout();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @OnClick(R.id.add_location_button)
    public void onAddLocationButtonClicked(final View view) {
        Router.showAddNewLocation(this);
    }

    public void setupLayout() {
        dataSource = new LocationDataSource(getApplicationContext());
        visitedLocations = dataSource.getAllLocations();

        if(visitedLocations != null && visitedLocations.size() > 0) {
            emptyListContainer.setVisibility(View.GONE);
            visitedLocationsRecyclerView.setVisibility(View.VISIBLE);
            visitedLocationsListAdapter = new LocationListAdapter(this, visitedLocations);
            final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            visitedLocationsRecyclerView.setLayoutManager(mLayoutManager);
            visitedLocationsRecyclerView.setAdapter(visitedLocationsListAdapter);
        } else {
            emptyListContainer.setVisibility(View.VISIBLE);
            visitedLocationsRecyclerView.setVisibility(View.GONE);
        }
    }
}
