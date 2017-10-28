package com.fetcher;

import backtype.storm.task.TopologyContext;
import com.config.StormCVConfig;
import com.model.CVParticle;
import com.model.Frame;
import com.model.GroupOfFrames;
import com.model.serializer.CVParticleSerializer;
import com.model.serializer.FrameSerializer;
import com.model.serializer.GroupOfFramesSerializer;
import com.util.RtspStreamReader;
import com.util.helper.FileLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by WangRupeng on 2017/4/22.
 */
public class UrlImageFetcher implements IFetcher<CVParticle> {
    private static final long serialVersionUID = 7135212329614102711L;
    private String TAG = "UrlImageFetcher";
    protected List<String> locations;
    protected int frameSkip = 1;
    protected long timeSkip = 0;
    protected LinkedBlockingQueue<Frame> frameQueue = new LinkedBlockingQueue<Frame>(30);

    private int batchSize = 1;
    private String id;
    private String imageType;
    private List<Frame> frameGroup;
    private int sleepTime = 0;
    private int groupSize = 1;

    private String userDir ;
    //set File log
    private FileLogger fileLogger ;
    private Logger logger = LoggerFactory.getLogger(UrlImageFetcher.class);
    private int mId;

    protected Map<String, RtspStreamReader> streamReaders;

    public UrlImageFetcher(List<String> locations) {
        this.locations =  locations;
    }

    public UrlImageFetcher frameSkip(int skip) {
        this.frameSkip = skip;
        return this;
    }

    public UrlImageFetcher timeSkip(long time) {
        this.timeSkip = time;
        return this;
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context) throws Exception {

        String logDir = (String)stormConf.get(StormCVConfig.STORMCV_LOG_DIRECTORY);
        userDir = System.getProperty("user.dir").replaceAll("\\\\", "/");
        mId = context.getThisTaskId();
        fileLogger = new FileLogger(logDir + TAG + "@" +mId);

        fileLogger.log(TAG + "@" + mId, userDir);
        this.id = context.getThisComponentId();
        int nrTasks = context.getComponentTasks(id).size();
        int taskIndex = context.getThisTaskIndex();

        if(stormConf.containsKey(StormCVConfig.STORMCV_FRAME_ENCODING)){
            imageType = (String)stormConf.get(StormCVConfig.STORMCV_FRAME_ENCODING);
        }

        // change the list based on the number of tasks working on it
        if(this.locations != null && this.locations.size() > 0){
            int batchSize = (int) Math.floor(locations.size() / nrTasks);
            int start = batchSize * taskIndex;
            locations = locations.subList(start, Math.min(start + batchSize, locations.size()));
        }
    }

    public UrlImageFetcher groupSize(int size){
        this.groupSize  = size;
        return this;
    }

    public UrlImageFetcher sleep(int ms){
        this.sleepTime = ms;
        return this;
    }

    public UrlImageFetcher groupOfFramesOutput(int nrFrames) {
        this.batchSize = nrFrames;
        return this;
    }

    @Override
    public CVParticleSerializer getSerializer() {
        if(batchSize  <= 1) return new FrameSerializer();
        else return new GroupOfFramesSerializer();
    }

    @Override
    public void activate() {
        if(streamReaders != null) {
            this.deactivate();
        }

        streamReaders = new HashMap<String, RtspStreamReader>();
        for(String location : locations) {
            //String streamId = ""+location.hashCode();
            String streamId = getCameraID(location);
            if (streamId.equals("")) {
                streamId = "" + location.hashCode();
            }
            /*if(location.contains("/")) {
                //streamId = id+"_"+location.substring(location.lastIndexOf("/")+1) + "_" + streamId;
                streamId = streamId + "_" + location.substring(location.lastIndexOf("/") + 1);
            }*/
            fileLogger.log(TAG + "@" + mId,"stream location is " + location + ",stream id is " + streamId);
            logger.info("stream location is " + location + ",stream id is " + streamId);
            RtspStreamReader reader = new RtspStreamReader(streamId, location, imageType, frameSkip, groupSize, sleepTime, frameQueue);
            fileLogger.log(TAG+"@" +mId , "reader running is :" + reader.isRunning());
            streamReaders.put(location, reader);
            new Thread(reader).start();
        }
    }

    //get camera the last block of local ip as the stream id
    private String getCameraID(String location) {
        //only local ip url
        String regEx = "192.168.1.\\d+";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(location);
        String result = "";
        if (matcher.find()) {
            System.out.println(matcher.group(0));
            result = matcher.group(0);
            result = result.substring(result.length() - 2, result.length());
        } else {
            if(location.contains("/")) {
                result = location.hashCode() + "_" + location.substring(location.lastIndexOf("/") + 1);
            }
        }
        return result;
    }

    @Override
    public void deactivate() {
        if(streamReaders != null) for(String location : streamReaders.keySet()){
            streamReaders.get(location).stop();
        }
        streamReaders = null;
    }

    @Override
    public CVParticle fetchData() {
        if(streamReaders == null) this.activate();
        Frame frame = frameQueue.poll();
        if(frame != null) {
            if(batchSize <= 1){
                return frame;
            }else{
                if(frameGroup == null || frameGroup.size() >= batchSize)
                    frameGroup = new ArrayList<Frame>();
                frameGroup.add(frame);
                if(frameGroup.size() == batchSize){
                    return new GroupOfFrames(frameGroup.get(0).getStreamId(), frameGroup.get(0).getSequenceNr(), frameGroup);
                }
            }
        }
        return null;
    }
}
