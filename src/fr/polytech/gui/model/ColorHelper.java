package fr.polytech.gui.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;

public class ColorHelper {
    /**
     * @return no data material
     */
    public static PhongMaterial noDataMaterial() {
        PhongMaterial phongMaterial = new PhongMaterial();
        Color color = new Color(0.5, 0.5, 0.5, 0.01);
        phongMaterial.setSpecularColor(color);
        phongMaterial.setDiffuseColor(color);
        return phongMaterial;
    }

    /**
     * @param colorList list of colors
     * @return list of phongmaterial from given colors
     */
    private static ArrayList<PhongMaterial> colorListToPhongArray(Color[] colorList) {
        ArrayList<PhongMaterial> phongMaterials = new ArrayList<>();
        for (Color color : colorList) {
            PhongMaterial phongMaterial = new PhongMaterial();
            phongMaterial.setDiffuseColor(color);
            phongMaterial.setSpecularColor(color);
            phongMaterials.add(phongMaterial);
        }

        return phongMaterials;
    }

    /**
     * @param opacity opacity of the color
     * @return a list of colors
     */
    public static ArrayList<PhongMaterial> redToBlue(float opacity) {
        Color[] colorList = {
                new Color(1, 0.28, 0.18, opacity),
                new Color(1, 0.38, 0.18, opacity),
                new Color(1, 0.48, 0.18, opacity),
                new Color(1, 0.58, 0.18, opacity),
                new Color(1, 0.78, 0.18, opacity),
                new Color(0.58, 0.58, 0.78, opacity),
                new Color(0.48, 0.48, 0.88, opacity),
                new Color(0.38, 0.38, 0.98, opacity),
                new Color(0.28, 0.28, 1, opacity),
                new Color(0.18, 0.18, 1, opacity),
        };

        return colorListToPhongArray(colorList);
    }

    /**
     * @return a list of colors
     */
    public static ArrayList<PhongMaterial> redAndBlue() {
        Color[] colorList = {
                new Color(1, 0.18, 0.18, 1.0f),
                new Color(1, 0.23, 0.23, 1.0f),
                new Color(1, 0.28, 0.28, 1.0f),
                new Color(1, 0.33, 0.33, 1.0f),
                new Color(1, 0.38, 0.38, 1.0f),
                new Color(0.68, 0.68, 1, 1.0f),
                new Color(0.58, 0.58, 1, 1.0f),
                new Color(0.48, 0.48, 1, 1.0f),
                new Color(0.38, 0.38, 1, 1.0f),
                new Color(0.28, 0.28, 1, 1.0f),
        };

        return colorListToPhongArray(colorList);
    }
}
