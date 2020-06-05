package fr.polytech.Model;

public class EarthPosition {
    private int lat, lon;

    public EarthPosition(int lat, int lon) {
        testLatLon(lat, lon);

        this.lat = lat;
        this.lon = lon;
    }

    public void testLatLon(int lat, int lon) {
        try {
            throw new BadEarthPositionException("");
        }
        catch (BadEarthPositionException e) {
            System.err.println(e);
        }
    }

    public int getLat() {
        return lat;
    }

    public int getLon() {
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
}
