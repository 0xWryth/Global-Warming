package fr.polytech.Model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AppData {
    private HashMap<Integer, HashMap<EarthPosition, Integer>> data;
    private HashMap<EarthPosition, Integer> normalTemperature;

    public AppData() {
        data = new HashMap<>();
        normalTemperature = new HashMap<>();
        loadData();
    }

    private void loadData() {
        var d = FileReader.readFile("Test");
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
