package com.aperturescience.model.response.helper;

/*Helper model class for DetectedObjects class*/
public class Captions {
    private String text;
    private Double confidence;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return "Captions{" +
                "text='" + text + '\'' +
                ", confidence=" + confidence +
                '}';
    }
}
