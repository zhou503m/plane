package com.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoumeng on 2017/10/28
 */
public class UAVHelper {
    public static List<float[]> convert2List(float[][] f) {
        List<float[]> list = new ArrayList<float[]>();
        for (int i = 0; i < f.length; i++){
            float[] ft = new float[f[i].length];
            for (int j = 0; j < ft.length; j++){
                ft[j] = f[i][j];
            }
            list.add(ft);
        }
        return list;
    }

    public static float[] filterByScore(float[][] f) {
        List<float[]> list = convert2List(f);
        float result[] = null;
        float max = list.get(0)[2];
        for (float[] ft : list) {
            if (ft[2] > max) {
                max = ft[2];
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i)[2] == max ){
                result = list.get(i);
            }
        }
        return result;
    }

    public static void genTxt(String path, String target) throws Exception{

        FileWriter fw = new FileWriter(new File(target));
        BufferedWriter bw =new BufferedWriter(fw);
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        //System.out.println("文件夹:" + file2.getAbsolutePath());
                        //genTxt(file2.getAbsolutePath());
                    } else {
                        //System.out.println("文件:" + file2.getAbsolutePath());
                        bw.write(file2.getAbsolutePath() + "\n");
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
        bw.close();
        fw.close();
    }

    public static void main(String[] args) throws Exception{
        if (args.length < 2) {
            System.out.println("-----use : args[0]:image folder   args[1]:target file path ");
            return;
        }
        String path = args[0];
        String target  = args[1];
        genTxt(path, target);
    }

}
