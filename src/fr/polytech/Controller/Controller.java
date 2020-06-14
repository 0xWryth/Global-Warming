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
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Pair;

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

    @FXML
    Button changeModelBtn;

    @FXML
    Pane previewPane = new Pane();

    private Boolean inverted;

    private CameraManager cameraManager;

    Group earth = new Group();
    Group earthSvg = new Group();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changingLblText();
        earthPane.setStyle("-fx-background-color: radial-gradient(focus-angle 45deg, focus-distance 5%, center 50% 50%, radius 100%, reflect, white 10%, white 15%, rgb(85, 124, 168) 60%, rgb(29,37,83) 80%)");

        loadingEarthModel(false);
        loadingEarthModel(true);

        inverted = false;

        loadingEarth(inverted);
    }

    private void changingLblText() {
        yearIndicatorLbl.textProperty().bind(
                Bindings.format(
                        "%.0f",
                        yearSlider.valueProperty()
                )
        );
    }

    private void loadingEarthModel(Boolean svgMap) {
        ObjModelImporter objImporter = new ObjModelImporter();
        try {
            String URL = svgMap ? "/fr/polytech/Assets/earthSvg.obj" : "/fr/polytech/Assets/earth.obj";
            URL modeUrl = this.getClass().getResource(URL);
            objImporter.read(modeUrl);
        } catch(ImportException e) {
            System.out.println(e);
        }

        if (svgMap) {
            MeshView[] mv = objImporter.getImport();
            earthSvg.getChildren().addAll(mv);
        }
        else {
            earth.getChildren().addAll(objImporter.getImport());
        }
    }

    private void loadingEarth(Boolean invertMode) {
        Group g1 = invertMode ? earthSvg : earth;
        Group g2 = invertMode ? earth : earthSvg;

        // Adding the earth to the graph root
        Group root3D = new Group(g1);

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

        // previewPane

        // Adding the earth to the graph root
        Group root3D2 = new Group(g2);

        // Light
        AmbientLight light2 = new AmbientLight(Color.WHITE);
        root3D2.getChildren().add(light2);

        // Camera
        PerspectiveCamera camera2 = new PerspectiveCamera(true);
        CameraManager cm2 = new CameraManager(camera2, previewPane, root3D2);
        cm2.setFixed(true);

        cameraManager.setFollower(cm2);

        // Adding the subscene
        SubScene subScene2 = new SubScene(root3D2, 50, 50, true, SceneAntialiasing.BALANCED);
        subScene2.setCamera(camera2);
        subScene2.setFill(Color.rgb(109,140,163));

        previewPane.getChildren().addAll(subScene2);
    }

    public void resetCamera(ActionEvent actionEvent) {
        cameraManager.resetCamera();
    }

    public void changeEarthModel(ActionEvent actionEvent) {
        System.gc();

        inverted = !inverted;
        Pair<Rotate, Rotate> p = cameraManager.getRotate();
        loadingEarth(inverted);
        cameraManager.setRotate(p);
//        earth.setVisible(!earth.visibleProperty().get());
//        earthSvg.setVisible(!earthSvg.visibleProperty().get());
        System.gc();
    }
}
