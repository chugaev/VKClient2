package com.anatoly.chugaev.VKAPP.network.Model.Group;

import com.google.gson.annotations.SerializedName;

public class Group {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("photo_200")
    private String photo200;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPhoto200() {
        return photo200;
    }
}
