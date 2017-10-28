package com.util;

import backtype.storm.utils.Utils;
import com.model.Frame;
import com.util.helper.FileLogger;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by WangRupeng on 2017/4/22.
 */
public class RtspStreamReader implements Runnable{

    private Logger logger = LoggerFactory.getLogger(RtspStreamReader.class);
    private String streamId;
    private boolean running = false; // indicator if the reader is still active
    private LinkedBlockingQueue<Frame> frameQueue; // queue used to store frames
    private long lastRead = -1; // used to determine if the EOF was reached if Xuggler does not detect it
    private int sleepTime;
    private boolean useSingleID = false;
    private double frameRate ;

    private String streamLocation;
    private LinkedBlockingQueue<String> videoList = null;
    private String imageType = Frame.JPG_IMAGE;


    private FFmpegFrameGrabber mGrabber;
    private Java2DFrameConverter mImageConverter;

    private org.bytedeco.javacv.Frame frame = null;
    private int oldW;
    private int oldH;
    private BufferedImage bi = null;
    private BufferedImage bi_copy = null;
    private int lastNumber = -1;
    private int frameNumber = 0;
    private int frameLength = 0;

    private long firstTime = 0;

    private int frameSkip = 1;

    public RtspStreamReader(LinkedBlockingQueue<String> videoList, String imageType, int frameSkip, int groupSize, int sleepTime, boolean uniqueIdPerFile, LinkedBlockingQueue<Frame> frameQueue){
        this.videoList = videoList;
        this.imageType = imageType;
        this.frameSkip = frameSkip;
        this.sleepTime = sleepTime;
        this.frameQueue = frameQueue;
        this.useSingleID = uniqueIdPerFile;
        this.streamId = ""+this.hashCode(); // give a default streamId
        lastRead = System.currentTimeMillis()+10000;

        mImageConverter = new Java2DFrameConverter();

    }

    public RtspStreamReader(String streamId, String streamLocation, String imageType, int frameSkip, int groupSize, int sleepTime, LinkedBlockingQueue<Frame> frameQueue){
        this.streamLocation = streamLocation;
        this.imageType = imageType;
        this.sleepTime = sleepTime;
        this.frameQueue = frameQueue;
        this.streamId = streamId;
        lastRead = System.currentTimeMillis()+10000;

        mImageConverter = new Java2DFrameConverter();

    }

    public RtspStreamReader(String streamId, String streamLocation, String imageType, long timeSkip, int groupSize, int sleepTime, LinkedBlockingQueue<Frame> frameQueue){
        this.streamLocation = streamLocation;
        this.imageType = imageType;
        this.sleepTime = sleepTime;
        this.frameQueue = frameQueue;
        this.streamId = streamId;
        lastRead = System.currentTimeMillis()+10000;

        mImageConverter = new Java2DFrameConverter();

    }
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                // if a url was provided read it
                if (videoList == null && streamLocation != null) {
                    logger.info("Start reading stream: " + streamLocation);

                    //Grabber init
                    mGrabber = FFmpegFrameGrabber.createDefault(streamLocation);
                    mGrabber.setOption("rtsp_transport", "tcp");
                    mGrabber.setImageWidth(1280);
                    mGrabber.setImageHeight(720);
                    mGrabber.start();
                    logger.info(streamLocation + " started");

                    try {
                        //read framerate
                        frameRate = mGrabber.getFrameRate();
                        logger.info("Frame rate is " + frameRate);
                    } catch (Exception e) {
                        logger.info("Unable to read metadata from video");
                    }
                } else if (videoList != null) {
                    logger.info("Waiting for new file to be downloaded...");
                    streamLocation = videoList.take();
                    if (!useSingleID) {
                        streamId = "" + streamLocation.hashCode();
                        if (streamLocation.contains("/"))
                            streamId = streamLocation.substring(streamLocation.lastIndexOf('/') + 1) + "_" + streamId;
                    }

                    mGrabber = new FFmpegFrameGrabber(streamLocation);
                    logger.info("Start reading File: " + streamLocation);

                    try {
                        //read framerate
                        frameRate = mGrabber.getFrameRate();
                        logger.info("Frame rate is " + frameRate);
                    } catch (Exception e) {
                        logger.info("Unable to read metadata from video");
                    }
                } else {
                    logger.error("No video list or url provided, nothing to be read");
                    break;
                }

