package com.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import java.util.Map;

/**
 * Created by zhoumeng on 10/25/17.
 */
public class GrabFrameBolt extends BaseRichBolt{
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        
    }

    public void execute(Tuple tuple) {

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
