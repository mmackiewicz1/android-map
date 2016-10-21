package com.android_map.androidmap.models;

public class MapDetailResponse {
    public String mapName;
    public String detailedImage;

    public MapDetailResponse(String mapName, String detailedImage) {
        this.mapName = mapName;
        this.detailedImage = detailedImage;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getDetailedImage() {
        return detailedImage;
    }

    public void setDetailedImage(String detailedImage) {
        this.detailedImage = detailedImage;
    }
}
