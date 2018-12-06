package com.anatoly.chugaev.VKAPP.network.Model.Profile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsersBaseResponse {
    @SerializedName("response")
    private List<User> mUsers;

    public List<User> getUsers() {
        return mUsers;
    }
}