                lastRead = System.currentTimeMillis() + 10000;

                //from video files
                if (streamLocation.startsWith("file://") || streamLocation.startsWith("/")) {
                    frameLength = mGrabber.getLengthInFrames();
                }

                //start grab image
                logger.info("Start to grab image from " + streamLocation);
                while (frameLength <= 0 || frameNumber < frameLength) {
                    try {
                        //fileLogger.log("frameLength ", frameLength+"");
                        //fileLogger.log("frameNumber ", frameNumber+"");

                        logger.info("Time between two frames used " + (System.currentTimeMillis() - firstTime) + "");
                        //grab one frame, with no audio
                        frame = mGrabber.grabImage();
                        firstTime = System.currentTimeMillis();
                        if (frame == null) {
                            continue;
                        }
                        frameNumber = mGrabber.getFrameNumber();

                        //grab the same frame, continue
                        if (frameNumber == lastNumber) {
                            continue;
                        }
                        //grab different frame, record the frame number
                        lastNumber = frameNumber;
                    } catch (FrameGrabber.Exception e) {
                        continue;
                    }

                    //set to grab one frame per second
                    /*if (frameNumber % (int) frameRate != 0) {
                        continue;
                    }*/
                    if (frameNumber % frameSkip != 0) {
                        continue;
                    }
                    if (frame != null) {
                        try {
                            //logger.info("frame number : " + lastNumber + "|time : " + firstTime + "|streamid : " + streamId);
                            oldW = frame.imageWidth;
                            oldH = frame.imageHeight;


                            bi = new BufferedImage(oldW, oldH, BufferedImage.TYPE_3BYTE_BGR);
                            //bi_copy = new BufferedImage(oldW, oldH, BufferedImage.TYPE_3BYTE_BGR);

                            //dispose graphics resource
                            Graphics graphics = bi.getGraphics();
                            //BufferedImage bufferedImage = mImageConverter.getBufferedImage(frame); //20 ms
                            //graphics.drawImage(bufferedImage, 0, 0, oldW, oldH, null);
                            graphics.drawImage(mImageConverter.getBufferedImage(frame), 0, 0, oldW, oldH, null); //30 ms
                            //bi_copy.setData(bi.getData()); //30 ms

                            byte[] buffer = ImageUtils.decodeToPixels(bi);
                            byte[] buffer_copy = ImageUtils.decodeToPixels(/*bi_copy*/bi);

                            //get timestamp
                            //long timestamp = (long) frameRate;
                            //if (frameRate > 0) timestamp = lastNumber * (long) frameRate;


                            //logger.info("timestamp is " + timestamp);
                            Frame newFrame = new Frame(streamId, lastNumber, imageType, buffer, buffer_copy , firstTime, new Rectangle(0, 0, oldW, oldH));
                            //System.out.println("Frame Info is " + newFrame.toString());

                            newFrame.getMetadata().put("uri", streamLocation);
                            frameQueue.put(newFrame);
                            //logger.info("Queue size is " + frameQueue.size());

                            // enforced throttling
                            if (sleepTime > 0) Utils.sleep(sleepTime);
                            // queue based throttling
                            if (frameQueue.size() > 20) Utils.sleep(frameQueue.size());
                            graphics.dispose();
                            //Utils.sleep(1000);
                        } catch (Exception e) {

                            e.printStackTrace();

                        }
                    }
                }

                mGrabber.stop();
                mGrabber.release();
            } catch (Exception e) {
                logger.warn("Stream closed unexpectatly: "+e.getMessage(), e);
                // sleep a minute and try to read the stream again
                Utils.sleep(1 * 60 * 1000);
            }
        }
    }

    /**
     * Tells the StreamReader to stop reading frames
     */
    public void stop() {
        running = false;
    }

    /**
     * Returns whether the StreamReader is still active or not
     * @return
     */
    public boolean isRunning(){
        // kill this thread if the last frame read is to long ago (means Xuggler missed the EoF) and clear resources
        if(lastRead > 0 && System.currentTimeMillis() - lastRead > 3000){
            running = false;
            return this.running;
        }
        return true;
    }
}
