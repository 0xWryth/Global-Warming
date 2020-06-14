package fr.polytech.Controller;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.sun.javaws.IconUtil;
import fr.polytech.Model.*;
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
import javafx.scene.input.DragEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import static fr.polytech.Model.Utils.getCoordTo3dCoord;

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

    Group root3D, root3D2;

    Group quadrilataire = new Group();

    AppData appData;

    int yearSliderValue = 2020;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appData = DataCreator.readFile("C:\\Users\\Lucas\\Documents\\MyDango\\PROJET-IHM-LucasBriatte\\src\\fr\\polytech\\Assets\\tempanomaly_4x4grid.csv");

        changingLblText();
        earthPane.setStyle("-fx-background-color: radial-gradient(focus-angle 45deg, focus-distance 5%, center 50% 50%, radius 100%, reflect, white 10%, white 15%, rgb(85, 124, 168) 60%, rgb(29,37,83) 80%)");

        loadingEarthModel(false);
        loadingEarthModel(true);

        inverted = false;

//        yearSlider.valueProperty().addListener(
//                (observable, oldvalue, newvalue) ->
//                {
//                    int i = newvalue.intValue();
//                    System.out.println(i);
//                }
//        );

        loadingEarth(inverted);
        showAnomalyByYear(yearSliderValue);
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
            Group g = new Group(mv);
            g.setId("earth");
            earthSvg.getChildren().addAll(g);
        }
        else {
            Group g = new Group(objImporter.getImport());
            g.setId("earth");
            earth.getChildren().addAll(g);
        }
    }

    private void loadingEarth(Boolean invertMode) {
        Group g1 = invertMode ? earthSvg : earth;
        Group g2 = invertMode ? earth : earthSvg;

        // Adding the earth to the graph root
        root3D = new Group(g1);

        // Light
        AmbientLight light = new AmbientLight(Color.WHITE);
        light.setId("light");
        root3D.getChildren().add(light);

        // Camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setId("camera");
        cameraManager = new CameraManager(camera, earthPane, root3D);

        // Adding the subscene
        SubScene subScene = new SubScene(root3D, 685, 670, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.TRANSPARENT);

        earthPane.getChildren().addAll(subScene);

        // previewPane

        // Adding the earth to the graph root
        root3D2 = new Group(g2);

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
        System.gc();
    }

    public void showAnomalyByYear(int year) {
        root3D.getChildren().remove(quadrilataire);
        quadrilataire = new Group();
        quadrilataire.setId("quadrilataire");

        final HashMap<EarthPosition, Double> anomaliesFromYear = appData.getAnomaliesFromYear(year);
        final double minDif = appData.getMinDif();
        final double maxDif = appData.getMaxDif();
        ArrayList<PhongMaterial> phongMaterials = ColorScale.redToBlue(0.001f);

        for (int lat = -88; lat <= 88; lat = lat + 4) {
            for (int lon = -178; lon <= 178; lon = lon + 4) {
                EarthPosition earthPosition = new EarthPosition(lat, lon);
                final Double anomaly = anomaliesFromYear.get(earthPosition);

                if (anomaly != null) {
                    final double anomalyColorDouble = (maxDif - anomaly) / (maxDif - minDif);
                    final int closeTo = (int) Math.round(anomalyColorDouble * 10);

                    PhongMaterial material = phongMaterials.get(closeTo);

                    // parent, topRight, bottomRight, bottomLeft, topLeft, material

                    Point3D topLeft = getCoordTo3dCoord(lat + 4, lon, 1.01f);
                    Point3D topRight = getCoordTo3dCoord(lat + 4, lon + 4, 1.01f);
                    Point3D bottomLeft = getCoordTo3dCoord(lat, lon, 1.01f);
                    Point3D bottomRight = getCoordTo3dCoord(lat, lon + 4, 1.01f);

                    Utils.AddQuadrilateral(quadrilataire, topRight, bottomRight, bottomLeft, topLeft, material);
                }
            }
        }

        root3D.getChildren().addAll(quadrilataire);
    }

    public void test() {
        showAnomalyByYear((int) yearSlider.getValue());
    }
}
