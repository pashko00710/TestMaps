package me.uptop.testmaps2.data.storage.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import me.uptop.testmaps2.data.storage.dto.PointsDto;

public class PointsRealm extends RealmObject {
    @PrimaryKey
    private String id;
    private String title;
    private String description;
    private double latitude;
    private double longitude;

    public PointsRealm() {
    }

    public PointsRealm(PointsDto pointsDto) {
        this.id = String.valueOf(pointsDto.getId());
        this.title = pointsDto.getTitle();
        this.description = pointsDto.getDescription();
        this.latitude = pointsDto.getLatitude();
        this.longitude = pointsDto.getLongitude();
    }
//
//    public PointsRealm(String id, String title, String description, double latitude, double longitude) {
//        this.id = id;
//        this.title = title;
//        this.description = description;
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
