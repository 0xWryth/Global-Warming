package fr.polytech.data.model;

public class EarthPosition {
    private double lat, lon;

    /**
     * EarthPosition constructor
     * @param lat - given area latitude
     * @param lon - given area longitude
     */
    public EarthPosition(double lat, double lon) {
        testLatLon(lat, lon);

        this.lat = lat;
        this.lon = lon;
    }

    // Helper

    /**
     * Testing if given latitude and longitude are correct or not
     * @param lat - given area latitude
     * @param lon - given area longitude
     */
    public void testLatLon(double lat, double lon) {
        if (!(lat >= -90.0 && lat <= 90.0 && lon >= -180.0 && lon <= 180.0)) {
            try {
                throw new BadEarthPositionException("La latitude/longitude " + lat + ", " + lon + " n'est pas correcte");
            } catch (BadEarthPositionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return earthPosition with {latitude + "-" + longitude} format
     */
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
