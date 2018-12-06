package com.anatoly.chugaev.VKAPP.utils;

import android.support.v7.util.DiffUtil;

import com.anatoly.chugaev.VKAPP.network.Model.Profile.ConvAndUser;

import java.util.List;

public class ProductDiffUtilCallback extends DiffUtil.Callback {
    private final List<ConvAndUser> oldList;
    private final List<ConvAndUser> newList;

    public ProductDiffUtilCallback(List<ConvAndUser> oldList, List<ConvAndUser> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        ConvAndUser oldConv = oldList.get(oldItemPosition);
        ConvAndUser newConv = newList.get(newItemPosition);
        return oldConv.getConversation().getConv().getPeer().getId()
                == newConv.getConversation().getConv().getPeer().getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        ConvAndUser oldConv = oldList.get(oldItemPosition);
        ConvAndUser newConv = newList.get(newItemPosition);
        return oldConv.getConversation().getMessage().getText()
                .equals(newConv.getConversation().getMessage().getText());
    }
}
