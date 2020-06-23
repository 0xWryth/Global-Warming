package fr.polytech.data.model;

import java.io.*;
import java.net.URL;
import java.util.HashMap;

public class DataCreator {
    public static AppData readFile(String filePath) {
        try {
            URL res = DataCreator.class.getResource(filePath);

            BufferedReader bufRead = new BufferedReader(
                    new InputStreamReader(res.openStream()));

            String line = bufRead.readLine();
            if (line != null) {
                line = line.replace("\"", "");
            }

            Integer minYear = null;
            HashMap<Integer, YearData> data = new HashMap<>();

            Integer maxYear = null;

            Double minDif = null;
            Double maxDif = null;

            Boolean firstLine = true;
            while ( line != null) {
                String[] array = line.split(",");

                if (firstLine) {
                    firstLine = false;

                    minYear = Integer.parseInt(array[2]);

                    for (int i = 2; i < array.length; i++) {
                        YearData yearData = new YearData();
                        data.put(Integer.parseInt(array[i]), yearData);
                    }

                    maxYear = Integer.parseInt(array[array.length - 1]);
                }
                else {
                    EarthPosition earthPosition = new EarthPosition(Double.parseDouble(array[0]), Double.parseDouble(array[1]));

                    int year = minYear;
                    for (int i = 2; i < array.length; i++) {
                        Double posTemp = array[i].equals("NA") ? Double.NaN : Double.parseDouble(array[i]);

                        if (!posTemp.isNaN()) {
                            if (minDif == null || maxDif == null) {
                                maxDif = posTemp;
                                minDif = posTemp;
                            }

                            if (posTemp > maxDif) {
                                maxDif = posTemp;
                            }

                            if (posTemp < minDif) {
                                minDif = posTemp;
                            }
                        }

                        YearData d = data.get(year);
                        d.put(earthPosition, posTemp);
                        year++;
                    }
                }

                line = bufRead.readLine();
            }

            bufRead.close();

            return new AppData(data, minDif, maxDif, minYear, maxYear);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
