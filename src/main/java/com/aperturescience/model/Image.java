package com.aperturescience.model;

import java.io.ByteArrayOutputStream;

public class Image {
    private ByteArrayOutputStream stream;
    private String filename;

    public Image(ByteArrayOutputStream stream, String filename) {
        this.stream = stream;
        this.filename = filename;
    }

    public ByteArrayOutputStream getStream() {
        return stream;
    }

    public void setStream(ByteArrayOutputStream stream) {
        this.stream = stream;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
