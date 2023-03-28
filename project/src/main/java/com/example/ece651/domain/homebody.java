package com.example.ece651.domain;

import java.util.Objects;

public class homebody {
    private Post post;
    private Boolean whether_liked;
    private Boolean whether_followed_post_user;

    public Boolean getWhether_followed_post_user() {
        return whether_followed_post_user;
    }

    public void setWhether_followed_post_user(Boolean whether_followed_post_user) {
        this.whether_followed_post_user = whether_followed_post_user;
    }

    public homebody(Post post, Boolean whether_liked, Boolean whether_followed_post_user){
        this.post = post;
        this.whether_liked = whether_liked;
        this.whether_followed_post_user = whether_followed_post_user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Boolean getWhether_liked() {
        return whether_liked;
    }

    public void setWhether_liked(Boolean whether_liked) {
        this.whether_liked = whether_liked;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof homebody))
            return false;
        homebody userNode = (homebody) o;
        return Objects.equals(post, userNode.post);
    }
}
