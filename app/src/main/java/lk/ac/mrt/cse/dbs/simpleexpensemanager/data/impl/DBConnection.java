package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.ConnectException;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

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

        // Auto increment is implicitly added to INTEGER PRIMARY KEY when the value is not explicitly given
        // Type, 0 shows a Expense, 1 shows a Income
        db.execSQL("CREATE TABLE IF NOT EXISTS Transactions(" +
                "Transaction_id INTEGER PRIMARY KEY," +
                "Acc_No TEXT," +
                "Amount REAL," +
                "Date DATE," +
                "Type INTEGER," +
                "FOREIGN KEY (Acc_No) REFERENCES Account(Acc_No))");

//        // For testing purposes
//        db.execSQL("INSERT INTO Account VALUES ('123456z', 'BOC','Supun Abeysinghe', 5)");
//        db.execSQL("INSERT INTO Account VALUES ('12s456z', 'BOsdaC','Supuasdn Absadeysinghe', 10000)");
//        db.execSQL("INSERT INTO Account VALUES ('1ss56z', 'BOsC','Supuasdn Abeysinghe', 10000)");
//
//        db.execSQL("INSERT INTO Transactions VALUES ('0001','12s456z', 500, '19-05-1994')");

    }

    public static synchronized DBConnection getInstance(Context context){
        if (connection == null){
            connection = new DBConnection(context);
        }
        return connection;
    }

    public SQLiteDatabase getDatabase(){
        return db;
    }
}
