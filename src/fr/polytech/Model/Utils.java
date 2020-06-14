package fr.polytech.Model;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Utils {
    private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;

    public static Point3D getCoordTo3dCoord(float lat, float lon, float radius) {
        float lat_cor = lat + TEXTURE_LAT_OFFSET;
        float lon_cor = lon + TEXTURE_LON_OFFSET;

        return new Point3D(-Math.sin(Math.toRadians(lon_cor))
                * Math.cos(Math.toRadians(lat_cor))*radius,
                -Math.sin(Math.toRadians(lat_cor))*radius,
                Math.cos(Math.toRadians(lon_cor))
                        * Math.cos(Math.toRadians(lat_cor))*radius);
    }

    public static void AddQuadrilateral(Group parent, Point3D topRight, Point3D bottomRight, Point3D bootmLeft,
                                  Point3D topLeft, PhongMaterial material) {
        final TriangleMesh triangleMesh = new TriangleMesh();

        final float[] points = {
                (float)topRight.getX(), (float)topRight.getY(), (float)topRight.getZ(),
                (float)topLeft.getX(), (float)topLeft.getY(), (float)topLeft.getZ(),
                (float)bootmLeft.getX(), (float)bootmLeft.getY(), (float)bootmLeft.getZ(),
                (float)bottomRight.getX(), (float)bottomRight.getY(), (float)bottomRight.getZ()
        };

        final float[] texCoords = {
                1, 1,
                1, 0,
                0, 1,
                0, 0
        };

        final int[] faces = {
                0, 1, 1, 0, 2, 2,
                0, 1, 2, 2, 3, 3
        };

        triangleMesh.getPoints().setAll(points);
        triangleMesh.getTexCoords().setAll(texCoords);
        triangleMesh.getFaces().setAll(faces);

        final MeshView meshView = new MeshView(triangleMesh);
        meshView.setMaterial(material);
        parent.getChildren().addAll(meshView);
    }
}
