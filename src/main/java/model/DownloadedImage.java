package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public class DownloadedImage {

    private Long id;
    private String imageHash;
    private String ddcUrl;
    private String externalUrl;
    private int imageProvider;
    private int priority;
    private LocalDateTime createdOn;
    private LocalDateTime lastModifiedOn;

    public DownloadedImage() {
    }

    public DownloadedImage(long id, String ddcUrl, String externalUrl, String imageHash, int imageProvider, int priority, Timestamp createdOn, Timestamp lastModifiedOn) {
        this.id = id;
        this.imageHash = imageHash;
        this.ddcUrl = ddcUrl;
        this.externalUrl = externalUrl;
        this.imageProvider = imageProvider;
        this.priority = priority;

        if(createdOn!=null)
        {
            this.createdOn = createdOn.toLocalDateTime();
        }

        if(lastModifiedOn!=null)
        {
            this.lastModifiedOn = lastModifiedOn.toLocalDateTime();
        }


    }

    public String getImageHash() {

        return imageHash;
    }

    public void setImageHash(String imageHash) {

        this.imageHash = imageHash;
    }

    public String getExternalUrl() {

        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {

        this.externalUrl = externalUrl;
    }

    public String getDdcUrl() {

        return ddcUrl;
    }

    public void setDdcUrl(String ddcUrl) {

        this.ddcUrl = ddcUrl;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public int getImageProvider() {

        return imageProvider;
    }

    public void setImageProvider(int imageProvider) {

        this.imageProvider = imageProvider;
    }

    public int getPriority() {

        return priority;
    }

    public void setPriority(int priority) {

        this.priority = priority;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(LocalDateTime lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

}
