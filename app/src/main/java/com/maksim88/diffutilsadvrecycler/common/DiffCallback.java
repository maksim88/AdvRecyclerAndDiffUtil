package com.maksim88.diffutilsadvrecycler.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.maksim88.diffutilsadvrecycler.adapter.SwipeableAdapter;
import com.maksim88.diffutilsadvrecycler.viewmodel.UserListItem;

import java.util.List;

/**
 * Created by maksim on 22.10.16.
 */

public class DiffCallback extends DiffUtil.Callback {

    private List<UserListItem> oldUsers;
    private List<UserListItem> newUsers;

    public DiffCallback(List<UserListItem> oldUsers, List<UserListItem> newUsers) {
        this.oldUsers = oldUsers;
        this.newUsers = newUsers;
    }


    @Override
    public int getOldListSize() {
        return oldUsers.size();
    }

    @Override
    public int getNewListSize() {
        return newUsers.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        boolean res = oldUsers.get(oldItemPosition).getId() == newUsers.get(newItemPosition).getId();
        //Timber.d("Items same? %s", res);
        return res;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        boolean res = oldUsers.get(oldItemPosition).equals(newUsers.get(newItemPosition));
        //Timber.d("Contents same? %s", res);
        return res;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        UserListItem newItem = newUsers.get(newItemPosition);
        UserListItem oldItem = oldUsers.get(oldItemPosition);
        Bundle diffBundle = new Bundle();
        if (!newItem.getImageUrl().equals(oldItem.getImageUrl())) {
            diffBundle.putString(SwipeableAdapter.KEY_IMAGEURL, newItem.getImageUrl());
        }
        if (!newItem.getUsername().equals(oldItem.getUsername())) {
            diffBundle.putString(SwipeableAdapter.KEY_USERNAME, newItem.getUsername());
        }
        if (diffBundle.size() == 0) return null;
        return diffBundle;
    }
}
