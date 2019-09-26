package zkezic.fesb.mimosproject.presentation.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import zkezic.fesb.mimosproject.R;
import zkezic.fesb.mimosproject.data.models.Location;
import zkezic.fesb.mimosproject.data.source.LocationDataSource;
import zkezic.fesb.mimosproject.presentation.utils.Router;
import zkezic.fesb.mimosproject.presentation.utils.Utils;

import static android.widget.Toast.LENGTH_LONG;


public class AddNoteImageActivity extends Activity implements DatePickerDialog.OnDateSetListener {

    private int locationID;
    private String locationName;
    private String locationAddress;
    private double locationLat;
    private double locationLon;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static final int IMAGE_VIDEO_REQUEST = 101;
    public static final int TAKE_PHOTO_REQUEST = 100;
    public static final int PERMISSION_CAMERA = 110;
    public static final int PERMISSION_GALLERY = 111;
    public static final int PERMISSION_VIDEO = 112;
    public DatePickerDialog datePickerDialog;
    File imageFileUploaded;
    File videoFileUploaded;
    LocationDataSource dataSource;
    Location locationEditing;

    @BindView(R.id.location_name) TextView locationNameLabel;

    @BindView(R.id.location_address) TextView locationAddressLabel;

    @BindView(R.id.editTextDate) TextInputEditText dateInput;

    @BindView(R.id.editTextNote) TextInputEditText noteInput;

    @BindView(R.id.btnSaveLocation) LinearLayout saveLocationButton;

    @BindView(R.id.change_location_label) TextView changeLocationButton;

    @BindView(R.id.memory_image) ImageView imageUploaded;

