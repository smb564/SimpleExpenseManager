package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

/**
 * Created by Supun on 11/20/2016.
 */
public class PersistentExpenseManager extends ExpenseManager{

    private Context context;

    public PersistentExpenseManager(Context context){
        this.context = context;
        try {
            setup();
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setup() throws ExpenseManagerException {
        super.setAccountsDAO(new PersistantAccountDAO(context));
        super.setTransactionsDAO(new PersistentTransactionDAO(context));
    }
}
