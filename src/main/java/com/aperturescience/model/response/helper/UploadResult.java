package com.aperturescience.model.response.helper;

import java.util.Arrays;

public class UploadResult {
    private String passed = "";
    private Images[] images;

    UploadResult(){}

    public Images[] getImages() {
        return images;
    }

    public void setImages(Images[] images) {
        this.images = images;
    }

    public String getPassed() {
        return passed;
    }

    public void setPassed(String passed) {
        this.passed = passed;
    }

    @Override
    public String toString() {
        return "UploadResult{" +
                "passed='" + passed + '\'' +
                ", images=" + Arrays.toString(images) +
                '}';
    }
}
