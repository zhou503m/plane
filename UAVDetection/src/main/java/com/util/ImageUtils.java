package com.util;

import com.model.Frame;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.io.*;

public class ImageUtils {

	/**
	 * Converts an image to byte buffer representing PNG (bytes as they would exist on disk)
	 * @param image
	 * @param encoding the encoding to be used, one of: png, jpeg, bmp, wbmp, gif
	 * @return byte[] representing the image
	 * @throws IOException if the bytes[] could not be written
	 */
	public static byte[] imageToBytes(BufferedImage image, String encoding) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, encoding, baos);
		return baos.toByteArray();
	}
	
	/**
	 * Converts the provided byte buffer into an BufferedImage
	 * @param buf byte[] of an image as it would exist on disk
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage bytesToImage(byte[] buf) throws IOException{
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);
		return ImageIO.read(bais);
	}

	/**
	 *
	 * @param bufferedImage
	 * @return
	 * @author WangRupeng
     */
	public static byte[] decodeToPixels(BufferedImage bufferedImage)
	{
		if(bufferedImage == null)
			return null;
		return ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();
	}

	/**
	 *
	 * @param pixels
	 * @param width
	 * @param height
     * @return
	 *
	 * @author WangRupeng
     */
	public static BufferedImage getImageFromArray(byte[] pixels, int width, int height)
	{
		if(pixels == null || width <= 0 || height <= 0)
			return null;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		byte[] array = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		//int n=  array.length;
		//int m = pixels.length;
		//System.out.println(array.length);
		System.arraycopy(pixels, 0, array, 0, array.length);
		return image;
	}

	/**
	 *
	 * @param bufferedImage
	 * @param formatName
	 * @return
	 * @throws IOException
	 * @author WangRupeng
     */
	public static byte[] encodeToImage(BufferedImage bufferedImage, String formatName) throws IOException {
		if(bufferedImage == null || formatName == null)
			return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, formatName, baos);
		return baos.toByteArray();
	}

	//outputStream转inputStream
	public static ByteArrayInputStream parse(OutputStream out) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos = (ByteArrayOutputStream)out;
		ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
		return swapStream;
	}

	public static boolean saveToFile(BufferedImage bufferedImage, String path,String fileName,String imageType) throws IOException {
		if (bufferedImage == null || path == null) {
			return false;
		}
		if (!path.endsWith("/")) {
			path += "/";
		}
		File file = new File(path + fileName + "." + imageType);
		ImageIO.write(bufferedImage, imageType, file);
		return true;
	}

	/**
	 * @param raw
	 * @param formatName
	 * @return
	 * @throws IOException
	 * @author WangRupeng
     */
	public static byte[] encodeToImage(byte[] raw, String formatName) throws IOException
	{
		if(raw == null || formatName == null)
			return null;
		int d = (int)Math.sqrt(raw.length/3);
		BufferedImage bufferedImage = getImageFromArray(raw, d, d);
		return encodeToImage(bufferedImage, formatName);
	}

	/**
	 * Converts a given image into grayscalse
	 * @param src
	 * @return
	 */
	public static BufferedImage convertToGray(BufferedImage src){
        ColorConvertOp grayOp = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        return grayOp.filter(src,null);
    }
	
	/**
	 * Creates a Mat object for the image in the provided frame
	 * @param frame
	 * @return Mat object representing the image of type Highgui.CV_LOAD_IMAGE_COLOR
	 * @throws IOException if the image cannot be read or converted into binary format
	 */
	public static Mat Frame2Mat(Frame frame) throws IOException{
		if(frame.getImage() == null) throw new IOException("Frame does not contain an image");
		return bytes2Mat(frame.getImageBytes());
	}
	
	/**
	 * Creates a Mat object for the image in the provided frame
	 * @param image the image to be converted to mat
	 * @param imageType the encoding to use, see {@link Frame}
	 * @return Mat object representing the image of type Highgui.CV_LOAD_IMAGE_COLOR
	 * @throws IOException if the image cannot be read or converted into binary format
	 */
	public static Mat Image2Mat(BufferedImage image, String imageType) throws IOException{
		MatOfByte mob = new MatOfByte( ImageUtils.imageToBytes(image, imageType) );
		return Highgui.imdecode(mob, Highgui.CV_LOAD_IMAGE_COLOR);
	}
	
	/**
	 * creates a Mat object directly from a set of bytes 
	 * @param bytes binary representation of an image
	 * @return Mat object of type Highgui.CV_LOAD_IMAGE_COLOR
	 */
	public static Mat bytes2Mat(byte[] bytes){
		MatOfByte mob = new MatOfByte( bytes );
		return Highgui.imdecode(mob, Highgui.CV_LOAD_IMAGE_COLOR);
	}
	
	/**
	 * Creates a byte representation of the provided mat object encoded using the imageType
	 * @param mat
	 * @param imageType
	 * @return
	 */
	public static byte[] Mat2ImageBytes(Mat mat, String imageType){
		MatOfByte buffer = new MatOfByte();
		Highgui.imencode("."+imageType, mat, buffer);
		return buffer.toArray();
	}
}
