package com.zjf.scanmodifyutils;

import java.io.File;

public class ConflictObj {
    private File checkFile;
    private String conflictMes;

    public ConflictObj() {
        super();
    }

    public File getCheckFile() {
        return checkFile;
    }

    public void setCheckFile(File checkFile) {
        this.checkFile = checkFile;
    }

    public String getConflictMes() {
        return conflictMes;
    }

    public void setConflictMes(String conflictMes) {
        this.conflictMes = conflictMes;
    }

    public ConflictObj(File checkFile, String conflictMes) {
        super();
        this.checkFile = checkFile;
        this.conflictMes = conflictMes;
    }

}
