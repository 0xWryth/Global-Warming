package fr.polytech.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AppData {
    private HashMap<Integer, YearData> data;
    private HashMap<EarthPosition, Integer> normalTemperature;
    private Double minDif, maxDif;

    public AppData(HashMap<Integer, YearData> data, Double minDif, Double maxDif) {
        this.data = data;
        this.minDif = minDif;
        this.maxDif = maxDif;
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
}
