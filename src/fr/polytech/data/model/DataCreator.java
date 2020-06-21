package fr.polytech.data.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class DataCreator {
    public static AppData readFile(String filePath) {
        try {
            FileReader file = new FileReader(filePath);
            BufferedReader bufRead = new BufferedReader(file);

            String line = bufRead.readLine();
            if (line != null) {
                line = line.replace("\"", "");
            }

            Integer minYear = null;
            HashMap<Integer, YearData> data = new HashMap<>();

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
            file.close();

            return new AppData(data, minDif, maxDif);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
