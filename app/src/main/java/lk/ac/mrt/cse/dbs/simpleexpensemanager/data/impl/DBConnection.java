package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.ConnectException;

/**
 * Created by Supun on 11/20/2016.
 */
public class DBConnection{

    private static DBConnection connection;
    private SQLiteDatabase db;

    private DBConnection(Context context){
        db = context.openOrCreateDatabase("140009A", context.MODE_PRIVATE, null);

        // if the table does not exists create tables
        db.execSQL("CREATE TABLE IF NOT EXISTS Account(" +
                "Acc_No TEXT PRIMARY KEY," +
                "Bank TEXT," +
                "Holder TEXT," +
                "Init_amount REAL" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS Transaction(" +
                "Transaction_id INTEGER PRIMARY KEY," +
                "Acc_No TEXT," +
                "Amount REAL," +
                "Date DATE," +
                "FOREIGN KEY (Acc_No) REFERENCES Account(Acc_No)");

    }

    public static synchronized DBConnection getInstance(Context context){
        if (connection == null){
            connection = new DBConnection(context);
        }
        return connection;
    }



}
