package com.model;

import backtype.storm.tuple.Tuple;

import java.util.List;

public class GroupOfFrames extends CVParticle{

	private List<Frame> frames;
	
	public GroupOfFrames(String streamId, long sequenceNr, List<Frame> frames) {
		super(streamId, sequenceNr);
		this.frames = frames;
	}
	
	public GroupOfFrames(Tuple tuple, List<Frame> list) {
		super(tuple);
		this.frames = list;
	}
	
	public List<Frame> getFrames(){
		return frames;
	}
	
	public int nrOfFrames(){
		return frames.size();
	}

}
