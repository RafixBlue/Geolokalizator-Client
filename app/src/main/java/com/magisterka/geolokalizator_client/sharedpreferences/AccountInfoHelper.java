package com.magisterka.geolokalizator_client.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.magisterka.geolokalizator_client.database.DatabaseAccount;
import com.magisterka.geolokalizator_client.database.DbContext;
import com.magisterka.geolokalizator_client.models.accountmodels.AccountInfoModel;

import static android.content.Context.MODE_PRIVATE;

public class AccountInfoHelper {

    private static String ACCOUNT_PREFERENCES = "account";
    private static String TOKEN = "token";
    private static String ROLE ="role";
    private static String CLIENT_USER_ID = "clientuserid";
    private static String USER_NAME = "username";

    private SharedPreferences accountPreferences;
    private DatabaseAccount databaseAccount;

    public AccountInfoHelper(Context context)
    {
        databaseAccount = new DatabaseAccount(context);
        accountPreferences = context.getSharedPreferences(ACCOUNT_PREFERENCES, MODE_PRIVATE);
    }

    public void saveToken(String token)
    {
        SharedPreferences.Editor editor = accountPreferences.edit();
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public void saveAccountInfo(AccountInfoModel accountInfo)
    {
        SharedPreferences.Editor editor = accountPreferences.edit();
        editor.putInt(ROLE, accountInfo.getRoleId());
        editor.putString(USER_NAME, accountInfo.getName());
        editor.commit();
    }

    public int getClientUserId()
    {
        int userId = accountPreferences.getInt(CLIENT_USER_ID,1);
        return userId;
    }

    public int getRoleId()
    {
        int roleId = accountPreferences.getInt(ROLE,1);
        return roleId;
    }

    public String getUserName()
    {
        String userName = accountPreferences.getString(USER_NAME,"");
        return userName;
    }

    public String getToken()
    {
        String token = accountPreferences.getString(TOKEN,null);
        return "Bearer " + token;
    }


    public void CreateNewUser()
    {

        int roleId = getRoleId();
        String userName = getUserName();

        boolean userExists = databaseAccount.userExistCheck(userName);

        if(!userExists) {
            int clientUserId = databaseAccount.insertUser(roleId,userName);

            saveClientUserId(clientUserId);

        }

    }
    private void saveClientUserId(int clientUserId)
    {
        SharedPreferences.Editor editor = accountPreferences.edit();
        editor.putInt(CLIENT_USER_ID, clientUserId);
        editor.commit();
    }
}
