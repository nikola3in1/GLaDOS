package com.aperturescience.model.response;

public class FaceData {
    private String faceId;
    private FaceRectangle faceRectangle;

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public FaceRectangle getFaceRectangle() {
        return faceRectangle;
    }

    public void setFaceRectangle(FaceRectangle faceRectangle) {
        this.faceRectangle = faceRectangle;
    }

    @Override
    public String toString() {
        return "FaceData{" +
                "faceId='" + faceId + '\'' +
                ", faceRectangle=" + faceRectangle +
                '}';
    }
}
