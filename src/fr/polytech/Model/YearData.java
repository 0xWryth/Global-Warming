package fr.polytech.Model;

import java.util.HashMap;

public class YearData {
    private HashMap<EarthPosition, Double> data;

    Double minTemp = null;
    Double maxTemp = null;

    public YearData() {
        data = new HashMap<>();
    }

    public void put(EarthPosition earthPosition, Double tempValue) {
        data.put(earthPosition, tempValue);

        if (tempValue != null) {
            if (maxTemp == null || minTemp == null) {
                minTemp = tempValue;
                maxTemp = tempValue;
            }

            if (tempValue > maxTemp) {
                maxTemp = tempValue;
            }

            if (tempValue < minTemp) {
                minTemp = tempValue;
            }
        }
    }
}
