package com.cq.daemon.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class AccountHelper {

    private static final String TAG = "AccountHelper";

    private static final String ACCOUNT_TYPE = "com.cq.daemon.account";

    /**
     * 添加账号
     *
     * @param context
     */
    public static void addAccount(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // 获得此类型的账户
        // 需要增加权限  GET_ACCOUNTS
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);

        if (accounts.length > 0) {
            Log.e(TAG, "账户已存在");
            return;
        }
        Account account = new Account("cq", ACCOUNT_TYPE);
        // 给这个账户类型添加一个账户
        // 需要增加权限  AUTHENTICATE_ACCOUNTS
        accountManager.addAccountExplicitly(account, "cq", new Bundle());
    }

    /**
     * 设置账户自动同步
     */
    public static void autoSync() {
        Account account = new Account("cq", ACCOUNT_TYPE);

        // 下面三个都需要同一个权限  WRITE_SYNC_SETTINGS

        // 设置同步
        ContentResolver.setIsSyncable(account, "com.cq.daemon.provider", 1);

        // 自动同步
        ContentResolver.setSyncAutomatically(account, "com.cq.daemon.provider", true);

        // 设置同步周期
        ContentResolver.addPeriodicSync(account, "com.cq.daemon.provider", new Bundle(), 1);
    }

}
