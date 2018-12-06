package com.anatoly.chugaev.VKAPP.network.Model.Profile;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private int id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("photo_200")
    private String photo200;
    @SerializedName("last_seen")
    private LastSeen mLastSeen;
    @SerializedName("deactivated")
    private String deleted;
    @SerializedName("online")
    private int online;
    @SerializedName("online_app")
    private long onlineApp;

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoto200() {
        return photo200;
    }

    public LastSeen getLastSeen() {
        return mLastSeen;
    }

    public String getDeleted() {
        return deleted;
    }

    public int getOnline() {
        return online;
    }

    public Long getOnlineApp() {
        return onlineApp;
    }

    public class LastSeen {
        @SerializedName("time")
        private long time;
        @SerializedName("platform")
        private int platform;

        public long getTime() {
            return time;
        }

        public int getPlatform() {
            return platform;
        }
    }
}
