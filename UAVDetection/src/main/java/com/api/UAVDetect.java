package com.api;

//import com.google.gson.Gson;
import com.model.ImageInfo;

/**
 * Created by zhoumeng on 10/21/17.
 */
public class UAVDetect {
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
        ImageInfo imageInfo = new ImageInfo("/home/hadoop/zm/uav-data/UAV-Images-raw/00168.jpg");
        float[][] result = uavDetect.detect(imageInfo);
        //Gson gson = new Gson();
        //System.out.println(gson.toJson(result));
        for (int i = 0; i < result.length; i++){
            for (int j = 0; j < result[i].length; j++)
                System.out.print(result[i][j] + "  ");
            System.out.println();
        }

    }
}