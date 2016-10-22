package com.maksim88.diffutilsadvrecycler.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by maksim on 22.10.16.
 */

public class Result {

    @SerializedName("results")
    @Expose
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
