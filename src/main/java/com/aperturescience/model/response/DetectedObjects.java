package com.aperturescience.model.response;
import com.aperturescience.model.response.helper.Description;

//Response model class for Object detection API
public class DetectedObjects {
    private Description description;

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DetectedObjects{" +
                "description=" + description +
                '}';
    }
}
