package fr.polytech.Model;

import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.util.Pair;

public class CameraManager {

    private static final double CAMERA_MIN_DISTANCE = -0.5;
    private static final double CAMERA_INITIAL_DISTANCE = -6.5;
    private static final double CAMERA_INITIAL_X_ANGLE = -38.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 172.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double SCROLL_SPEED = 0.5;
    private static final double BLOCK_SCROLL_MAX = -2;
    private static final double BLOCK_SCROLL_MIN = -10;

    private final Group cameraXform = new Group();
    private final Group cameraXform2 = new Group();
    private Rotate rx = new Rotate();
    private Rotate ry = new Rotate();
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    private Boolean fixed;
    private CameraManager follower;

    private Camera camera;

    public void resetCamera() {
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);

        ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        rx.setAngle(CAMERA_INITIAL_X_ANGLE);

        if (follower != null) {
            follower.resetCamera();
            follower.camera.setTranslateZ(BLOCK_SCROLL_MAX - 1);
        }
    }

    public CameraManager(Camera cam, Node mainRoot, Group root) {

        fixed = false;

        camera = cam;

        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(camera);

        rx.setAxis(Rotate.X_AXIS);
        ry.setAxis(Rotate.Y_AXIS);
        cameraXform.getTransforms().addAll(ry, rx);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        rx.setAngle(CAMERA_INITIAL_X_ANGLE);

        // Add mouse handler
        handleMouse(mainRoot, root);
    }

    private void handleMouse(Node mainRoot, final Node root) {

        mainRoot.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (!fixed) {
                    mousePosX = me.getSceneX();
                    mousePosY = me.getSceneY();
                    mouseOldX = me.getSceneX();
                    mouseOldY = me.getSceneY();

                    // Set focus on the mainRoot to be able to detect key press
                    mainRoot.requestFocus();

                    if (follower != null) {
                        follower.mousePosX = me.getSceneX();
                        follower.mousePosY = me.getSceneY();
                        follower.mouseOldX = me.getSceneX();
                        follower.mouseOldY = me.getSceneY();
                    }
                }
            }
        });
        mainRoot.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (!fixed) {
                    mouseOldX = mousePosX;
                    mouseOldY = mousePosY;
                    mousePosX = me.getSceneX();
                    mousePosY = me.getSceneY();
                    mouseDeltaX = (mousePosX - mouseOldX) * 0.3 * (camera.getTranslateZ() / BLOCK_SCROLL_MAX);
                    mouseDeltaY = (mousePosY - mouseOldY) * 0.3 * (camera.getTranslateZ() / BLOCK_SCROLL_MAX);

                    double modifier = 1.0;

                    if (me.isControlDown()) {
                        modifier = CONTROL_MULTIPLIER;
                    }
                    if (me.isShiftDown()) {
                        modifier = SHIFT_MULTIPLIER;
                    }
                    if (me.isPrimaryButtonDown()) {
                        double newRyAngle = ry.getAngle() + mouseDeltaX * modifier * ROTATION_SPEED;
                        ry.setAngle(newRyAngle);
                        double newRxAngle = rx.getAngle() - mouseDeltaY * modifier * ROTATION_SPEED;
                        if (newRxAngle >= -50.0 && newRxAngle <= 50.0) {
                            rx.setAngle(newRxAngle);
                        }
                    }

                    if (follower != null) {
                        follower.mouseOldX = mousePosX;
                        follower.mouseOldY = mousePosY;
                        follower.mousePosX = me.getSceneX();
                        follower.mousePosY = me.getSceneY();
                        follower.mouseDeltaX = (mousePosX - mouseOldX);
                        follower.mouseDeltaY = (mousePosY - mouseOldY);
                        if (me.isPrimaryButtonDown()) {
                            double newRyAngle = ry.getAngle() + mouseDeltaX * modifier * ROTATION_SPEED;
                            follower.ry.setAngle(newRyAngle);
                            double newRxAngle = rx.getAngle() - mouseDeltaY * modifier * ROTATION_SPEED;
                            if (newRxAngle >= -50.0 && newRxAngle <= 50.0) {
                                follower.rx.setAngle(newRxAngle);
                            }
                        }
                    }
                }
            }
        });
        mainRoot.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (!fixed) {
                    double modifier = 1.0;

                    if (event.isControlDown()) {
                        modifier = CONTROL_MULTIPLIER;
                    }
                    if (event.isShiftDown()) {
                        modifier = SHIFT_MULTIPLIER;
                    }
                    double z = camera.getTranslateZ();
                    double newZ = z + event.getDeltaY() * MOUSE_SPEED * (modifier + SCROLL_SPEED);
                    if (newZ > CAMERA_MIN_DISTANCE) newZ = CAMERA_MIN_DISTANCE;

                    if (newZ <= BLOCK_SCROLL_MAX && newZ >= BLOCK_SCROLL_MIN) {
                        camera.setTranslateZ(newZ);
                    }
                }
            }
        });
    }

    public void setFixed(Boolean b) {
        fixed = b;
    }

    public void setFollower(CameraManager cm) {
        follower = cm;
        follower.camera.setTranslateZ(BLOCK_SCROLL_MAX - 1);
    }

    public Pair<Rotate, Rotate> getRotate() {
        Pair<Rotate, Rotate> r = new Pair(rx, ry);

        return r;
    }

    public void setRotate(Pair<Rotate, Rotate> p) {
        rx.setAngle(p.getKey().getAngle());
        ry.setAngle(p.getValue().getAngle());
    }
}