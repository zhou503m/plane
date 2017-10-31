package com.api;

import com.model.ImageInfo;
import com.util.UAVHelper;
import com.util.helper.FileLogger;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
/**
 * Created by zhoumeng on 10/21/17.
 */
public class UAVDetect {

    private static FileLogger fileLogger = new FileLogger("/home/hadoop/zm/UAVDetect.txt");
    public native void hello();
    public native void init(String model_file, String weights_file);
    public native float[][] detect(ImageInfo imageInfo);

    static {
        System.load("/home/hadoop/zm/ssd/jni/UAV-jni/src/java/build/libuavdetect.so");
    }

    public static void main(String[] args) throws Exception{
        if (args.length < 1){
            System.out.println("use : args[0] -- iamge txt file path .");
            return;
        }
        UAVDetect uavDetect = new UAVDetect();
        //uavDetect.hello();
        uavDetect.init("/home/hadoop/zm/ssd/model/deploy.prototxt", "/home/hadoop/zm/ssd/model/VGG_VOC0712_SSD_300x300_iter_120000.caffemodel");

        ImageInfo imageInfo;
        long start = System.currentTimeMillis();
        File file=new File(args[0]);
        BufferedReader reader=null;
        String temp = null;
        reader=new BufferedReader(new FileReader(file));
        FileWriter fw = new FileWriter(new File("/home/hadoop/zm/cor.txt"));
        BufferedWriter bw = new BufferedWriter(fw);
            while((temp=reader.readLine())!=null){
                System.out.println("image = " + temp );
                imageInfo = new ImageInfo(temp);
                //ImageInfo imageInfo = new ImageInfo("/home/hadoop/zm/ssd/images/1/1_0361.jpg");
                float[][] result = uavDetect.detect(imageInfo);
                fileLogger.log(" time deley = " , (System.currentTimeMillis() - start) + "");
                start = System.currentTimeMillis();

                float[] realResult = UAVHelper.filterByScore(result);
                for (int j = 0; j < realResult.length; j++) {
                    System.out.print((int)realResult[j] +  ",  ");
                }
                System.out.println();
                bw.write(temp + " " + (int)realResult[3] + " " + (int)realResult[4] + " " + (int)realResult[5] + " " + (int)realResult[6] + "\n");
                //Thread.sleep(3000);

        }
        bw.close();
        fw.close();
        reader.close();




        //if mem out

/*
        int count = 0 ;
        while (count++ < 100) {
            ImageInfo imageInfo = new ImageInfo("/home/hadoop/zm/ssd/images/1/1_0564.jpg");
            uavDetect.detect(imageInfo);
            System.out.println("detect : " +  count);
        }
*/

    }
}