package com.api;

/**
 * Created by zhoumeng on 10/20/17.
 */
public class SSDTool {

    public native void sayHello(); //test hello_world jni
    public native float[][] extract(); //none Java paraments jni
    //public native float[][] detect(ImageInfo imageInfo); //detcet jni -- final

    static {
        //System.load("/home/hadoop/zm/ssd/jni/detect/libssd.so");
        //System.loadLibrary("ssd");
        System.load("/home/hadoop/zm/jnitest/libhello.so");
        System.out.println("load lib..");
    }

    public static void main(String[] args) {
        //System.load("/home/hadoop/zm/ssd/jni/detect/libssd.so");
       // System.loadLibrary("ssd");
        System.out.println(System.getProperty("java.library.path"));
        SSDTool ssdTool = new SSDTool();
        //ssdTool.extract();
        ssdTool.sayHello();
    }
}
