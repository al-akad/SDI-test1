package Layout.org.layoutlib;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 23.04.2017.
 */

public class DBConnections extends SQLiteOpenHelper {

    public static final String DbName ="DataBase12.db";

  //  private static final String keyId= "id";
    private static final String KeyRpm = "rpm";
    private static final String keyCurrent = "current";
    private static final String keyVoltage =  "Voltage";
    private static final String keyTemperature = "Temperature";
    private static final String keyTime = "Time";
    private static final String mytable = "Table";
   // static final String COLTYPE = "TYPE" ;
   // static final String COLDATE = "DATE" ;
   // static final String COLTIME = "TIME" ;

    public DBConnections(Context context) {
        super(context, DbName, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table mytable (id INTEGER PRIMARY KEY AUTOINCREMENT,KeyRpm INTEGER, keyCurrent INTEGER, keyVoltage INTEGER, keyTemperature  INTEGER, keyTime INTEGER )");
        //db.execSQL("INSERT INTO "+TABLENAME+" (TYPE,DATE,TIME) VALUES ('testtype','testdate','testtime')");
        //db.execSQL("INSERT INTO "+TABLENAME+" (TYPE,DATE,TIME) VALUES ('testtype2','testdate2','testtime2')");
        //db.execSQL("INSERT INTO "+TABLENAME+" (TYPE,DATE,TIME) VALUES ('testtype3','testdate3','testtime3')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS mytable");
        onCreate(db);

    }



    public boolean insertData(int MachRpm, int MachCurrent, int MachVoltage, int MachTemperature) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("KeyRpm", MachRpm);
        contentValues.put("keyCurrent", MachCurrent);
        contentValues.put("keyVoltage", MachVoltage);
        contentValues.put("keyTemperature", MachTemperature);
        contentValues.put("keyTime", getDateTime());
        //db.insert("Admins", null, contentValues);
        long result = db.insert("mytable", null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }


  public String viewData() {
      SQLiteDatabase db = this.getReadableDatabase();
      String[] columns = new String[]{KeyRpm, keyCurrent};
      Cursor cursor = db.query(mytable, columns, null, null, null, null, null);
      //       StringBuffer stringBuffer = new StringBuffer();
      String result = "";
      int rpmm = cursor.getColumnIndex(KeyRpm);
      int Currengt = cursor.getColumnIndex(keyCurrent);

      for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
          result = result + cursor.getString(rpmm) + "" + cursor.getString(Currengt) + "\n";
      }
      return result;
  }
 //       while (cursor.moveToNext())
 //       {
 //           int krpm = cursor.getInt(1);
 //           int ktime = cursor.getInt(5);
 //           stringBuffer.append(krpm);

//        }
//        return stringBuffer.toString();
//   }




    public Integer[] arry() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM mytable", null);
        int columnIndex = 1;
        Integer[] in = new Integer[c.getCount()];
        if (c.moveToFirst())
        {
            for (int i = 0; i < c.getCount(); i++)
            {
                in[i] = c.getInt(columnIndex);

                c.moveToNext();
            }
        }
        return in;
    }

    public Integer[] arry2() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM mytable", null);
        int columnIndex = 5;
        Integer[] in = new Integer[c.getCount()];
        if (c.moveToFirst())
        {
            for (int i = 0; i < c.getCount(); i++)
            {
                in[i] = c.getInt(columnIndex);

                c.moveToNext();
            }
        }
        return in;
    }



    public Integer[] arry3() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM mytable", null);
        int columnIndex = 3;
        Integer[] in = new Integer[c.getCount()];
        if (c.moveToFirst())
        {
            for (int i = 0; i < c.getCount(); i++)
            {
                in[i] = c.getInt(columnIndex);

                c.moveToNext();
            }
        }
        return in;
    }

    public Integer[] arry4() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM mytable", null);
        int columnIndex = 4;
        Integer[] in = new Integer[c.getCount()];
        if (c.moveToFirst())
        {
            for (int i = 0; i < c.getCount(); i++)
            {
                in[i] = c.getInt(columnIndex);

                c.moveToNext();
            }
        }
        return in;
    }

    public Integer[] arry1() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM mytable", null);
        int columnIndex = 2;
        Integer[] in = new Integer[c.getCount()];
        if (c.moveToFirst())
        {
            for (int i = 0; i < c.getCount(); i++)
            {
                in[i] = c.getInt(columnIndex);

                c.moveToNext();
            }
        }
        return in;
    }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "ddMMyyyyHHmmss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    public ArrayList getAllrecord(){

      ArrayList arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from mytable", null);
        res.moveToFirst();
        while (res.isAfterLast() == false){

            //arrayList.add(0);
            //Timestamp.valueOf(res.getString(0));
           String t1 = res.getString(res.getColumnIndex("id"));
            String t2 = res.getString(1);
            String t3 = res.getString(2);
            String t4 = res.getString(3);
            String t5 = res.getString(4);
            String t6 = res.getString(5);
           arrayList.add( t1 + "-"+t2 +"-"+t3+"-"+t4+ "-"+t5+"-"+t6  );
            res.moveToNext();
        }
        return arrayList;

    }

    public boolean updateData(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        //db.insert("Admins", null, contentValues);
        db.update("mytable", contentValues, "id= ?", new String[]{id});
        return true;
    }


    protected Integer Delete(String id){

        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete("mytable", "id= ?", new String[]{id});
    }
}