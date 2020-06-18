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
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
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

    private Group earth;
    private Group earthSvg;

    private Group root3D, root3D2;

    private Group quadrilataire;
    private Group histo;

    private AppData appData;

    private Boolean quadrilateralView = true;

    private ArrayList<PhongMaterial> quadriColor;
    private ArrayList<PhongMaterial> quadriFullColor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appData = DataCreator.readFile("C:\\Users\\Lucas\\Documents\\MyDango\\PROJET-IHM-LucasBriatte\\src\\fr\\polytech\\Assets\\tempanomaly_4x4grid.csv");

        changingLblText();
        earthPane.setStyle("-fx-background-color: radial-gradient(focus-angle 45deg, focus-distance 5%, center 50% 50%, radius 100%, reflect, white 10%, white 15%, rgb(85, 124, 168) 60%, rgb(29,37,83) 80%)");

        quadriColor = ColorScale.redToBlue(0.001f);
        quadriFullColor = ColorScale.redToBlue(1);

        creatingQuadriAndHisto();

        loadingEarth();
        loadingSvgEarth();

        inverted = false;
        displayingEarth(inverted);

        showQuadri(2020);
        showHisto(2020);
    }

    private void showHisto(int year) {
        final HashMap<EarthPosition, Double> anomaliesFromYear = appData.getAnomaliesFromYear(year);
        final double minDif = appData.getMinDif();
        final double maxDif = appData.getMaxDif();

        histo.getChildren().forEach((cylinder) -> {
            Cylinder cyl = (Cylinder) cylinder;

            String[] latlon = cyl.getId().split("\\|");
            int lat = Integer.parseInt(latlon[0]);
            int lon = Integer.parseInt(latlon[1]);

            EarthPosition earthPosition = new EarthPosition(lat, lon);
            final Double anomaly = anomaliesFromYear.get(earthPosition);

            final double anomalyColorDouble = (maxDif - anomaly) / (maxDif - minDif);
            int closeTo = (int) Math.round(anomalyColorDouble * 9);

            PhongMaterial material = quadriFullColor.get(closeTo);

            double height = Math.round((1f + anomaly.floatValue() / 20) * 100.0) / 100.0;
            cyl.setHeight(height);
            cyl.setMaterial(material);
        });
    }

    private void showQuadri(int year) {
        final HashMap<EarthPosition, Double> anomaliesFromYear = appData.getAnomaliesFromYear(year);
        final double minDif = appData.getMinDif();
        final double maxDif = appData.getMaxDif();

        quadrilataire.getChildren().forEach((mv) -> {
            MeshView m = (MeshView) mv;

            String[] latlon = mv.getId().split("\\|");
            int lat = Integer.parseInt(latlon[0]);
            int lon = Integer.parseInt(latlon[1]);

            Double anomaly = anomaliesFromYear.get(new EarthPosition(lat, lon));
            final double anomalyColorDouble = (maxDif - anomaly) / (maxDif - minDif);
            final int closeTo = (int) Math.round(anomalyColorDouble * 9);

            PhongMaterial material = quadriColor.get(closeTo);

            m.setMaterial(material);
        });
    }

    private void creatingQuadriAndHisto() {
        quadrilataire = new Group();
        histo = new Group();

        for (int lat = -88; lat <= 88; lat = lat + 4) {
            for (int lon = -178; lon <= 178; lon = lon + 4) {
                PhongMaterial material = quadriColor.get(0);

                Point3D topLeft = getCoordTo3dCoord(lat + 4, lon, 1.01f);
                Point3D topRight = getCoordTo3dCoord(lat + 4, lon + 4, 1.01f);
                Point3D bottomLeft = getCoordTo3dCoord(lat, lon, 1.01f);
                Point3D bottomRight = getCoordTo3dCoord(lat, lon + 4, 1.01f);

                String id = Integer.toString(lat) + "|" + Integer.toString(lon);
                Utils.AddQuadrilateral(quadrilataire, topRight, bottomRight, bottomLeft, topLeft, material, id);

                Cylinder cylinder = Utils.createLine(new Point3D(0, 0, 0),
                        getCoordTo3dCoord(lat, lon, 1f));
                cylinder.setMaterial(material);
                cylinder.setId(id);

                histo.getChildren().addAll(cylinder);
            }
        }
    }

    private void changingLblText() {
        yearIndicatorLbl.textProperty().bind(
                Bindings.format(
                        "%.0f",
                        yearSlider.valueProperty()
                )
        );
    }

    private void loadingEarth() {
        earth = Utils.loadingEarthModel(false);
    }

    private void loadingSvgEarth() {
        earthSvg = Utils.loadingEarthModel(true);
    }

    private void displayingEarth(Boolean invertMode) {
        Group g1 = invertMode ? earthSvg : earth;
        Group g2 = invertMode ? earth : earthSvg;

        // Adding the earth to the graph root
        root3D = new Group(g1);

        // Light
        AmbientLight light = new AmbientLight(Color.WHITE);
        light.setId("light");
        root3D.getChildren().add(light);

        root3D.getChildren().add(quadrilataire);
        root3D.getChildren().add(histo);
        quadrilataire.setVisible(!invertMode);
        histo.setVisible(invertMode);

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
        displayingEarth(inverted);
        cameraManager.setRotate(p);

        System.gc();
    }

    public void changeYear() {
        showQuadri((int) yearSlider.getValue());
        showHisto((int) yearSlider.getValue());
    }

    public void switchMode(ActionEvent actionEvent) {
//        quadrilateralView = !quadrilateralView;
//        showAnomalyByYear((int) yearSlider.getValue());
    }
}
