package com.codewithharry.mmmutmeityproject;

public class Model_for_camera {
    private String imageByte;

    public Model_for_camera(){}

    public Model_for_camera(String imageByte) {
        this.imageByte = imageByte;
    }

    public String getImageByte() {
        return imageByte;
    }

    public void setImageByte(String imageByte) {
        this.imageByte = imageByte;
    }
}
