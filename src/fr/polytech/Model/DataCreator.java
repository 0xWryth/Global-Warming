package fr.polytech.Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
                        Double posTemp = array[i].equals("NA") ? null : Double.parseDouble(array[i]);
                        var d = data.get(year);
                        d.put(earthPosition, posTemp);
                        year++;
                    }
                }

                line = bufRead.readLine();
            }

            bufRead.close();
            file.close();

            return new AppData(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
