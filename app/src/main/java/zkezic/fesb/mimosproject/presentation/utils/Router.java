package zkezic.fesb.mimosproject.presentation.utils;

import android.content.Context;
import android.content.Intent;

import zkezic.fesb.mimosproject.presentation.activities.AddNewLocationActivity;
import zkezic.fesb.mimosproject.presentation.activities.AddNoteImageActivity;
import zkezic.fesb.mimosproject.presentation.activities.AllLocationsMapActivity;
import zkezic.fesb.mimosproject.presentation.activities.GalleryActivity;
import zkezic.fesb.mimosproject.presentation.activities.LocationDetailsActivity;
import zkezic.fesb.mimosproject.presentation.activities.MainMenuActivity;

public class Router {

    public static void showMainMenu(final Context context) {
        Intent mainMenuIntent = new Intent(context, MainMenuActivity.class);
        context.startActivity(mainMenuIntent);
    }

    public static void showAddNewLocation(final Context context) {
        Intent addNewLocationIntent = new Intent(context, AddNewLocationActivity.class);
        context.startActivity(addNewLocationIntent);
    }

    public static void showAddLocationDetails(final Context context, final String locationName,
                                              final String locationAddress,
                                              final double locationLatitude,
                                              final double locationLongitude) {

        Intent addLocationIntent = new Intent(context, AddNoteImageActivity.class);
        addLocationIntent.putExtra("LOC_NAME", locationName);
        addLocationIntent.putExtra("LOC_ADDRESS", locationAddress);
        addLocationIntent.putExtra("LATITUDE", locationLatitude);
        addLocationIntent.putExtra("LONGITUDE", locationLongitude);
        context.startActivity(addLocationIntent);
    }

    public static void showLocationDetails(final Context context, final int locationID) {
        Intent locationDetailsIntent = new Intent(context, LocationDetailsActivity.class);
        locationDetailsIntent.putExtra("ITEM", locationID);
        context.startActivity(locationDetailsIntent);
    }

    public static void showGallery(final Context context, final String filename,
                                   final boolean isVideo) {

        Intent galleryIntent = new Intent(context, GalleryActivity.class);
        galleryIntent.putExtra("FILENAME", filename);
        galleryIntent.putExtra("IS_VIDEO", isVideo);
        context.startActivity(galleryIntent);
    }

}
