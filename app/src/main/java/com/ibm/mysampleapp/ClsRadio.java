package com.ibm.mysampleapp;

public class ClsRadio {
  String vodName;
  String filePath;
  boolean isFave;

    public ClsRadio(String vodName, String filePath) {
        this.vodName = vodName;
        this.filePath = filePath;
        this.isFave = false; //will turn true only by clicking the star
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

    public Boolean getIsFave() {
        return isFave;
    }
    public void setIsFave(boolean isFave) {
        this.isFave = isFave;
    }


}
