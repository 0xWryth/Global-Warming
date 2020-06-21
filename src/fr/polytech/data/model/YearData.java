package fr.polytech.data.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class YearData {
    private LinkedHashMap<EarthPosition, Double> data;

    public YearData() {
        data = new LinkedHashMap<>();
    }

    public void put(EarthPosition earthPosition, Double tempValue) {
        data.put(earthPosition, tempValue);
    }

    public Double getAnomalyFromArea(EarthPosition earthPosition) {
        return data.get(earthPosition);
    }

    public HashMap<EarthPosition, Double> getAnomalies() {
        return data;
    }
}
