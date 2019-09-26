package zkezic.fesb.mimosproject.presentation.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import zkezic.fesb.mimosproject.R;
import zkezic.fesb.mimosproject.data.models.Location;
import zkezic.fesb.mimosproject.data.source.LocationDataSource;
import zkezic.fesb.mimosproject.presentation.utils.Router;

import static android.widget.Toast.LENGTH_LONG;


public class LocationDetailsActivity extends Activity {

    @BindView(R.id.location_image) ImageView locationImage;

    @BindView(R.id.location_name) TextView locationName;

    @BindView(R.id.location_address) TextView locationAddress;

    @BindView(R.id.location_date) TextView locationDate;

    @BindView(R.id.location_note) TextView locationNote;

    @BindView(R.id.location_video_label) TextView locationVideoLabel;

    @BindView(R.id.location_video) VideoView locationVideo;

    @BindView(R.id.toolbar_title) TextView toolbarTitle;

    @BindView(R.id.button_edit_location) ImageView editLocation;

    @BindView(R.id.button_delete_location)ImageView deleteLocation;

    LocationDataSource dataSource;
    Location locationDisplayed;
    ArrayList<Location> locations;
    Integer key = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupLayout();
    }

    @Override
    public void onBackPressed() {
        Router.showMainMenu(this);
    }

    @OnClick(R.id.location_image)
    public void onLocationImageClicked(final View view) {
        Router.showGallery(this, "Travel journal/image_" + Integer.toString(locationDisplayed.getId()) + ".png", false);
    }

    @OnTouch(R.id.location_video)
    public boolean onLocationVideoClicked(final View view) {
        Router.showGallery(this, "Travel journal/video_" + Integer.toString(locationDisplayed.getId()) + ".mp4", true);
        return false;
    }

    @OnClick(R.id.location_map)
    public void onMapLocationButtonClicked(final View view) {
        Intent editLocationIntent = new Intent(LocationDetailsActivity.this, AllLocationsMapActivity.class);
        editLocationIntent.putExtra("ID", locationDisplayed.getId());
        startActivity(editLocationIntent);
    }

    @OnClick(R.id.button_edit_location)
    public void onEditLocationButtonClicked(final View view) {
        Intent editLocationIntent = new Intent(LocationDetailsActivity.this, AddNoteImageActivity.class);
        editLocationIntent.putExtra("ID", locationDisplayed.getId());
        startActivity(editLocationIntent);
    }

    @OnClick(R.id.button_delete_location)
    public void onDeleteLocationButtonClicked(final View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(LocationDetailsActivity.this).create();
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Are you sure you want to delete this location?");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                (dialog, which) -> dialog.dismiss());

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete",
                (dialog, which) -> {
                    if (locationDisplayed != null) {
                        dataSource.deleteRow(String.valueOf(locationDisplayed.getId()));
                        Toast.makeText(getApplicationContext(), "Location deleted successfully", Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(LocationDetailsActivity.this, MainMenuActivity.class);
                        startActivity(myIntent);
                    }

                    dialog.dismiss();
                });

        alertDialog.show();
    }

    public void setupLayout() {
        dataSource = new LocationDataSource(getApplicationContext());
        dataSource.open();
        locations = dataSource.getAllLocations();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.get("ITEM") != null) key = Integer.valueOf(extras.get("ITEM").toString());

            locationDisplayed = dataSource.getLocation(key);

            if (locationDisplayed == null) {
                Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_LONG).show();
                finish();
            }

            Glide.with(this)
                    .load(new File(Environment.getExternalStorageDirectory(),
                            "Travel journal/image_" + Integer.toString(locationDisplayed.getId()) + ".png")) // Uri of the picture
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(locationImage);

            toolbarTitle.setText(locationDisplayed.getName());
            locationName.setText(locationDisplayed.getName());
            locationAddress.setText(locationDisplayed.getAddress());
            locationDate.setText(locationDisplayed.getDate());
            locationNote.setText(locationDisplayed.getNote());

            try {
                File videoFileUploaded = new File(Environment.getExternalStorageDirectory(),
                        "Travel journal/video_" + Integer.toString(locationDisplayed.getId()) + ".mp4");

                if (videoFileUploaded.exists()) {
                    locationVideoLabel.setVisibility(View.VISIBLE);
                    locationVideo.setVisibility(View.VISIBLE);
                    locationVideo.setMediaController(new MediaController(this));
                    locationVideo.setVideoURI(Uri.fromFile(videoFileUploaded));
                    locationVideo.start();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open video", LENGTH_LONG).show();
            }
        }
    }
}
