package zkezic.fesb.mimosproject.data.managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseManager extends SQLiteOpenHelper {

    private static int DATABASE_VERSION=2;

    // Database creation sql statement
    private  static final String DATABASE_CREATE ="create table mylocations"
            +"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            +"name TEXT NOT NULL,"
            +"address TEXT NOT NULL,"
            +"latitude REAL NOT NULL,"
            +"longitude REAL NOT NULL,"
            +"date TEXT NOT NULL,"
            +"note TEXT NOT NULL);";

    public DatabaseManager(Context context) {
        super(context, "locationsdatabase.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseManager.class.getName(),
                "Upgrading database from version" + oldVersion+ "to " + newVersion+
                        ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS mylocations");
        onCreate(db);
    }
}
