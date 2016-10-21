package com.android_map.androidmap.models;

public class MapThumbnailResponse {
        public String Thumbnail;
        public String MapName;
        public double LatitudeMin;
        public double LatitudeMax;
        public double LongitudeMin;
        public double LongitudeMax;

        public String getThumbnail() {
                return Thumbnail;
        }

        public void setThumbnail(String thumbnail) {
                Thumbnail = thumbnail;
        }

        public String getMapName() {
                return MapName;
        }

        public void setMapName(String mapName) {
                MapName = mapName;
        }

        public double getLatitudeMin() {
                return LatitudeMin;
        }

        public void setLatitudeMin(double latitudeMin) {
                LatitudeMin = latitudeMin;
        }

        public double getLatitudeMax() {
                return LatitudeMax;
        }

        public void setLatitudeMax(double latitudeMax) {
                LatitudeMax = latitudeMax;
        }

        public double getLongitudeMin() {
                return LongitudeMin;
        }

        public void setLongitudeMin(double longitudeMin) {
                LongitudeMin = longitudeMin;
        }

        public double getLongitudeMax() {
                return LongitudeMax;
        }

        public void setLongitudeMax(double longitudeMax) {
                LongitudeMax = longitudeMax;
        }
}
