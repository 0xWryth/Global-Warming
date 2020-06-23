package fr.polytech.data.model;

import java.io.*;
import java.net.URL;
import java.util.HashMap;

/**
 * Class helper used to create an AppData from a file source
 */
public class DataCreator {
    /**
     * @param filePath - a given source file path
     * @return an appdata filled with a file data
     */
    public static AppData readFile(String filePath) {
        try {
            // Getting internal assets
            URL res = DataCreator.class.getResource(filePath);

            // Opening the resource
            BufferedReader bufRead = new BufferedReader(
                    new InputStreamReader(res.openStream()));

            // Reading line by line the file
            String line = bufRead.readLine();
            if (line != null) {
                // Removing un-needed " character
                line = line.replace("\"", "");
            }

            // Minimum year
            Integer minYear = null;

            // Future app data
            HashMap<Integer, YearData> data = new HashMap<>();

            // Maximum year
            Integer maxYear = null;

            // Minimum and maximum anomaly data
            Double minDif = null;
            Double maxDif = null;

            Boolean firstLine = true;
            while ( line != null) {
                // Splitting CSV line into an array
                String[] array = line.split(",");

                // Getting years list interval
                if (firstLine) {
                    firstLine = false;

                    minYear = Integer.parseInt(array[2]);

                    for (int i = 2; i < array.length; i++) {
                        YearData yearData = new YearData();
                        data.put(Integer.parseInt(array[i]), yearData);
                    }

                    maxYear = Integer.parseInt(array[array.length - 1]);
                }
                // Filling app data
                else {
                    EarthPosition earthPosition = new EarthPosition(Double.parseDouble(array[0]), Double.parseDouble(array[1]));

                    int year = minYear;
                    for (int i = 2; i < array.length; i++) {
                        Double posTemp = array[i].equals("NA") ? Double.NaN : Double.parseDouble(array[i]);

                        // Finding app min and max anomaly data
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
