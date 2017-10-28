package com;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import com.bolt.GrabFrameBolt;
import com.config.StormCVConfig;
import com.fetcher.UrlImageFetcher;
import com.model.Frame;
import com.spout.CVParticleSpout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoumeng on 10/25/17.
 */
public class Topology {
    public static void main(String[] args) throws InterruptedException, IOException {
        StormCVConfig conf = new StormCVConfig();
        conf.setNumWorkers(4);
        conf.setMaxSpoutPending(32);
        conf.put(StormCVConfig.STORMCV_FRAME_ENCODING, Frame.JPG_IMAGE);
        conf.put(Config.TOPOLOGY_ENABLE_MESSAGE_TIMEOUTS, true);
        conf.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS , 10); // The maximum amount of time given to the topology to fully process a message emitted by a spout (default = 30)
        conf.put(StormCVConfig.STORMCV_SPOUT_FAULTTOLERANT, false); // indicates if the spout must be fault tolerant; i.e. spouts do NOT! replay tuples on fail
        conf.put(StormCVConfig.STORMCV_CACHES_TIMEOUT_SEC, 30); // TTL (seconds) for all elements in all caches throughout the topology (avoids memory overload)
        //conf.put(StormCVConfig.STORMCV_LOG_DIRECTORY,"/home/hadoop/storm-projects/stormcv-log/");
        conf.put(StormCVConfig.STORMCV_LOG_DIRECTORY,"C:\\Users\\Administrator\\Desktop\\ssd\\log\\");
        conf.put(StormCVConfig.STORMCV_RTMP_URL, "rtp://127.0.0.1:80");
        //conf.put(StormCVConfig.STORMCV_HDFS_URL,"hdfs://hadoop05:9000/user/tmp/realtime/");
        //conf.put(StormCVConfig.STORMCV_REDIS_HOST, "121.42.164.108");
        //conf.put(StormCVConfig.STORMCV_REDIS_PASSWORD, "wang12345");
        //conf.put(StormCVConfig.STORMCV_REDIS_PORT, "6379");

        //add resources urls
        List<String> urls = new ArrayList<String>();
        urls.add("rtsp://admin:123456@192.168.1.61:554/unicast/c1/s0/live");

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", new CVParticleSpout(new UrlImageFetcher(urls).frameSkip(5)), 1);

        builder.setBolt("ssd-detect", new GrabFrameBolt(), 1).shuffleGrouping("spout");

        try {
            // run in local mode
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology( "video", conf, builder.createTopology());
			/*Utils.sleep(1200*1000); // run for one minute and then kill the topology
			cluster.shutdown();
			System.exit(1);*/

            // run on a storm cluster(multi server)
            // StormSubmitter.submitTopology("some_topology_name", conf, builder.createTopology());
        } catch (Exception e){
            e.printStackTrace();
        }



    }
}





















