package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Supun on 11/20/2016.
 */
public class PersistantAccountDAO implements AccountDAO {

    private Context context;

    public PersistantAccountDAO(Context context){
        this.context = context;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accounts = new ArrayList<>();

        SQLiteDatabase db = DBConnection.getInstance(context).getDatabase();
        String sql = "SELECT Acc_No FROM Account";
        Cursor cursor = db.rawQuery(sql, null);


        while(cursor.moveToNext()){
            accounts.add(cursor.getString(cursor.getColumnIndex("Acc_No")));
        }

        return accounts;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<>();

        SQLiteDatabase db = DBConnection.getInstance(context).getDatabase();
        String sql = "SELECT * FROM Account";
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            accounts.add(new Account(cursor.getString(cursor.getColumnIndex("Acc_No")),
                    cursor.getString(cursor.getColumnIndex("Bank")),
                    cursor.getString(cursor.getColumnIndex("Holder")),
                    cursor.getDouble(cursor.getColumnIndex("Init_amount"))
            ));

        }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = DBConnection.getInstance(context).getDatabase();
        String sql = "SELECT * FROM Account WHERE Acc_No="+accountNo;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToNext()){
            return new Account(cursor.getString(cursor.getColumnIndex("Acc_No")),
                    cursor.getString(cursor.getColumnIndex("Bank")),
                    cursor.getString(cursor.getColumnIndex("Holder")),
                    cursor.getDouble(cursor.getColumnIndex("Init_amount"))
            );
        }else{
            throw new InvalidAccountException(accountNo+" is not available");
        }
    }

    @Override
    public void addAccount(Account account) {
        // Use prepared statements to prevent injections
        String sql = "INSERT INTO Account VALUES (?,?,?,?)";
        SQLiteStatement qry = DBConnection.getInstance(context).getDatabase().compileStatement(sql);

        // Set the values
        qry.bindString(1, account.getAccountNo());
        qry.bindString(2, account.getBankName());
        qry.bindString(3, account.getAccountHolderName());
        qry.bindDouble(4, account.getBalance());

        qry.executeInsert();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sql = "DELETE FROM Account WHERE Acc_No="+accountNo;
        SQLiteStatement qry = DBConnection.getInstance(context).getDatabase().compileStatement(sql);

        qry.bindString(1, accountNo);

        qry.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String sql = "UPDATE Account SET Init_amount = Init_amount + ? WHERE Acc_No = ?";
        SQLiteStatement qry = DBConnection.getInstance(context).getDatabase().compileStatement(sql);

        if (expenseType == ExpenseType.EXPENSE){
            qry.bindDouble(1, -amount);
            qry.bindString(2, accountNo);
        }else{
            qry.bindDouble(1, amount);
            qry.bindString(2, accountNo);
        }
    }
}