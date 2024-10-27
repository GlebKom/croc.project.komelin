package ru.komelin.crocprojectkomelin.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "link")
public class Link {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "link_address")
    private String linkAddress;

    @Min(0)
    @Column(name = "download_limit")
    private int downloadLimit;

    @Column(name = "lifetime")
    private LocalDateTime lifetime;

    @Min(1)
    @Column(name = "request_per_second")
    private int requestPerSecond;

    @OneToMany(mappedBy = "link", cascade = CascadeType.ALL)
    private List<Photo> photos;

    public Link() {
    }

    public Link(String linkAddress, int downloadLimit, LocalDateTime lifetime, int requestPerSecond) {
        this.linkAddress = linkAddress;
        this.downloadLimit = downloadLimit;
        this.lifetime = lifetime;
        this.requestPerSecond = requestPerSecond;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public int getDownloadLimit() {
        return downloadLimit;
    }

    public void setDownloadLimit(int downloadLimit) {
        this.downloadLimit = downloadLimit;
    }

    public LocalDateTime getLifetime() {
        return lifetime;
    }

    public void setLifetime(LocalDateTime lifetime) {
        this.lifetime = lifetime;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public int getRequestPerSecond() {
        return requestPerSecond;
    }

    public void setRequestPerSecond(int requestPerSecond) {
        this.requestPerSecond = requestPerSecond;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return id == link.id && downloadLimit == link.downloadLimit && requestPerSecond == link.requestPerSecond && Objects.equals(linkAddress, link.linkAddress) && Objects.equals(lifetime, link.lifetime) && Objects.equals(photos, link.photos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, linkAddress, downloadLimit, lifetime, requestPerSecond, photos);
    }
}
