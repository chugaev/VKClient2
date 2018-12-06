package com.anatoly.chugaev.VKAPP.network.Model.Conversation;

import com.google.gson.annotations.SerializedName;

public class ConversationBaseResponse {
    @SerializedName("response")
    private ConversationsResponse mResponse;

    public ConversationsResponse getResponse() {
        return mResponse;
    }
}
