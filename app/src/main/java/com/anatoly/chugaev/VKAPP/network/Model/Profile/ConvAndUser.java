package com.anatoly.chugaev.VKAPP.network.Model.Profile;

import com.anatoly.chugaev.VKAPP.network.Model.Conversation.Conversation;
import com.anatoly.chugaev.VKAPP.network.Model.Group.Group;

public class ConvAndUser {

    private Conversation mConversation;
    private User mUser;
    private Group mGroup;

    public ConvAndUser(Conversation conversation, User user, Group group) {
        this.mConversation = conversation;
        this.mUser = user;
        this.mGroup = group;
    }

    public Conversation getConversation() {
        return mConversation;
    }

    public User getUser() {
        return mUser;
    }

    public Group getGroup() {
        return mGroup;
    }
}
