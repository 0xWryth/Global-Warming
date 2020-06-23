package fr.polytech.data.model;

public class EarthPosition {
    private double lat, lon;

    public EarthPosition(double lat, double lon) {
        testLatLon(lat, lon);

        this.lat = lat;
        this.lon = lon;
    }

    public void testLatLon(double lat, double lon) {
        if (!(lat >= -90.0 && lat <= 90.0 && lon >= -180.0 && lon <= 180.0)) {
            try {
                throw new BadEarthPositionException("La latitude/longitude " + lat + ", " + lon + " n'est pas correcte");
            } catch (BadEarthPositionException e) {
                e.printStackTrace();
            }
        }
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return lat + "-" + lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EarthPosition that = (EarthPosition) o;

        if (lat != that.lat) return false;
        return lon == that.lon;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
