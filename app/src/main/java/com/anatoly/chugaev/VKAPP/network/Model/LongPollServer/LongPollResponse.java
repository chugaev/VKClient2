package com.anatoly.chugaev.VKAPP.network.Model.LongPollServer;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LongPollResponse {
    @SerializedName("ts")
    private long ts;
    @SerializedName("updates")
    private List<List<String>> updates;

    public long getTs() {
        return ts;
    }

    public List<List<String>> getUpdates() {
        return updates;
    }
}
