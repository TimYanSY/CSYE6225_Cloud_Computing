package com.neu.edu.ysy.module;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Lecture {
    private List<String> notes;
    private List<String> materials;

    public Lecture() {
        this.notes = new ArrayList<String>();
        this.materials = new ArrayList<String>();
    }

    public List<String> getNotes() {
        return notes;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public boolean addNote(String note) {
        return this.notes.add(note);
    }

    public boolean addMaterial(String material) {
        return this.materials.add(material);
    }
}
