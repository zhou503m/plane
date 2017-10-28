package com.model;

import backtype.storm.tuple.Tuple;
import com.util.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Frame extends CVParticle {

	public final static String NO_IMAGE = "none";
	public final static String JPG_IMAGE = "jpg";
	public final static String PNG_IMAGE = "png";
	public final static String GIF_IMAGE = "gif";
	
	private long timeStamp;
	private String imageType = JPG_IMAGE;
	private byte[] imageBytes;
	private BufferedImage image;
	private Rectangle boundingBox;
    private String text = "flag";

	private List<SearchEntity> entities = new ArrayList<SearchEntity>();
	private List<FaceSearchFeature> faceSearchFeatures = new ArrayList<FaceSearchFeature>();


	private byte[] boxedImage;
	private BufferedImage boxedBufferedImage;

    public void setText(String t) {
        this.text = t;
    }

    public String getText() {
        return this.text;
    }

	public Frame(String streamId, long sequenceNr, String imageType, BufferedImage image, long timeStamp, Rectangle boundingBox, List<Feature> features) throws IOException {
		this(streamId, sequenceNr, imageType, image, timeStamp, boundingBox);
		//if(features != null) this.features = features;
	}
	
	public Frame(String streamId, long sequenceNr, String imageType, BufferedImage image, long timeStamp, Rectangle boundingBox ) throws IOException {
		super( streamId, sequenceNr);
		this.imageType = imageType;
		setImage(image);
		this.timeStamp = timeStamp;
		this.boundingBox = boundingBox;
	}
	
	public Frame(String streamId, long sequenceNr, String imageType, byte[] image, long timeStamp, Rectangle boundingBox, List<Feature> features) {
		this(streamId, sequenceNr, imageType, image, timeStamp, boundingBox);
		//if(features != null) this.features = features;
	}


	public void addFaceEntity(List<SearchEntity> entities) {
		this.entities = entities;
	}

	public List<SearchEntity> getEntities(){
		return this.entities;
	}

	public void addFaceSearchFeatures(FaceSearchFeature faceSearchFeature) {
	    faceSearchFeatures.add(faceSearchFeature);
    }


	public void setFaceSearchFeatures(List<FaceSearchFeature> faceSearchFeatures) {
	    this.faceSearchFeatures = faceSearchFeatures;
    }

    public List<FaceSearchFeature> getFaceSearchFeatures() {
	    return this.faceSearchFeatures;
    }

	public void setBoxedImage(byte[] image) {
		this.boxedImage = image;
	}

	public byte[] getBoxedImage() {
		return this.boxedImage;
	}

	public BufferedImage getBoxedBufferedImage() throws IOException {
		if(boxedImage == null) {
			//imageType = NO_IMAGE;
			return null;
		}
		if(this.boxedBufferedImage == null){
			//image = ImageUtils.bytesToImage(imageBytes);
			//System.out.println(imageBytes.length + " " + (int)boundingBox.getWidth() + " " + (int)boundingBox.getHeight());
			//int width = image.getWidth();
			//int height = image.getHeight();
			this.boxedBufferedImage = ImageUtils.getImageFromArray(boxedImage,(int)boundingBox.getWidth(),(int)boundingBox.getHeight());
		}
		return this.boxedBufferedImage;
	}

	public void setBoxedBufferedImage(BufferedImage bufferedImage) {
		this.boxedBufferedImage = bufferedImage;
		if(bufferedImage != null){
			this.boxedImage = ImageUtils.decodeToPixels(bufferedImage);
		}else{
			this.boxedImage = null;
		}
	}

	public Frame(String streamId, long sequenceNr, String imageType, byte[] image, long timeStamp, Rectangle boundingBox ) {
		super(streamId, sequenceNr);
		this.imageType = imageType;
		this.imageBytes = image;
		this.timeStamp = timeStamp;
		this.boundingBox = boundingBox;
	}

	public Frame(String streamId, long sequenceNr, String imageType, byte[] image, byte[] image_copy, long timeStamp, Rectangle boundingBox ) {
		super(streamId, sequenceNr);
		this.imageType = imageType;
		this.boxedImage = image_copy;
		this.imageBytes = image;
		this.timeStamp = timeStamp;
		this.boundingBox = boundingBox;
	}

	
	public Frame(Tuple tuple, String imageType, byte[] image, long timeStamp, Rectangle box, String text, byte[] boximage, List<FaceSearchFeature> faceSearchFeatures) {
		super(tuple);
		this.imageType = imageType;
		this.imageBytes = image;
		this.timeStamp = timeStamp;
		this.boundingBox = box;
        this.text = text;
        this.boxedImage = boximage;
        //this.entities = entities;
        this.faceSearchFeatures = faceSearchFeatures;
	}

	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(Rectangle rectangle) {
		this.boundingBox = rectangle;
	}
	
	public BufferedImage getImage() throws IOException {
		if(imageBytes == null) {
			imageType = NO_IMAGE;
			return null;
		}
		if(image == null){
			//image = ImageUtils.bytesToImage(imageBytes);
            //System.out.println(imageBytes.length + " " + (int)boundingBox.getWidth() + " " + (int)boundingBox.getHeight());
			//int width = image.getWidth();
			//int height = image.getHeight();
			image = ImageUtils.getImageFromArray(imageBytes,(int)boundingBox.getWidth(),(int)boundingBox.getHeight());
        }
		return image;
	}

	public void setImage(BufferedImage image) throws IOException {
		this.image = image;
		if(image != null){
			if(imageType.equals(NO_IMAGE)) imageType = JPG_IMAGE;
			//this.imageBytes = ImageUtils.imageToBytes(image, imageType);
            this.imageBytes = ImageUtils.decodeToPixels(image);
		}else{
			this.imageBytes = null;
			this.imageType = NO_IMAGE;
		}
	}
	
	public void setImage(byte[] imageBytes, String imgType){
		this.imageBytes = imageBytes;
		this.imageType = imgType;
		this.image = null;
	}
	
	public void removeImage(){
		this.image = null;
		this.imageBytes = null;
		this.imageType = NO_IMAGE;
	}


	public long getTimestamp(){
		return this.timeStamp;
	}

	/*public List<Feature> getFeatures() {
		return features;
	}*/

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) throws IOException {
		this.imageType = imageType;
		if(image != null){
			imageBytes = ImageUtils.imageToBytes(image, imageType);
		}else{
			image = ImageUtils.bytesToImage(imageBytes);
			imageBytes = ImageUtils.imageToBytes(image, imageType);
		}
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public String toString(){
		String result= "Frame : {streamId:"+getStreamId()+", sequenceNr:"+getSequenceNr()+", timestamp:"+getTimestamp()+", imageType:"+imageType;
		//for(Feature f : features) result += f.getUsername()+" = "+f.getSparseDescriptors().size()+", ";
		return result + " }";
	}
}

