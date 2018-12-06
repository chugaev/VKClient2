package com.anatoly.chugaev.VKAPP.network.Model.LongPollServer;

import com.google.gson.annotations.SerializedName;

public class DataLongPollServerResponse {
    @SerializedName("key")
    private String key;
    @SerializedName("server")
    private String server;
    @SerializedName("ts")
    private long ts;
    @SerializedName("pts")
    private String pts;

    public DataLongPollServerResponse(String key, String server, long ts) {
        this.key = key;
        this.server = server;
        this.ts = ts;
    }

    public String getKey() {
        return key;
    }

    public String getServer() {
        return server;
    }

    public long getTs() {
        return ts;
    }

    public String getPts() {
        return pts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }
}
