package com.fetcher;

import backtype.storm.task.TopologyContext;
import com.model.CVParticle;
import com.model.serializer.CVParticleSerializer;

import java.io.Serializable;
import java.util.Map;

public interface IFetcher<Output extends CVParticle> extends Serializable {

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context) throws Exception;
	
	public CVParticleSerializer<Output> getSerializer();
	
	public void activate();

	public void deactivate() ;
	
	public Output fetchData();
	
}
