package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Supun on 11/20/2016.
 */
public class PersistentTransactionDAO implements TransactionDAO{
    private Context context;

    public PersistentTransactionDAO(Context context){
        this.context = context;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String sql = "INSERT INTO Transactions (Acc_No, Amount, Date, Type) VALUES (?, ?, ?, ?)";
        SQLiteStatement qry = DBConnection.getInstance(context).getDatabase().compileStatement(sql);

        qry.bindString(1, accountNo);
        qry.bindDouble(2, amount);
        qry.bindString(3, new SimpleDateFormat("dd-MM-yyyy").format(date));
        if (expenseType==ExpenseType.EXPENSE)
            qry.bindLong(4, 0);
        else
            qry.bindLong(4, 1);

        qry.executeInsert();

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT * FROM Transactions";

        Cursor cursor = DBConnection.getInstance(context).getDatabase().rawQuery(sql, null);

        while(cursor.moveToNext()){
            Date parsedDate = null;
            try {
                parsedDate = new SimpleDateFormat("dd-MM-yyyy").parse(cursor.getString(cursor.getColumnIndex("Date")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transactions.add(new Transaction(parsedDate,
                    cursor.getString(cursor.getColumnIndex("Acc_No")),
                    (cursor.getInt(cursor.getColumnIndex("Type"))==0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                    cursor.getDouble(cursor.getColumnIndex("Amount"))
            ));
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        // Newest entries are taken (ordered by date)
        List<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT * FROM Transactions ORDER BY Date DESC LIMIT "+limit;

        Cursor cursor = DBConnection.getInstance(context).getDatabase().rawQuery(sql, null);

        while(cursor.moveToNext()){
            Date parsedDate = null;
            try {
                parsedDate = new SimpleDateFormat("dd-MM-yyyy").parse(cursor.getString(cursor.getColumnIndex("Date")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transactions.add(new Transaction(parsedDate,
                    cursor.getString(cursor.getColumnIndex("Acc_No")),
                    (cursor.getInt(cursor.getColumnIndex("Type"))==0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                    cursor.getDouble(cursor.getColumnIndex("Amount"))
            ));
        }
        return transactions;
    }
}