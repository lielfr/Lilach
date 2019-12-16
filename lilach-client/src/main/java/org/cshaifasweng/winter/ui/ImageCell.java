package org.cshaifasweng.winter.ui;

import javafx.scene.image.ImageView;

public class ImageCell {
    private ImageView image;

    public ImageCell(ImageView image) {
        this.image = image;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}
