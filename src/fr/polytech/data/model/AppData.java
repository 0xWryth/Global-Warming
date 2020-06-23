package fr.polytech.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AppData {
    // The app data
    private HashMap<Integer, YearData> data;

    // Min & Max anomalies
    private Double minDif, maxDif;

    // Min & Max years
    private Integer minYear, maxYear;

    /**
     * AppData constructor
     * @param data - an hashmap of the anomalies by area and by year
     * @param minDif - minimum anomaly
     * @param maxDif - maximum anomly
     * @param minYear - minimum year
     * @param maxYear - maximum year
     */
    public AppData(HashMap<Integer, YearData> data, Double minDif, Double maxDif, Integer minYear, Integer maxYear) {
        this.data = data;
        this.minDif = minDif;
        this.maxDif = maxDif;
        this.minYear = minYear;
        this.maxYear = maxYear;
    }

    // Getters

    /**
     * @return minimum anomaly data
     */
    public Double getMinDif() {
        return minDif;
    }

    /**
     * @return maxmimum anomaly data
     */
    public Double getMaxDif() {
        return maxDif;
    }

    /**
     * @return year list
     */
    public Set<Integer> getYearList() {
        return data.keySet();
    }

    /**
     * @return minimum year
     */
    public Integer getMinYear() {
        return minYear;
    }

    /**
     * @return maximum year
     */
    public Integer getMaxYear() {
        return maxYear;
    }

    // Methodes

    /**
     * @param year - given year
     * @param area - given area
     * @return anomalies of a given area at a given year
     */
    public Double getAnomalyFromYearAndArea(Integer year, EarthPosition area) {
        return data.get(year).getAnomalyFromArea(area);
    }

    /**
     * @param year - given year
     * @return anomalies by area at given year
     */
    public HashMap<EarthPosition, Double> getAnomaliesFromYear(Integer year) {
        return data.get(year).getAnomalies();
    }

    /**
     * @param area - given area
     * @return anomalies by year at given area
     */
    public ArrayList<Double> getAnomaliesFromArea(EarthPosition area) {
        ArrayList<Double> arrayList = new ArrayList<>();

        data.forEach((integer, yearData) -> {
            arrayList.add(yearData.getAnomalyFromArea(area));
        });

        return arrayList;
    }
}
