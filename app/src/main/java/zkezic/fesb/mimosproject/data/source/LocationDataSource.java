package zkezic.fesb.mimosproject.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import zkezic.fesb.mimosproject.data.managers.DatabaseManager;
import zkezic.fesb.mimosproject.data.models.Location;

public class LocationDataSource {
    private SQLiteDatabase database;
    private DatabaseManager databaseManager;

    public  LocationDataSource(Context context){
        databaseManager= new DatabaseManager(context);

        try {this.open();} catch (SQLException e){e.printStackTrace();}
    }

    public void open() throws  SQLException{
        database = databaseManager.getWritableDatabase();
    }

    public void	close()	{
        database.close();
    }

    // dodavanje podataka u bazu
    public int addLocToDb(int locationID, String locationName, String address,
                           double latitude, double longitude, String date, String note){

        ContentValues values= new ContentValues();
        values.put("name", locationName);
        values.put("address", address);
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("date", date);
        values.put("note", note);

        if(locationID > 0) {
            database.update("mylocations", values,
                    "_id = " + Integer.toString(locationID), null);
        } else {
           return (int) database.insert("mylocations",null,values);
        }

        return locationID;
    }

    public  void deleteRow(String id){
        database.execSQL("DELETE FROM mylocations WHERE _id="+id+";");
    }

    // dohvacanje zbroja svih lokacija
    public int getLocationsCount(){

        int locationsCount = 0;

        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM mylocations", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            locationsCount = cursor.getInt(0);
            cursor.moveToNext();
        }

        cursor.close();
        return locationsCount;
    }

    // dohvacanje svih lokacija
    public ArrayList<Location> getAllLocations(){

        ArrayList<Location> locationsList= new ArrayList<Location>();

        Cursor cursor = database.rawQuery("SELECT * FROM mylocations", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            Location location= new Location();
            location.setId(cursor.getInt(0));
            location.setName(cursor.getString(1));
            location.setAddress(cursor.getString(2));
            location.setLatitude(cursor.getDouble(3));
            location.setLongitude(cursor.getDouble(4));
            location.setDate(cursor.getString(5));
            location.setNote(cursor.getString(6));

            locationsList.add(location);

            cursor.moveToNext();
        }

        cursor.close();
        return locationsList;
    }

    // dohvacanje lokacije po ID-u
    public Location getLocation(int locationID){

        Location locationFound = new Location();

        Cursor cursor = database.rawQuery("SELECT * FROM mylocations where _id = " + locationID, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            locationFound.setId(cursor.getInt(0));
            locationFound.setName(cursor.getString(1));
            locationFound.setAddress(cursor.getString(2));
            locationFound.setLatitude(cursor.getDouble(3));
            locationFound.setLongitude(cursor.getDouble(4));
            locationFound.setDate(cursor.getString(5));
            locationFound.setNote(cursor.getString(6));

            cursor.moveToNext();
        }
        cursor.close();
        return locationFound;
    }
}
