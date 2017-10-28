package com.api;

//import com.google.gson.Gson;
import com.model.ImageInfo;
import com.util.UAVHelper;
import com.util.helper.FileLogger;
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
        UAVDetect uavDetect = new UAVDetect();
        uavDetect.hello();
        uavDetect.init("/home/hadoop/zm/ssd/model/deploy.prototxt", "/home/hadoop/zm/ssd/model/VGG_VOC0712_SSD_300x300_iter_120000.caffemodel");
        //ImageInfo imageInfo = new ImageInfo("/home/hadoop/zm/uav-data/UAV-Images-raw/00168.jpg");
        long start = System.currentTimeMillis();
        List<float[]> list = null;
        for (int i = 0; i < 50; i++){
            ImageInfo imageInfo = new ImageInfo("/home/hadoop/zm/test-image/2.jpg");
            float[][] result = uavDetect.detect(imageInfo);
            fileLogger.log(" time deley :" , (System.currentTimeMillis() - start) + "");
            start = System.currentTimeMillis();
            float[] realResult = UAVHelper.filterByScore(result);
            for (int j = 0; j < realResult.length; j++) {
                System.out.print(realResult[j] +  ",  ");
            }
            System.out.println();

        }

        //Gson gson = new Gson();
        //System.out.println(gson.toJson(result));
//        for (int i = 0; i < result.length; i++){
//            for (int j = 0; j < result[i].length; j++)
//                System.out.print(result[i][j] + "  ");
//            System.out.println();
//        }

    }
}