package com.example.ece651.domain;

import java.util.Objects;

public class homebody {
    private Post post;
    private Boolean whether_liked;
    public homebody(Post post, Boolean whether_liked){
        this.post = post;
        this.whether_liked = whether_liked;
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
