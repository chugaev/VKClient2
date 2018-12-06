package com.anatoly.chugaev.VKAPP.network.Model.Conversation;

import com.google.gson.annotations.SerializedName;

public class MessageBaseResponse {
    @SerializedName("response")
    private MessageResponse mResponse;

    public MessageResponse getResponse() {
        return mResponse;
    }
}
