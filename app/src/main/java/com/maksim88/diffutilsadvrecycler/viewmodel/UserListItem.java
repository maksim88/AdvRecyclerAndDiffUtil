package com.maksim88.diffutilsadvrecycler.viewmodel;

/**
 * Created by maksim on 22.10.16.
 */

public class UserListItem {

    private long id;

    private String imageUrl;

    private String username;

    public UserListItem(String imageUrl, String username) {
        this.imageUrl = imageUrl;
        this.username = username;
        //Random gen = new Random();
        //id = gen.nextLong();
        id = createHashId(username + imageUrl);
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private long createHashId(String s) {
        long h = 0;
        for (int i = 0; i < s.length(); i++) {
            h = 31 * h + s.charAt(i);
        }
        return h;
    }

    @Override
    public boolean equals(Object o) {
        if ( o == null ) {
            return false;
        }
        if ( o == this ) {
            return true;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        UserListItem that = (UserListItem) o;
        return    this.getUsername().equals(that.getUsername())
                && this.getImageUrl().equals(that.getImageUrl());
    }
}
