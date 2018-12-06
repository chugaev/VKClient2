package com.anatoly.chugaev.VKAPP.network.Model.Conversation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageResponse {
    @SerializedName("count")
    private int count;
    @SerializedName("items")
    private List<Message> mMessages;

    public int getCount() {
        return count;
    }

    public List<Message> getMessages() {
        return mMessages;
    }
}
