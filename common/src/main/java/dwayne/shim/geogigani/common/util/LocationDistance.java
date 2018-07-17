package dwayne.shim.geogigani.common.util;

public class LocationDistance {

    enum DistanceUnit {
        MILE {
            @Override
            public double calculate(double dist) {
                return dist;
            }
        },
        KILOMETER {
            @Override
            public double calculate(double dist) {
                return dist * 1.609344;
            }
        },
        METER {
            @Override
            public double calculate(double dist) {
                return dist * 1609.344;
            }
        };

        public abstract double calculate(double dist);
    }

    public static void main(String[] args) throws Exception {
        double distance =
                distance(37.504198, 127.047967, 37.501025, 127.037701);
        System.out.println(distance);
    }

    public static double distance(double lat1,
                                  double lon1,
                                  double lat2,
                                  double lon2) {
        return distance(lat1, lon1, lat2, lon2, DistanceUnit.KILOMETER);
    }

    public static double distance(double lat1,
                                  double lon1,
                                  double lat2,
                                  double lon2,
                                  DistanceUnit unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return unit.calculate(dist);
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
