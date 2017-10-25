package com.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.FileInputStream;

/**
 * Created by zhoumeng on 10/23/17.
 */
public class ImageInfo {
        public byte[] pixels;
        public int width;
        public int height;

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
