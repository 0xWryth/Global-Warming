package fr.polytech.Controller;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import fr.polytech.Model.CameraManager;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    Pane earthPane = new Pane();

    @FXML
    Slider yearSlider;

    @FXML
    Label yearIndicatorLbl;

    @FXML
    Button resetCameraBtn;

    private CameraManager cameraManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changingLblText();
        earthPane.setId("earthPane");
//        earthPane.setStyle("-fx-background-color: radial-gradient(radius 180%, 10, 50, 50, rgba(255,255,255,1) 0%, rgba(85,124,168,1) 100%)");
        earthPane.setStyle("-fx-background-color: radial-gradient(focus-angle 45deg, focus-distance 5%, center 50% 50%, radius 100%, reflect, white 10%, rgb(85, 124, 168) 60%, rgb(29,37,83) 80%)");
        loadingEarth();
    }

    private void changingLblText() {
        yearIndicatorLbl.textProperty().bind(
                Bindings.format(
                        "%.0f",
                        yearSlider.valueProperty()
                )
        );
    }

    private MeshView[] loadingEarthModel() {
        ObjModelImporter objImporter = new ObjModelImporter();
        try {
            URL modeUrl = this.getClass().getResource("/fr/polytech/Assets/earth.obj");
            objImporter.read(modeUrl);
        } catch(ImportException e) {
            System.out.println(e);
        }

        return objImporter.getImport();
    }

    private void loadingEarth() {
        MeshView[] meshViews = loadingEarthModel();

        // Adding the earth to the graph root
        Group root3D = new Group(meshViews);

        // Light
        AmbientLight light = new AmbientLight(Color.WHITE);
        root3D.getChildren().add(light);

        // Camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        cameraManager = new CameraManager(camera, earthPane, root3D);

        // Adding the subscene
        SubScene subScene = new SubScene(root3D, 685, 670, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.TRANSPARENT);


        earthPane.getChildren().addAll(subScene);
    }

    public void resetCamera(ActionEvent actionEvent) {
        cameraManager.resetCamera();
    }
}
