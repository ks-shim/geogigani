package dwayne.shim.geogigani.indexing.distance;

import dwayne.shim.geogigani.common.util.LocationDistance;

import java.util.*;

public class LocationDistanceCalculator {

    private List<LocationMapInfo> locationMapInfoList;

    public LocationDistanceCalculator() {
        locationMapInfoList = new ArrayList<>();
    }

    public void addAndCalculateDistance(String id,
                                        double latitude,
                                        double longitude,
                                        double distanceLimit) {

        LocationMapInfo newMapInfo = new LocationMapInfo(id, latitude, longitude);
        for(LocationMapInfo oldMapInfo : locationMapInfoList)
            oldMapInfo.saveIfDistanceIs(distanceLimit, newMapInfo);

        locationMapInfoList.add(newMapInfo);
    }

    public Map<String, LocationMapInfo> asMap() {
        Map<String, LocationMapInfo> map = new HashMap<>();
        for(LocationMapInfo mapInfo : locationMapInfoList)
            map.put(mapInfo.id, mapInfo);
        return map;
    }

    public static class LocationMapInfo {
        String id;
        double latitude;
        double longitude;

        List<ShortDistancesInfo> shortDistancesInfoList;

        LocationMapInfo(String id,
                        double latitude,
                        double longitude) {
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;

            shortDistancesInfoList = new ArrayList<>();
        }

        void saveIfDistanceIs(double distanceLimit,
                              LocationMapInfo newMapInfo) {
            double distance = LocationDistance.distance(
                    this.latitude, this.longitude,
                    newMapInfo.latitude, newMapInfo.longitude);
            if(distance > distanceLimit || Double.isNaN(distance)) return;

            shortDistancesInfoList.add(new ShortDistancesInfo(newMapInfo.id, distance));
            newMapInfo.addShortDistanceInfo(new ShortDistancesInfo(this.id, distance));
        }

        void addShortDistanceInfo(ShortDistancesInfo distancesInfo) {
            shortDistancesInfoList.add(distancesInfo);
        }

        List<ShortDistancesInfo> getShortDistancesInfoList(double distanceFrom, double distanceTo) {
            List<ShortDistancesInfo> subList = new ArrayList<>();
            for(ShortDistancesInfo sdi : shortDistancesInfoList) {
                if(sdi.distance < distanceFrom && sdi.distance >= distanceTo) continue;
                subList.add(sdi);
            }
            return subList;
        }

        public String getShortDistancesInfoAsString(double distanceFrom, double distanceTo, int listSize) {
            Collections.sort(shortDistancesInfoList);

            int count = 0;
            StringBuilder sb = new StringBuilder();
            for(ShortDistancesInfo sdi : shortDistancesInfoList) {
                if(sdi.distance < distanceFrom && sdi.distance >= distanceTo) continue;
                if(sb.length() != 0) sb.append(' ');
                sb.append(sdi.id);

                if(++count >= listSize) break;
            }
            return sb.toString();
        }
    }

    private static class ShortDistancesInfo implements Comparable<ShortDistancesInfo> {
        String id;
        double distance;

        ShortDistancesInfo(String id,
                           double distance) {
            this.id = id;
            this.distance = distance;
        }

        @Override
        public int compareTo(ShortDistancesInfo o) {
            if(distance > o.distance) return 1;
            else if(distance < o.distance) return -1;
            else if(distance == o.distance) return 0;
            return 0;
        }
    }

}
