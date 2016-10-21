package com.android_map.androidmap.models;

public class MapThumbnailResponse {
        private String thumbnail;
        private String mapName;
        private double latitudeMin;
        private double latitudeMax;
        private double longitudeMin;
        private double longitudeMax;

        public MapThumbnailResponse(
                        String thumbnail,
                        String mapName,
                        double latitudeMin,
                        double latitudeMax,
                        double longitudeMin,
                        double longitudeMax) {
                this.thumbnail = thumbnail;
                this.mapName = mapName;
                this.latitudeMin = latitudeMin;
                this.latitudeMax = latitudeMax;
                this.longitudeMin = longitudeMin;
                this.longitudeMax = longitudeMax;
        }

        public String getThumbnail() {
                return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
                thumbnail = thumbnail;
        }

        public String getMapName() {
                return mapName;
        }

        public void setMapName(String mapName) {
                mapName = mapName;
        }

        public double getLatitudeMin() {
                return latitudeMin;
        }

        public void setLatitudeMin(double latitudeMin) {
                latitudeMin = latitudeMin;
        }

        public double getLatitudeMax() {
                return latitudeMax;
        }

        public void setLatitudeMax(double latitudeMax) {
                latitudeMax = latitudeMax;
        }

        public double getLongitudeMin() {
                return longitudeMin;
        }

        public void setLongitudeMin(double longitudeMin) {
                longitudeMin = longitudeMin;
        }

        public double getLongitudeMax() {
                return longitudeMax;
        }

        public void setLongitudeMax(double longitudeMax) {
               longitudeMax = longitudeMax;
        }
}
