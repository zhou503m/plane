package com.model;

import backtype.storm.tuple.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Feature extends CVParticle{

	private String name;
	private long duration;
	private List<Descriptor> sparseDescriptors = new ArrayList<Descriptor>();
	private float[][][] denseDescriptors = new float[0][0][0];
	
	public Feature(String streamId, long sequenceNr, String name, long duration, List<Descriptor> sparseDescriptors, float[][][] denseDescriptors) {
		super(streamId, sequenceNr);
		this.name = name;
		this.duration = duration;
		if(sparseDescriptors != null){
			this.sparseDescriptors = sparseDescriptors;
		}
		if(denseDescriptors != null){
			this.denseDescriptors = denseDescriptors;
		}
	}
	
	public Feature(Tuple tuple, String name, long duration, List<Descriptor> sparseDescriptors, float[][][] denseDescriptors) {
		super(tuple);
		this.name = name;
		this.duration = duration;
		if(sparseDescriptors != null){
			this.sparseDescriptors = sparseDescriptors;
		}
		if(denseDescriptors != null){
			this.denseDescriptors = denseDescriptors;
		}
	}

	public String getName() {
		return name;
	}

	public List<Descriptor> getSparseDescriptors() {
		return sparseDescriptors;
	}
	
	public float[][][] getDenseDescriptors(){
		return denseDescriptors;
	}
	
	public long getDuration(){
		return this.duration;
	}
	
	public Feature deepCopy(){
		float[][][] denseCopy = new float[denseDescriptors.length][][];
		for(int x=0; x<denseDescriptors.length; x++){
			denseCopy[x] = new float[denseDescriptors[x].length][];
			for(int y=0; y<denseDescriptors[x].length; y++){
				denseCopy[x][y] = Arrays.copyOf(denseDescriptors[x][y], denseDescriptors[x][y].length);
			}
		}
		
		List<Descriptor> sparseCopy = new ArrayList<Descriptor>(this.sparseDescriptors.size());
		for(Descriptor d : sparseDescriptors){
			sparseCopy.add(d.deepCopy());
		}
		
		Feature copyFeature = new Feature(new String(this.getStreamId()), this.getSequenceNr(), new String(this.getName()), this.getDuration(), 
				sparseCopy, denseCopy);
		copyFeature.setRequestId(getRequestId());
		copyFeature.setMetadata(this.getMetadata());
		return copyFeature;
	}
	
	public String toString(){
		return "Feature {stream:"+getStreamId()+", nr:"+getSequenceNr()+", username: "+name+", descriptors: "+sparseDescriptors+"}";
	}
}
