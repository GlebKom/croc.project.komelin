package ru.komelin.crocprojectkomelin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "photo")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "link")
    private String hash;

    public Photo(){}

    public Photo(String name, String link) {
        this.name = name;
        this.hash = link;
    }

    public Photo(String link) {
        this.hash = link;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String link) {
        this.hash = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
