package com.aperturescience.model.response.helper;

public class Images {
    private String filename = "";
    private String direct_link = "";
    private String original_filename = "";

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDirect_link() {
        return direct_link;
    }

    public void setDirect_link(String direct_link) {
        this.direct_link = direct_link;
    }

    public String getOriginal_filename() {
        return original_filename;
    }

    public void setOriginal_filename(String original_filename) {
        this.original_filename = original_filename;
    }

    @Override
    public String toString() {
        return "Images{" +
                "filename='" + filename + '\'' +
                ", direct_link='" + direct_link + '\'' +
                ", original_filename='" + original_filename + '\'' +
                '}';
    }
}
