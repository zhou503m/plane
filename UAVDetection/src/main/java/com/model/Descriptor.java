package com.model;

import backtype.storm.tuple.Tuple;

import java.awt.*;

public class Descriptor extends CVParticle {

	private Rectangle boundingBox;
	private long duration;
	private float[] values;
	
	public Descriptor(String streamId, long sequenceNr, Rectangle boundingBox, long duration, float[] values) {
		super(streamId, sequenceNr);
		this.boundingBox = boundingBox;
		this.values = values;
		this.duration = duration;
	}
	
	public Descriptor(Tuple tuple, Rectangle boundingBox, long duration, float[] values) {
		super(tuple);
		this.boundingBox = boundingBox;
		this.duration = duration;
		this.values = values;
	}

	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(Rectangle box){
		this.boundingBox = box;
	}
	
	public long getDuration(){
		return duration;
	}

	public float[] getValues() {
		return values;
	}
	
	/**
	 * Simply changes the location of this descriptor by moving it in the provided x,y direction
	 * @param x
	 * @param y
	 */
	public void translate(int x, int y){
		this.boundingBox.x += x;
		this.boundingBox.y += y;
	}
	
	public Descriptor deepCopy(){
		float[] valuesCopy = new float[values.length];
		for(int i=0;i<values.length; i++){
			valuesCopy[i] = values[i];
		}
		Descriptor copy = new Descriptor(new String(this.getStreamId()), this.getSequenceNr(), new Rectangle(this.getBoundingBox()), this.getDuration(), valuesCopy);
		copy.setRequestId(getRequestId());
		copy.setMetadata(this.getMetadata());
		return copy;
	}
	
	public String toString(){
		return "Descriptor {stream:"+getStreamId()+", nr:"+getSequenceNr()+", box:"+boundingBox+" duration: "+duration+"}";
	}
}