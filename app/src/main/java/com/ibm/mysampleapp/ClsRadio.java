package com.ibm.mysampleapp;

public class ClsRadio {
  String vodName;
    String filePath;

    public ClsRadio(String vodName, String filePath) {
        this.vodName = vodName;
        this.filePath = filePath;
    }

    public String getVodName() {
        return vodName;
    }

    public void setVodName(String vodName) {
        this.vodName = vodName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
