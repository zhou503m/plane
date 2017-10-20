package com.api;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;

public class SsdDetect{

    static {
        System.loadLibrary("ssd");
    }

    private class ImageInfo
    {
        public byte[] pixels;
        public int width;
        public int height;
        //test
        public String test_str = "hello_world..";

        public ImageInfo()
        {

        }

        public ImageInfo(byte[] pixels, int width, int height)
        {
            this.pixels = pixels;
            this.width = width;
            this.height = height;
        }

        public ImageInfo(String imagePath) throws Exception {
            BufferedImage image = ImageIO.read(new FileInputStream(imagePath));
            this.pixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
            this.width = image.getWidth();
            this.height = image.getHeight();
        }
    }


    public native void sayHello(); //test hello_world jni
    public native float[][] extract(); //none Java paraments jni 
	public native float[][] detect(ImageInfo imageInfo); //detcet jni -- final

    public static void main(String args[]) throws Exception{
        SsdDetect detector = new SsdDetect();
        //test jni methods
        detector.sayHello();

        float[][] extarctInfo = detector.extract();
        ImageInfo imageInfo = detector.new ImageInfo("/home/hadoop/zm/ssd/jni/detect/cat.jpg");
        // 静态内部类实例化方法SsdDetect.ImageInfo imageInfo = new SsdDetect.ImageInfo("/home/hadoop/zm/ssd/jni/detect/cat.jpg");
        System.out.println(imageInfo.width + "  " + imageInfo.height);
        float[][] detectInfo =  detector.detect(imageInfo);

        //print detectInfo
        for (int i=0; i<detectInfo.length; i++) {
            System.out.print(i + "   :");
            for (int j=0; j<detectInfo[i].length; j++) {
                System.out.print(detectInfo[i][j] + "    ");
            }
            System.out.println();
        }

        System.out.println("-----------extract------");
        for (int i=0; i<extarctInfo.length; i++) {
            System.out.print(i + "   :");
            for (int j=0; j<extarctInfo[i].length; j++) {
                System.out.print(extarctInfo[i][j] + "    ");
            }
            System.out.println();
        }
    }
}
