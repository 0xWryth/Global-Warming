package fr.polytech;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Setting FXML file URL
            final URL url = getClass().getResource("/fr/polytech/gui/view/gui.fxml");

            // Loading FXML file
            final FXMLLoader fxmlLoader = new FXMLLoader(url);

            // Creating GUI root
            final VBox root = (VBox) fxmlLoader.load();

            // Creating JAVAFX Scene
            final Scene scene = new Scene(root, 685, 740);
            primaryStage.setScene(scene);
        } catch (IOException ex) {
            System.err.println("Loading error: " + ex);
        }
        primaryStage.setTitle("Visualisation des anomalies de températures");

        primaryStage.show();
    }
}
