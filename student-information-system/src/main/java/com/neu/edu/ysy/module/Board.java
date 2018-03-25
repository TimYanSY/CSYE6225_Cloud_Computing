package com.neu.edu.ysy.module;

import java.util.LinkedHashSet;
import java.util.Set;

public class Board {
    private Set<String> image;
    private Set<String> annoucement;

    public Board() {
        image = new LinkedHashSet<String>();
        annoucement = new LinkedHashSet<String>();
    }

    public Set<String> getImage() {
        return image;
    }

    public Set<String> getAnnoucement() {
        return annoucement;
    }

    public boolean addImage(String image) {
        return this.image.add(image);
    }

    public boolean addAnnouncement(String announce) {
        return this.annoucement.add(announce);
    }
}
