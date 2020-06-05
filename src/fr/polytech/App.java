package fr.polytech;

import fr.polytech.Model.AppData;
import fr.polytech.Model.DataCreator;
import fr.polytech.Model.EarthPosition;

public class App {
    public static void main(String[] args) {
        AppData a = DataCreator.readFile(args[0]);
    }
}
