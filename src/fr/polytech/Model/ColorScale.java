package fr.polytech.Model;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;

public class ColorScale {
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

        ArrayList<PhongMaterial> phongMaterials = new ArrayList<>();
        for (Color color : colorList) {
            PhongMaterial phongMaterial = new PhongMaterial();
            phongMaterial.setDiffuseColor(color);
            phongMaterial.setSpecularColor(color);
            phongMaterials.add(phongMaterial);
        }

        return phongMaterials;
    }
}