    @BindView(R.id.memory_video) VideoView videoUploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_image);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupLayout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if (requestCode == IMAGE_GALLERY_REQUEST){
                imageUploaded.setVisibility(View.VISIBLE);
                Uri imageUri = data.getData();
                imageFileUploaded = Utils.createFile(this, "temp_image.png", imageUri);
                Glide.with(this).clear(imageUploaded);
                Glide.with(this)
                        .load(imageFileUploaded) // Uri of the picture
                        .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                        )
                        .into(imageUploaded);
            } else if (requestCode == IMAGE_VIDEO_REQUEST){
                Uri imageUri = data.getData();
                try {
                    videoFileUploaded = Utils.createFile(this, "temp_video.mp4", imageUri);
                    videoUploaded.setVisibility(View.VISIBLE);
                    videoUploaded.setMediaController(new MediaController(this));
                    videoUploaded.setVideoURI(imageUri);
                    videoUploaded.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Unable to open video", LENGTH_LONG).show();
                }
            } else if(requestCode == TAKE_PHOTO_REQUEST) {
                imageUploaded.setVisibility(View.VISIBLE);
                Glide.with(this).clear(imageUploaded);
                Glide.with(this)
                        .load(imageFileUploaded)
                        .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                        )
                        .into(imageUploaded);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CAMERA) {
            showCamera(false);
        } else if(requestCode == PERMISSION_GALLERY) {
            showGalleryPicker(false);
        } else if(requestCode == PERMISSION_VIDEO) {
            showVideoPicker(false);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        hideKeyboard();
        noteInput.requestFocus();
        showKeyboard();
        dateInput.setText(String.format("%s.%s.%s.",
                Integer.toString(dayOfMonth),
                Integer.toString(monthOfYear + 1),
                Integer.toString(year)));
    }

    @OnClick(R.id.memory_image)
    public void onImageUploadedClicked(final View view) {
        Router.showGallery(this, Utils.getFileNameFromPath(imageFileUploaded), false);
    }

    @OnTouch(R.id.memory_video)
    public boolean onVideoUploadedClicked(final View view) {
        Router.showGallery(this, Utils.getFileNameFromPath(videoFileUploaded), true);
        return false;
    }

    @OnClick(R.id.change_location_button)
    public void onChangeLocationButtonClicked(final View view) {
        onBackPressed();
    }

    @OnClick(R.id.button_take_image)
    public void onTakeImageButtonClicked(final View view) {
        showCamera(true);
    }

    @OnClick(R.id.button_add_from_gallery)
    public void onAddFromGalleryButtonClicked(final View view){
        showGalleryPicker(true);
    }

    @OnClick(R.id.button_add_video)
    public void onAddVideoButtonClicked(final View view){
        showVideoPicker(true);
    }

    @OnClick(R.id.btnSaveLocation)
    public void onSaveLocationButtonClicked(final View view) {
        if (!TextUtils.isEmpty(dateInput.getText().toString()) &&
                !TextUtils.isEmpty(noteInput.getText().toString())) {

            if (Utils.isThisDateValid(dateInput.getText().toString(), "dd.mm.yyyy.")) {
                if (imageUploaded.getDrawable() != null) {
                    LocationDataSource locationDataSource = new LocationDataSource(getApplicationContext());
                    locationDataSource.open();
                    locationID = locationDataSource.addLocToDb(locationID, locationName,
                            locationAddress, locationLat, locationLon,
                            dateInput.getText().toString(), noteInput.getText().toString());

                    if(locationID > 0) {
                        if(imageFileUploaded != null) {
                            Utils.renameFile(getApplicationContext(), imageFileUploaded,"image_" + Integer.toString(locationID) + ".png");
                        }

                        if(videoFileUploaded != null) {
                            Utils.renameFile(getApplicationContext(), videoFileUploaded, "video_" + Integer.toString(locationID) + ".mp4");
                        }
                    }

                    Toast.makeText(getApplicationContext(),
                            "Your location data is saved", LENGTH_LONG).show();

                    Intent intent = new Intent(AddNoteImageActivity.this, LocationDetailsActivity.class);
                    intent.putExtra("ITEM", locationID);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Upload image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Date time format : dd.mm.yyyy.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Missing field", Toast.LENGTH_SHORT).show();
        }

    }

    public void setupLayout() {
        loadData();
        setupDatePicker();
    }

    public void loadData() {
        dataSource = new LocationDataSource(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.get("ID") != null) locationID = Integer.valueOf(extras.get("ID").toString());

            if(locationID == 0) {
                changeLocationButton.setText(R.string.change_location);
                locationName = extras.getString("LOC_NAME");
                locationAddress = extras.getString("LOC_ADDRESS");
                locationLat = extras.getDouble("LATITUDE");
                locationLon = extras.getDouble("LONGITUDE");
                locationNameLabel.setText(locationName);
                locationAddressLabel.setText(locationAddress);
            } else {
                changeLocationButton.setText(R.string.cancel);
                locationEditing = dataSource.getLocation(locationID);
                if(locationEditing != null) {
                    locationName = locationEditing.getName();
                    locationAddress = locationEditing.getAddress();
                    locationLat = locationEditing.getLatitude();
                    locationLon = locationEditing.getLongitude();
                    locationNameLabel.setText(locationName);
                    locationAddressLabel.setText(locationAddress);
                    dateInput.setText(locationEditing.getDate());
                    noteInput.setText(locationEditing.getNote());
//
//                    File imagePreviouslyUploaded = new File(Environment.getExternalStorageDirectory(),
//                            "Travel journal/image_" + Integer.toString(locationEditing.getId()) + ".png");
//
//                    if(imagePreviouslyUploaded.exists()) {
//                        imageFileUploaded = imagePreviouslyUploaded;
//                        Glide.with(this)
//                                .load(imageFileUploaded) // Uri of the picture
//                                .apply(new RequestOptions()
//                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                        .skipMemoryCache(true))
//                                .into(imageUploaded);
//                    }

                    if(imageFileUploaded != null && imageFileUploaded.exists()) {
                        imageUploaded.setVisibility(View.VISIBLE);
                        Glide.with(this)
                                .load(imageFileUploaded) // Uri of the picture
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true))
                                .into(imageUploaded);
                    } else {
                        imageFileUploaded = new File(Environment.getExternalStorageDirectory(),
                                "Travel journal/image_" + Integer.toString(locationEditing.getId()) + ".png");

                        if(imageFileUploaded.exists()) {
                            imageUploaded.setVisibility(View.VISIBLE);
                            Glide.with(this)
                                    .load(imageFileUploaded) // Uri of the picture
                                    .apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true))
                                    .into(imageUploaded);
                        }
                    }

                    try {
                        if(videoFileUploaded != null && videoFileUploaded.exists()) {
                            videoUploaded.setVisibility(View.VISIBLE);
                            videoUploaded.setMediaController(new MediaController(this));
                            videoUploaded.setVideoURI(Uri.fromFile(videoFileUploaded));
                            videoUploaded.start();
                        } else {

                            videoFileUploaded = new File(Environment.getExternalStorageDirectory(),
                                    "Travel journal/video_" + Integer.toString(locationEditing.getId()) + ".mp4");

                            if(videoFileUploaded.exists()) {
                                videoUploaded.setVisibility(View.VISIBLE);
                                videoUploaded.setMediaController(new MediaController(this));
                                videoUploaded.setVideoURI(Uri.fromFile(videoFileUploaded));
                                videoUploaded.start();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this,"Unable to open video", LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if(imm != null && view != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if(imm != null && view != null)
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void setupDatePicker() {
        Calendar currentDate = Calendar.getInstance();

        dateInput.setOnFocusChangeListener((view, hasFocus) -> {
            if(datePickerDialog != null) {
                if(hasFocus) {
                    datePickerDialog.show();
                } else {
                    datePickerDialog.dismiss();
                    hideKeyboard();
                }
            }
        });

        datePickerDialog = new DatePickerDialog(
                this, this,
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DATE));
    }

    public void showGalleryPicker(final boolean shouldShowRequestDialog) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if(shouldShowRequestDialog) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        PERMISSION_GALLERY);
            }

            return;
        }

        Intent imageIntent = new Intent(Intent.ACTION_PICK);
        File imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String imageDirectoryPath = imageDirectory.getPath();
        Uri data = Uri.parse(imageDirectoryPath);
        imageIntent.setDataAndType(data,"image/*");
        startActivityForResult(imageIntent,  IMAGE_GALLERY_REQUEST);
    }

    public void showVideoPicker(final boolean shouldShowRequestDialog) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if(shouldShowRequestDialog) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        PERMISSION_VIDEO);
            }

            return;
        }

        Intent imageIntent = new Intent(Intent.ACTION_PICK);
        File imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String imageDirectoryPath = imageDirectory.getPath();
        Uri data = Uri.parse(imageDirectoryPath);
        imageIntent.setDataAndType(data,"video/*");
        startActivityForResult(imageIntent,  IMAGE_VIDEO_REQUEST);
    }

    public void showCamera(final boolean shouldShowRequestDialog) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if(shouldShowRequestDialog) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        PERMISSION_CAMERA);
            }

            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFileUploaded = Utils.getOutputMediaFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".provider",
                        imageFileUploaded));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, TAKE_PHOTO_REQUEST);
    }

}

