package fr.polytech;

import fr.polytech.Model.AppData;
import fr.polytech.Model.DataCreator;
import fr.polytech.Model.EarthPosition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class App extends Application {
    public static void main(String[] args) {
        AppData a = DataCreator.readFile(args[0]);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Setting FXML file URL
            final URL url = getClass().getResource("/fr/polytech/View/gui.fxml");

            // Loading FXML file
            final FXMLLoader fxmlLoader = new FXMLLoader(url);

            // Creating GUI root
            final HBox root = (HBox) fxmlLoader.load();

            // Creating JAVAFX Scene
            final Scene scene = new Scene(root, 1060, 740);
            primaryStage.setScene(scene);
        } catch (IOException ex) {
            System.err.println("Loading error: " + ex);
        }
        primaryStage.setTitle("App");

        primaryStage.show();
    }
}
