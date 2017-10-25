package com.spider.DataHelper;

import com.api.UAVDetect;
import com.model.ImageInfo;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Created by zhoumeng on 10/24/17.
 */
public class FilterBySSD {
    public static void main(String[] args) throws Exception{
        UAVDetect uavDetect = new UAVDetect();
        String imageFolder = "/home/hadoop/zm/uav-data/UAV-Images-raw/";
        if (args.length == 1) {
            imageFolder = args[0];
        }
        float[][] result;
        String resultPath = "/home/hadoop/zm/detected_image.txt";
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultPath));
        File file = new File(imageFolder);
        File flist[] = file.listFiles();
        try {
            if (flist == null) {
                throw (new NullPointerException());
            }
            for (File f : flist) {
                System.out.println(f.getAbsolutePath());
                ImageInfo imageInfo = new ImageInfo(f.getAbsolutePath());
                result = uavDetect.detect(imageInfo);
                for (int i = 0; i < result.length; i++){
                    if (result[i][2] == 15.0){
                        bufferedWriter.write(f.getAbsolutePath() + "\n");
                    }
                    bufferedWriter.write(result[i][1] + "\n");
                }

            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bufferedWriter.close();
        }

    }

}
