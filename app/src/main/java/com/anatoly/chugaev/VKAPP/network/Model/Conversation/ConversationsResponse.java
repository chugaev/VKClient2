package com.anatoly.chugaev.VKAPP.network.Model.Conversation;

import com.anatoly.chugaev.VKAPP.network.Model.Group.Group;
import com.anatoly.chugaev.VKAPP.network.Model.Profile.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConversationsResponse {
    @SerializedName("count")
    private int mCount;
    @SerializedName("items")
    private List<Conversation> mConversations;
    @SerializedName("profiles")
    private List<User> mUsers;
    @SerializedName("groups")
    private List<Group> mGroups;

    public int getCount() {
        return mCount;
    }

    public List<Conversation> getConversations() {
        return mConversations;
    }

    public List<User> getUsers() {
        return mUsers;
    }

    public List<Group> getGroups() {
        return mGroups;
    }
}
