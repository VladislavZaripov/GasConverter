package Gasconverter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DB {

    private final Context context;
    @SuppressLint("StaticFieldLeak")
    private static DBHelper dbHelper;
    private static SQLiteDatabase db;
    public static Cursor cursor;
    public static ContentValues cv;
    public Activity activity;

    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "DataBaseGasConverter v2";
    public static final String TABLE1_NAME = "Vehicle";
    public static final String TABLE1_COLUMN_ID = "_id";
    public static final String TABLE1_COLUMN_GROUP = "GroupV";
    public static final String TABLE1_COLUMN_BRAND = "Brand";
    public static final String TABLE1_COLUMN_NUMBER = "Number";

    public static final String TABLE2_NAME = "Balloons";
    public static final String TABLE2_COLUMN_ID = "_id";
    public static final String TABLE2_COLUMN_ID_VEHICLE = "Vehicle_id";
    public static final String TABLE2_COLUMN_GAS = "Gas";
    public static final String TABLE2_COLUMN_VOLUME = "Volume";

    private static final String TABLE1_CREATE = "create table " + TABLE1_NAME +
            "(" +
            TABLE1_COLUMN_ID + " integer primary key autoincrement, " +
            TABLE1_COLUMN_GROUP + " text, " +
            TABLE1_COLUMN_BRAND + " text, " +
            TABLE1_COLUMN_NUMBER + " text" +
            ");";

    private static final String TABLE2_CREATE = "create table " + TABLE2_NAME +
            "(" +
            TABLE2_COLUMN_ID + " integer primary key autoincrement, " +
            TABLE2_COLUMN_ID_VEHICLE + " integer, " +
            TABLE2_COLUMN_GAS + " text, " +
            TABLE2_COLUMN_VOLUME + " integer" +
            ");";

    public DB(Context context) {
        this.context = context;
    }

    public void open() {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
        Log.d(MainActivity.LOG, "DB_open");
    }

    public void close() {
        if (dbHelper != null) dbHelper.close();
        Log.d(MainActivity.LOG, "DB_close");
    }

    public static ArrayList <String> getFilterResult(String group, String brand) {
        Log.d(MainActivity.LOG, "group = " + group + "; brand = " + brand);

        String selection = null;
        String[] selectionArgs = null;
        ArrayList<String> strings = new ArrayList<>();

        int sumFilters = 0;

        if (!group.equals("Все")) {
            selection = TABLE1_COLUMN_GROUP + " = ?";
            strings.add(group);
            sumFilters++;
        }

        if (!brand.equals("Все")) {
            if (sumFilters > 0) {
                selection = selection.concat(" AND " + TABLE1_COLUMN_BRAND + " = ?");
            } else {
                selection = TABLE1_COLUMN_BRAND + " = ?";
            }
            strings.add(brand);
            sumFilters++;
        }

        if (sumFilters > 0) {
            selectionArgs = new String[sumFilters];
            selectionArgs = strings.toArray(selectionArgs);
        }

        ArrayList <String> filterResult = new ArrayList<>();
        Cursor cursor = db.query(TABLE1_NAME, null, selection, selectionArgs, null, null, TABLE1_COLUMN_ID + " DESC");
        if (cursor.moveToFirst()) {
            do {
                filterResult.add(cursor.getString(cursor.getColumnIndex(DB.TABLE1_COLUMN_NUMBER)));
                Log.d(MainActivity.LOG, cursor.getString(cursor.getColumnIndex(DB.TABLE1_COLUMN_NUMBER)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return filterResult;
    }

    public static Cursor getTable1Pos(String number) {
        return cursor = db.query(TABLE1_NAME, null, TABLE1_COLUMN_NUMBER + " = ?", new String[]{number}, null, null, null);
    }

    public static Cursor getTable2Pos(int id) {
        return cursor = db.query(TABLE2_NAME, null, TABLE2_COLUMN_ID_VEHICLE + " = ?", new String[]{Integer.toString(id)}, null, null, null);
    }

    public static ArrayList <List <String>> getFilter() {
        ArrayList <List <String>> filter = new ArrayList<>();

        Set <String> Group = new TreeSet<>();
        Set <String> Brand = new TreeSet<>();

        cursor = db.query(TABLE1_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Group.add(cursor.getString(cursor.getColumnIndex(DB.TABLE1_COLUMN_GROUP)));
                Brand.add(cursor.getString(cursor.getColumnIndex(DB.TABLE1_COLUMN_BRAND)));
                Log.d(MainActivity.LOG, "getFilter: " + cursor.getString(cursor.getColumnIndex(DB.TABLE1_COLUMN_GROUP)) + " - " + cursor.getString(cursor.getColumnIndex(DB.TABLE1_COLUMN_BRAND)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        List<String> groupList = new ArrayList<>();
        groupList.add("Все");
        groupList.addAll(Group);

        List<String> brandList = new ArrayList<>();
        brandList.add("Все");
        brandList.addAll(Brand);

        filter.add(groupList);
        filter.add(brandList);

        return filter;
    }

    public static int addPositionInTable1(String group, String brand, String number) {
        cv = new ContentValues();
        cv.put(TABLE1_COLUMN_GROUP, group);
        cv.put(TABLE1_COLUMN_BRAND, brand);
        cv.put(TABLE1_COLUMN_NUMBER, number);
        int id = (int) db.insert(TABLE1_NAME, null, cv);
        cv.clear();
        return id;
    }

    public static void addPositionInTable2(Integer vehicle_id, String gas, Integer volume) {
        cv = new ContentValues();
        cv.put(TABLE2_COLUMN_ID_VEHICLE, vehicle_id);
        cv.put(TABLE2_COLUMN_GAS, gas);
        cv.put(TABLE2_COLUMN_VOLUME, volume);
        db.insert(TABLE2_NAME, null, cv);
        cv.clear();
    }

    public static void updatePositionInTable1(Integer id, String group, String brand, String number) {
        cv = new ContentValues();
        cv.put(TABLE1_COLUMN_GROUP, group);
        cv.put(TABLE1_COLUMN_BRAND, brand);
        cv.put(TABLE1_COLUMN_NUMBER, number);
        db.update(TABLE1_NAME, cv, TABLE1_COLUMN_ID + "= ?", new String[]{id.toString()});
        cv.clear();
    }

    public static void updatePositionInTable2(Integer id, Integer vehicle_id, String gas, Integer volume) {
        cv = new ContentValues();
        cv.put(TABLE2_COLUMN_ID_VEHICLE, vehicle_id);
        cv.put(TABLE2_COLUMN_GAS, gas);
        cv.put(TABLE2_COLUMN_VOLUME, volume);
        db.update(TABLE2_NAME, cv, TABLE2_COLUMN_ID + "= ?", new String[]{id.toString()});
        cv.clear();
    }

    public static void deleteRowTable2(int id) {
        int delCount;
        delCount = db.delete(TABLE2_NAME, TABLE2_COLUMN_ID + " = " + id, null);
        Log.d(MainActivity.LOG, "deleteRowTable2: id = " + id + ", count = " + delCount);
    }

    public static void deletePosition(int id) {
        int delCount;
        delCount = db.delete(TABLE1_NAME, TABLE1_COLUMN_ID + " = " + id, null);
        Log.d(MainActivity.LOG, "deletePosition Table1: id = " + id + ", count = " + delCount);
        delCount = db.delete(TABLE2_NAME, TABLE2_COLUMN_ID_VEHICLE + " = " + id, null);
        Log.d(MainActivity.LOG, "deletePosition Table2: id_recipe = " + id + ", count = " + delCount);
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            activity = (Activity) context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE1_CREATE);
            Log.d(MainActivity.LOG, "TABLE1_CREATE");
            db.execSQL(TABLE2_CREATE);
            Log.d(MainActivity.LOG, "TABLE2_CREATE");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}