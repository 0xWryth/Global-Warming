package fr.polytech.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AppData {
    private HashMap<Integer, YearData> data;
    private Double minDif, maxDif;
    private Integer minYear, maxYear;

    public AppData(HashMap<Integer, YearData> data, Double minDif, Double maxDif, Integer minYear, Integer maxYear) {
        this.data = data;
        this.minDif = minDif;
        this.maxDif = maxDif;
        this.minYear = minYear;
        this.maxYear = maxYear;
    }

    public Double getAnomalyFromYearAndArea(Integer year, EarthPosition area) {
        return data.get(year).getAnomalyFromArea(area);
    }

    public HashMap<EarthPosition, Double> getAnomaliesFromYear(Integer year) {
        return data.get(year).getAnomalies();
    }

    public ArrayList<Double> getAnomaliesFromArea(EarthPosition area) {
        ArrayList<Double> arrayList = new ArrayList<>();

        data.forEach((integer, yearData) -> {
            arrayList.add(yearData.getAnomalyFromArea(area));
        });

        return arrayList;
    }

    public Double getMinDif() {
        return minDif;
    }

    public Double getMaxDif() {
        return maxDif;
    }

    public Set<Integer> getYearList() {
        return data.keySet();
    }

    public Integer getMinYear() {
        return minYear;
    }

    public Integer getMaxYear() {
        return maxYear;
    }
}
