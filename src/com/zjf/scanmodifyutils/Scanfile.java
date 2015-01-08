package com.zjf.scanmodifyutils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自动扫描文件夹下的文件
 */
public class Scanfile {
    /**
     * checkstyle格式文件路径
     */
    public static String checkstyleConfigXmlPath = "D:\\searchbox\\5.0\\baidusearch-phone-baseline\\ci\\checkStyle_config_bdmobile.xml";
    /**
     * jar包路径
     */
    public static String checktyleJarPath = "D:\\checkstyle-6.1.1-all.jar";
    /**
     * 存放符合要求的java文件
     */
    public static List<javaFile> javaFileList = new ArrayList<javaFile>();
    /**
     * 存放要扫描的文件地址
     */
    public static ArrayList<String> projectPathArrayList = new ArrayList<String>();
    /**
     * 存放checkstyle信息
     */
    public static List<ConflictObj> conflictObjs = new ArrayList<ConflictObj>();
    /**
     * 自定义检测的个数上限
     */
    private static final int SIZE = 5;

    /**
     * @param args
     */
    public static void main(String[] args) {
        projectPathArrayList.add("D:\\android_apps\\nido_2048_Demo");
        projectPathArrayList.add("D:\\android_apps\\DialogFragmentDemo");
        ShellExecuter mShellExecuter = new ShellExecuter();
        for (String projectPath : projectPathArrayList) {
            conflictObjs.clear();
            javaFileList.clear();
            try {
                System.out.println("对项目" + new File(projectPath).getName()
                        + "进行checkstyle检测");
                GetFile(new File(projectPath));
                System.out.println("checkstyle检测完毕,待检测文件个数为"
                        + javaFileList.size() + "个");
                int size = Math.min(javaFileList.size(), SIZE);
                System.out.println("检测" + size + "个：");
                Collections.sort(javaFileList);
                if (isSameTimeModified(javaFileList)) {
                    System.out.println("此工程为复制工程,无法判断初始修改时间！");
                } else {
                    for (int i = 0; i < size; i++) {
                        ConflictObj conflictObj = new ConflictObj();
                        conflictObj.setCheckFile(javaFileList.get(i).mFile);
                        String[] str = new String[] { "java", "-jar",
                                checktyleJarPath, "-c",
                                checkstyleConfigXmlPath,
                                javaFileList.get(i).mFile.getAbsolutePath() };
                        try {
                            String result = mShellExecuter.execute(str, null);
                            conflictObj.setConflictMes(result);
                            // System.out.println("检测结果：" + result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        conflictObjs.add(conflictObj);
                    }
                }

                // 遍历configs，依次打印checkstyle信息
                for (ConflictObj conflictObj : conflictObjs) {
                    System.out.println("检测对象:\r\n\t"
                            + conflictObj.getCheckFile().getName()
                            + " \r\n检测结果:\r\n" + conflictObj.getConflictMes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 递归调用查找指定文件加下所有文件
     */
    public static void GetFile(File checkfile) {
        if (!checkfile.isDirectory()) {
            if (isJava(checkfile) && isModifiedWithinHalfHour(checkfile)) {
                javaFile mJavaFile = new javaFile(checkfile,
                        checkfile.lastModified());
                javaFileList.add(mJavaFile);
            }

        } else {
            File[] fileList = checkfile.listFiles();
            for (File file : fileList) {
                GetFile(file);
            }
        }
    }

    /**
     * 判断是否是java文件
     * 
     * @param file
     * @return
     */
    public static boolean isJava(File file) {
        String path = file.getName();
        if (path.contains(".java") && !path.equals("BuildConfig.java")
                && !path.equals("R.java")) {
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否在半小时内修改过
     * 
     * @param file
     * @return
     */
    public static boolean isModifiedWithinHalfHour(File file) {
        if (System.currentTimeMillis() - file.lastModified() < 30 * 60 * 1000) {
            return true;
        }
        return false;
    }

    /**
     * 判断工程目录是否是同一时间修改的，单位秒
     * 
     * @param javaFileList
     * @return true 是同一时间修改 false 不是同一时间修改
     */
    public static boolean isSameTimeModified(List<javaFile> javaFileList) {
        boolean flag = true;
        long time = javaFileList.get(0).mModifiesTime / 1000;
        for (javaFile file : javaFileList) {
            if (file.mModifiesTime / 1000 != time) {
                flag = false;
                break;
            }
        }
        return flag;
    }

}
