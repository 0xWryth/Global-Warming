package fr.polytech.gui.controller;

import fr.polytech.gui.model.*;
import fr.polytech.data.model.AppData;
import fr.polytech.data.model.DataCreator;
import fr.polytech.data.model.EarthPosition;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Pair;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
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

    @FXML
    Pane legendPane;

    @FXML
    Label latLonLbl;

    @FXML
    ScatterChart scatterChart;

    @FXML
    Pane graphPane;

    @FXML
    ImageView playIcon;

    @FXML
    ImageView loopIcon;

    @FXML
    Button speedBtn;

    private Boolean inverted;

    private CameraManager cameraManager;

    private Group earth;
    private Group earthSvg;

    private Group root3D, root3D2;

    private Group quadrilataire;
    private Group histo;

    private AppData appData;

    private ArrayList<PhongMaterial> quadriColor;
    private ArrayList<PhongMaterial> histoColor;
    private PhongMaterial noDataColor;

    private boolean playingAnimation = false;
    private float animationSpeed = 1.0f;
    private boolean isLooping = false;

    private AnimationTimer animation;

    private int sliderYear;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appData = DataCreator.readFile("/fr/polytech/data/assets/tempanomaly_4x4grid.csv");

        sliderYear = appData.getMaxYear();

        changingLblText();
        earthPane.setStyle("-fx-background-color: radial-gradient(focus-angle 45deg, focus-distance 5%, center 50% 50%, radius 100%, reflect, white 10%, white 15%, rgb(85, 124, 168) 60%, rgb(29,37,83) 80%)");

        quadriColor = ColorHelper.redToBlue(0.001f);
        histoColor = ColorHelper.redAndBlue();
        noDataColor = ColorHelper.noDataMaterial();

        scatterChart.setLegendVisible(false);

        creatingQuadriAndHisto();

        loadingEarth();
        loadingSvgEarth();

        creatingAnimation();

        inverted = false;
        displayingEarth(inverted);

        showQuadri(sliderYear);
        showHisto(sliderYear);

        showLegend(inverted);
    }

    @FXML
    private void showGraph() {
        graphPane.setVisible(true);
        scatterChart.getData().clear();

        final String latLonStr = latLonLbl.getText();
        final String[] latLonArr = latLonStr.split(", ");
        final String latStr = latLonArr[0].split("[.]")[0];
        final String lonStr = latLonArr[1].split("[.]")[0];

        double lat = Double.parseDouble(latStr);
        lat = lat - lat % 4;
        double lon = Double.parseDouble(lonStr);
        lon = lon - lon % 4 + 2.0;

        ArrayList<Double> anomalies = appData.getAnomaliesFromArea(new EarthPosition(lat, lon));

        XYChart.Series series = new XYChart.Series();

        for (int i = 0; i < anomalies.size(); i++) {
            if (!anomalies.get(i).isNaN()) {
                series.getData().add(new XYChart.Data(appData.getMinYear() + i, anomalies.get(i)));
            }
        }
        scatterChart.getData().add(series);
        scatterChart.setTitle("Anomalies de témpératures de la zone " + lat + ", " + lon + " par an");
    }

    private void creatingAnimation() {
        animation = new AnimationTimer() {
            private long startNanoTime;

            @Override
            public void start() {
                startNanoTime = System.nanoTime();
                super.start();
            }

            @Override
            public void handle(long now) {
                if (now - startNanoTime >= (1e9 / 4) / animationSpeed) {
                    startNanoTime = System.nanoTime();
                    yearSlider.setValue(sliderYear + 1);
                    int maxYear = appData.getYearList().stream().mapToInt(v -> v)
                            .max().orElseThrow(NoSuchElementException::new);
                    if (sliderYear >= maxYear && !isLooping) {
                        playAnimation();
                    }
                    else if (sliderYear >= maxYear && isLooping) {
                        yearSlider.setValue(appData.getMinYear());
                    }
                    changeYear();
                }
            }
        };
    }

    private void showLegend(Boolean inverted) {
        ArrayList<PhongMaterial> phongMaterials = inverted ? histoColor : quadriColor;

        legendPane.getChildren().forEach((pane) -> {
            final String idStr = pane.getId();
            final String[] idArr = idStr.split("_");
            final String id = idArr[1];

            String color = phongMaterials.get(Integer.parseInt(id) - 1).getSpecularColor().toString();
            color = color.substring(2, 8);
            String style = "-fx-background-color: #" + color;
            pane.setStyle(style);
        });
    }

    private void showHisto(int year) {
        final HashMap<EarthPosition, Double> anomaliesFromYear = appData.getAnomaliesFromYear(year);
        final double minDif = appData.getMinDif();
        final double maxDif = appData.getMaxDif();

        histo.getChildren().forEach((cylinder) -> {
            String[] latlon = cylinder.getId().split("\\|");
            int lat = Integer.parseInt(latlon[1]);
            int lon = Integer.parseInt(latlon[2]);

            EarthPosition earthPosition = new EarthPosition(lat, lon);
            final Double anomaly = anomaliesFromYear.get(earthPosition);

            if (cylinder instanceof Cylinder) {
                Cylinder cyl = (Cylinder) cylinder;

                final double anomalyColorDouble = (maxDif - anomaly) / maxDif;
                int closeTo = (int) Math.round(anomalyColorDouble * 4);

                PhongMaterial material = histoColor.get(closeTo);

                double height = Utils.roundXNumber((1f + anomaly.floatValue() / 12), 2);
                if (anomaly < 0) {
                    height = 0;
                }
                cyl.setHeight(height);
                cyl.setMaterial(material);
            } else if (cylinder instanceof MeshView) {
                MeshView mv = (MeshView) cylinder;

                mv.setVisible(anomaly <= 0);

                final double anomalyColorDouble = (maxDif - anomaly) / (maxDif - minDif);
                int closeTo = (int) Math.round(anomalyColorDouble * 5);

                PhongMaterial material = histoColor.get(closeTo + 4);
                mv.setMaterial(material);
            }
        });
    }

    private void showQuadri(int year) {
        final HashMap<EarthPosition, Double> anomaliesFromYear = appData.getAnomaliesFromYear(year);
        final double minDif = appData.getMinDif();
        final double maxDif = appData.getMaxDif();

        quadrilataire.getChildren().forEach((mv) -> {
            MeshView m = (MeshView) mv;

            String[] latlon = mv.getId().split("\\|");
            int lat = Integer.parseInt(latlon[1]);
            int lon = Integer.parseInt(latlon[2]);

            Double anomaly = anomaliesFromYear.get(new EarthPosition(lat, lon));
            if (anomaly.isNaN()) {
                m.setMaterial(noDataColor);
            }
            else {
                final double anomalyColorDouble = (maxDif - anomaly) / (maxDif - minDif);
                final int closeTo = (int) Math.round(anomalyColorDouble * 9);

                PhongMaterial material = quadriColor.get(closeTo);

                m.setMaterial(material);
            }
        });
    }

    private void creatingQuadriAndHisto() {
        quadrilataire = new Group();
        histo = new Group();

        for (int lat = -88; lat <= 88; lat = lat + 4) {
            for (int lon = -178; lon <= 178; lon = lon + 4) {
                PhongMaterial material = quadriColor.get(0);

                Point3D topLeft = Utils.getCoordTo3dCoord(lat + 4, lon, 1.01f);
                Point3D topRight = Utils.getCoordTo3dCoord(lat + 4, lon + 4, 1.01f);
                Point3D bottomLeft = Utils.getCoordTo3dCoord(lat, lon, 1.01f);
                Point3D bottomRight = Utils.getCoordTo3dCoord(lat, lon + 4, 1.01f);

                String id = lat + "|" + lon;
                MeshView mv = Utils.AddQuadrilateral(topRight, bottomRight, bottomLeft, topLeft, material, "q|" + id);
                mv.setOnMouseClicked(e->{
                    PickResult pr = e.getPickResult();
                    latLonLbl.setText(Utils.cursorToCoords(pr));
                });
                quadrilataire.getChildren().add(mv);

                Cylinder cylinder = Utils.createLine(new Point3D(0, 0, 0),
                        Utils.getCoordTo3dCoord(lat, lon, 1f));
                cylinder.setMaterial(material);
                cylinder.setId("c|" + id);

                histo.getChildren().addAll(cylinder);

                float width = 0.8f;

                Point3D topLeft2 = Utils.getCoordTo3dCoord(lat + width, lon - width, 1.01f);
                Point3D topRight2 = Utils.getCoordTo3dCoord(lat + width, lon + width, 1.01f);
                Point3D bottomLeft2 = Utils.getCoordTo3dCoord(lat - width, lon - width, 1.01f);
                Point3D bottomRight2 = Utils.getCoordTo3dCoord(lat - width, lon + width, 1.01f);

                MeshView mv2 = Utils.AddQuadrilateral(topRight2, bottomRight2, bottomLeft2, topLeft2, material, "q|" + id);
                mv.setOnMouseClicked(e->{
                    PickResult pr = e.getPickResult();
                    latLonLbl.setText(Utils.cursorToCoords(pr));
                });

                histo.getChildren().add(mv2);
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
        earthSvg.setOnMouseClicked(e->{
            PickResult pr = e.getPickResult();
            latLonLbl.setText(Utils.cursorToCoords(pr));
        });
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
        showLegend(inverted);

        System.gc();
    }

    public void changeYear() {
        sliderYear = (int) yearSlider.getValue();
        showQuadri(sliderYear);
        showHisto(sliderYear);
    }

    public void playAnimation() {
        playingAnimation = !playingAnimation;

        ControlButtonsHelper.changeAnimation(yearSlider, playingAnimation, playIcon, animation);
    }

    public void closeGraphPane(MouseEvent mouseEvent) {
        PickResult pr = mouseEvent.getPickResult();
        Node node = pr.getIntersectedNode();
        if (node instanceof Pane && node.getId().equals("graphPane")) {
            graphPane.setVisible(false);
        }
    }

    public void setLoop(ActionEvent actionEvent) {
        isLooping = !isLooping;

        ControlButtonsHelper.setLoopIcon(loopIcon, isLooping);
    }

    public void setSpeed(ActionEvent actionEvent) {
        String speed = ControlButtonsHelper.setSpeedText(this.speedBtn);

        animationSpeed = Float.parseFloat(speed);
        speedBtn.setText(speed);
    }
}