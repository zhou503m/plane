package com.util;

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


}
