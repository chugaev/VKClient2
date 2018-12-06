package com.anatoly.chugaev.VKAPP.network.Model.Conversation;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("date")
    private long date;
    @SerializedName("peer_id")
    private long fromId;
    @SerializedName("text")
    private String text;
    @SerializedName("out")
    private int out;
    @SerializedName("id")
    private int id;

    public long getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public long getFromId() {
        return fromId;
    }

    public int getOut() {
        return out;
    }

    public int getId() {
        return id;
    }
}
