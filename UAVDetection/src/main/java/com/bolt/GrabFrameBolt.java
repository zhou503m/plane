package com.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.util.helper.FileLogger;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhoumeng on 10/25/17.
 */
public class GrabFrameBolt extends BaseRichBolt{
    private OutputCollector collector;
    private FileLogger fileLogger;
    private static String TAG = "BaseRichBolt";

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
        fileLogger = new FileLogger("C:\\Users\\Administrator\\Desktop\\ssd\\log\\log.txt");
    }

    public void execute(Tuple tuple) {
        long req_id = (long)tuple.getValue(0);
        String className = (String) tuple.getValue(1);
        String stream_id = (String)tuple.getValue(2);
        long sequnce_num = (long)tuple.getValue(3);
        HashMap<String, Object> meta_data = (HashMap<String, Object>)tuple.getValue(4);

        fileLogger.log("FRAME " + sequnce_num, "req_id is :"+req_id +", meta_data is :" );

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
