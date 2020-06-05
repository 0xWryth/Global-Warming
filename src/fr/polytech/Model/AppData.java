package fr.polytech.Model;

import javafx.util.Pair;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AppData {
    private HashMap<Integer, YearData> data;
    private HashMap<EarthPosition, Integer> normalTemperature;

    public AppData(HashMap<Integer, YearData> data) {
        this.data = data;
    }

    public ArrayList<Integer> getYearsList() {
        return null;
    }

    public Pair<Integer, Integer> getMinMaxAnomalyTemperature() {
        return null;
    }

    public Set<EarthPosition> getAreaList() {
        return null;
    }

    public Integer getAnomalyFromYearAndArea(Integer year, EarthPosition area) {
        return null;
    }

    public HashMap<EarthPosition, Integer> getAnomaliesFromYear(Integer year) {
        return null;
    }

    public HashMap<Integer, Integer> getAnomaliesFromArea(EarthPosition area) {
        return null;
    }
}
