package com.magisterka.geolokalizator_client.models.accountmodels;

public class AccountInfoModel {
    private int userID;
    private String name;
    private int roleID;

    public int getRoleId() {
        return roleID;
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String nName) {
        name = nName;
    }

    public void setRoleId(int nRoleId) {
        roleID = nRoleId;
    }

    public void setUserID(int nUserID) {
        userID = nUserID;
    }
}
