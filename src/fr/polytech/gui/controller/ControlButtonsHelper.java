package fr.polytech.gui.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ControlButtonsHelper {
    public static String setSpeedText(Button speedBtn) {
        ArrayList<String> speedArr = new ArrayList<>();
        speedArr.add("0.5");
        speedArr.add("1");
        speedArr.add("2");

        int i = speedArr.indexOf(speedBtn.getText());
        if (i == speedArr.size() - 1) {
            i = 0;
        }
        else {
            i++;
        }

        return speedArr.get(i);
    }

    public static void setLoopIcon(ImageView loopIcon, Boolean isLooping) {
        try {
            if (isLooping) {
                loopIcon.setImage(new Image(new FileInputStream("src/fr/polytech/gui/assets/icons/loop.png")));
            }
            else {
                loopIcon.setImage(new Image(new FileInputStream("src/fr/polytech/gui/assets/icons/no-loop.png")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void changeAnimation(Slider yearSlider, Boolean playingAnimation, ImageView playIcon, AnimationTimer animation) {
        yearSlider.setDisable(playingAnimation);

        if (playingAnimation) {
            try {
                playIcon.setImage(new Image(new FileInputStream("src/fr/polytech/gui/assets/icons/pause.png")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            animation.start();
        }
        else {
            try {
                playIcon.setImage(new Image(new FileInputStream("src/fr/polytech/gui/assets/icons/play.png")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            animation.stop();
        }
    }
}
