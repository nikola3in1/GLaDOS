package com.aperturescience.model.response.helper;

import java.util.Arrays;
import java.util.HashSet;

/*Helper model class for DetectedObjects class*/
public class Description {
    private HashSet<String> tagSet = new HashSet<>();
    private String[] tags;
    private Captions[] captions;

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
        tagSet = new HashSet<>(Arrays.asList(tags));
    }

    public Captions[] getCaptions() {
        return captions;
    }

    public void setCaptions(Captions[] captions) {
        this.captions = captions;
    }

    @Override
    public String toString() {
        return "Description{" +
                "tagSet=" +  Arrays.toString(tagSet.toArray())+
                ", captions=" + Arrays.toString(captions) +
                '}';
    }


}
