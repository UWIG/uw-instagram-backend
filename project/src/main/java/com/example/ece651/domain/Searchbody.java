package com.example.ece651.domain;

import java.util.Objects;

public class Searchbody {
    private Media avatar;
    private boolean isFollowing;
    private String username;
    public Searchbody(Media avatar, boolean isFollowing, String username){
        this.avatar =avatar;
        this.isFollowing =isFollowing;
        this.username = username;
    }

    public Media getAvatar() {
        return avatar;
    }

    public void setAvatar(Media avatar) {
        this.avatar = avatar;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Searchbody))
            return false;
        Searchbody userNode = (Searchbody) o;
        return Objects.equals(username, userNode.username);
    }
}
