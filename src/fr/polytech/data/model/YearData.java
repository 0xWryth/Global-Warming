package fr.polytech.data.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * A year data
 */
public class YearData {
    private LinkedHashMap<EarthPosition, Double> data;

    /**
     * YearData constructor
     */
    public YearData() {
        data = new LinkedHashMap<>();
    }

    // Getter

    /**
     * @return earthPostion anomalies list
     */
    public HashMap<EarthPosition, Double> getAnomalies() {
        return data;
    }

    // Methods

    /**
     * Adding an anomaly data for a given position
     * @param earthPosition - given position
     * @param tempValue - given anomaly value
     */
    public void put(EarthPosition earthPosition, Double tempValue) {
        data.put(earthPosition, tempValue);
    }

    /**
     * @param earthPosition - given position
     * @return anomly for an area
     */
    public Double getAnomalyFromArea(EarthPosition earthPosition) {
        return data.get(earthPosition);
    }
}
