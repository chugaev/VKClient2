package com.anatoly.chugaev.VKAPP.network.Model.LongPollServer;

import com.google.gson.annotations.SerializedName;

public class DataLongPollServer {
    @SerializedName("response")
    private DataLongPollServerResponse mResponse;

    public DataLongPollServerResponse getResponse() {
        return mResponse;
    }
}
