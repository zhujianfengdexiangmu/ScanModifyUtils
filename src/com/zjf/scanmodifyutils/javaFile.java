package com.zjf.scanmodifyutils;

import java.io.File;

public class javaFile implements Comparable<javaFile> {
    File mFile;
    long mModifiesTime;

    public javaFile(File mFile, long mModifiesTime) {
        this.mFile = mFile;
        this.mModifiesTime = mModifiesTime;
    }

    /**
     * 实现排序方法
     */
    @Override
    public int compareTo(javaFile arg0) {
        if(mModifiesTime > arg0.mModifiesTime){
            return -1;
        }
        else return 1;
    }

}
